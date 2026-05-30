USE master;
GO
IF EXISTS (SELECT name FROM sys.databases WHERE name = N'CHGiaDung')
BEGIN
    ALTER DATABASE CHGiaDung SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE CHGiaDung;
END
GO
CREATE DATABASE CHGiaDung;
GO
USE CHGiaDung;
GO

-- ============================================================
-- BẢNG 1: nga_users
-- ============================================================
CREATE TABLE nga_users (
  user_id       INT          IDENTITY(1,1) PRIMARY KEY,
  username      VARCHAR(60)  NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name     NVARCHAR(150),
  email         NVARCHAR(100),
  phone         VARCHAR(20),
  role          NVARCHAR(20) DEFAULT 'staff',
  status        VARCHAR(20)  DEFAULT 'active',
  last_login    DATETIME,
  avatar_url    NVARCHAR(255),
  created_at    DATETIME     DEFAULT GETDATE(),
  CONSTRAINT chk_user_status CHECK (status IN ('active','inactive')),
  CONSTRAINT chk_user_role   CHECK (role   IN ('admin','staff','customer'))
);
GO
CREATE UNIQUE INDEX uix_users_email ON nga_users(email) WHERE email IS NOT NULL;
GO

-- ============================================================
-- BẢNG 2: nga_customers
-- gender dùng LIKE thay vì IN để tránh lỗi collation Unicode
-- ============================================================
CREATE TABLE nga_customers (
  customer_id  INT           IDENTITY(1,1) PRIMARY KEY,
  username     VARCHAR(60),
  full_name    NVARCHAR(150) NOT NULL,
  phone        VARCHAR(20),
  email        VARCHAR(150),
  address      NVARCHAR(200),
  dob          DATE,
  gender       NVARCHAR(10),
  created_at   DATETIME      DEFAULT GETDATE(),
  FOREIGN KEY (username) REFERENCES nga_users(username),
  CONSTRAINT chk_cust_phone  CHECK (phone  IS NULL OR phone  LIKE '[0-9]%'),
  CONSTRAINT chk_cust_email  CHECK (email  IS NULL OR email  LIKE '%@%'),
  CONSTRAINT chk_cust_gender CHECK (gender IS NULL OR gender IN ('Nam','Nu','Khac') OR gender LIKE N'N%')
);
GO
CREATE UNIQUE INDEX uix_cust_username ON nga_customers(username) WHERE username IS NOT NULL;
CREATE UNIQUE INDEX uix_cust_phone    ON nga_customers(phone)    WHERE phone    IS NOT NULL;
CREATE UNIQUE INDEX uix_cust_email    ON nga_customers(email)    WHERE email    IS NOT NULL;
GO

-- ============================================================
-- BẢNG 3: nga_categories
-- ============================================================
CREATE TABLE nga_categories (
  category_id INT           IDENTITY(1,1) PRIMARY KEY,
  parent_id   INT           NULL,
  name        NVARCHAR(100) NOT NULL,
  description NVARCHAR(1000),
  image_url   NVARCHAR(255),
  sort_order  INT           DEFAULT 0,
  status      VARCHAR(20)   DEFAULT 'active',
  created_at  DATETIME      DEFAULT GETDATE(),
  CONSTRAINT fk_cat_parent  FOREIGN KEY (parent_id) REFERENCES nga_categories(category_id),
  CONSTRAINT chk_cat_status CHECK (status IN ('active','inactive'))
);
GO

-- ============================================================
-- BẢNG 4: tra_products
-- ============================================================
CREATE TABLE tra_products (
  product_id   INT           IDENTITY(1,1) PRIMARY KEY,
  category_id  INT           NOT NULL,
  supp_name    NVARCHAR(100),
  supp_contact NVARCHAR(100),
  supp_phone   VARCHAR(20),
  supp_email   VARCHAR(100),
  supp_status  VARCHAR(20)   DEFAULT 'active',
  name         NVARCHAR(200) NOT NULL,
  unit         NVARCHAR(50)  NOT NULL,
  price        DECIMAL(12,2) NOT NULL,
  stock_qty    INT           DEFAULT 0,
  min_stock    INT           DEFAULT 10,
  sold_qty     INT           DEFAULT 0,
  is_featured  BIT           DEFAULT 0,
  weight_kg    DECIMAL(8,3),
  description  NVARCHAR(MAX),
  image_url    NVARCHAR(255),
  status       VARCHAR(20)   DEFAULT 'active',
  created_at   DATETIME      DEFAULT GETDATE(),
  updated_at   DATETIME      DEFAULT GETDATE(),
  CONSTRAINT fk_prod_cat     FOREIGN KEY (category_id) REFERENCES nga_categories(category_id),
  CONSTRAINT chk_price       CHECK (price     >= 0),
  CONSTRAINT chk_stock       CHECK (stock_qty >= 0),
  CONSTRAINT chk_minstk      CHECK (min_stock >= 0),
  CONSTRAINT chk_soldqty     CHECK (sold_qty  >= 0),
  CONSTRAINT chk_prod_status CHECK (status      IN ('active','inactive')),
  CONSTRAINT chk_supp_status CHECK (supp_status IN ('active','inactive') OR supp_status IS NULL)
);
GO

-- ============================================================
-- BẢNG 5: tra_stock_imports
-- ============================================================
CREATE TABLE tra_stock_imports (
  id           INT           IDENTITY(1,1) PRIMARY KEY,
  batch_code   VARCHAR(50),
  product_id   INT           NOT NULL,
  user_id      INT           NOT NULL,
  import_date  DATETIME      DEFAULT GETDATE(),
  quantity     INT           NOT NULL,
  import_price DECIMAL(12,2) NOT NULL,
  note         NVARCHAR(200),
  status       VARCHAR(20)   DEFAULT 'done',
  FOREIGN KEY (product_id) REFERENCES tra_products(product_id),
  FOREIGN KEY (user_id)    REFERENCES nga_users(user_id),
  CONSTRAINT chk_imp_qty    CHECK (quantity     > 0),
  CONSTRAINT chk_imp_price  CHECK (import_price >= 0),
  CONSTRAINT chk_imp_status CHECK (status IN ('done','cancelled'))
);
GO

-- ============================================================
-- BẢNG 6: tra_orders
-- ============================================================
CREATE TABLE tra_orders (
  order_id         INT           IDENTITY(1,1) PRIMARY KEY,
  customer_id      INT           NOT NULL,
  user_id          INT           NOT NULL,
  order_date       DATETIME      DEFAULT GETDATE(),
  status           VARCHAR(20)   DEFAULT 'pending',
  shipping_address NVARCHAR(200),
  shipping_fee     DECIMAL(10,2) DEFAULT 0,
  note             NVARCHAR(MAX),
  total_amount     DECIMAL(15,2) DEFAULT 0,
  disc_code        VARCHAR(50),
  disc_type        VARCHAR(10),
  disc_value       DECIMAL(12,2),
  disc_min_order   DECIMAL(12,2),
  discount_amount  DECIMAL(12,2) DEFAULT 0,
  pay_method       NVARCHAR(50),
  pay_status       NVARCHAR(20)  DEFAULT 'pending',
  pay_amount       DECIMAL(15,2) DEFAULT 0,
  pay_txn_id       VARCHAR(100),
  pay_at           DATETIME,
  CONSTRAINT fk_ord_cust    FOREIGN KEY (customer_id) REFERENCES nga_customers(customer_id),
  CONSTRAINT fk_ord_user    FOREIGN KEY (user_id)     REFERENCES nga_users(user_id),
  CONSTRAINT chk_ord_status CHECK (status     IN ('pending','confirmed','shipped','done','cancelled')),
  CONSTRAINT chk_pay_status CHECK (pay_status IN ('pending','paid','failed','refunded') OR pay_status IS NULL),
  CONSTRAINT chk_pay_method CHECK (pay_method IN ('COD','Banking','Momo','VNPay')       OR pay_method IS NULL),
  CONSTRAINT chk_disc_type  CHECK (disc_type  IN ('percent','fixed')                   OR disc_type  IS NULL),
  CONSTRAINT chk_total      CHECK (total_amount    >= 0),
  CONSTRAINT chk_ship_fee   CHECK (shipping_fee    >= 0),
  CONSTRAINT chk_disc_amt   CHECK (discount_amount >= 0)
);
GO

-- ============================================================
-- BẢNG 7: chi_order_items
-- ============================================================
CREATE TABLE chi_order_items (
  order_id       INT,
  product_id     INT,
  quantity       INT           DEFAULT 1,
  unit_price     NUMERIC(38,2) NOT NULL,
  rating         INT,
  review_comment NVARCHAR(MAX),
  is_approved    BIT,
  reviewed_at    DATETIME,
  PRIMARY KEY (order_id, product_id),
  CONSTRAINT fk_item_order   FOREIGN KEY (order_id)   REFERENCES tra_orders(order_id),
  CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES tra_products(product_id),
  CONSTRAINT chk_oi_qty      CHECK (quantity   > 0),
  CONSTRAINT chk_oi_price    CHECK (unit_price >= 0),
  CONSTRAINT chk_oi_rating   CHECK (rating IS NULL OR rating BETWEEN 1 AND 5)
);
GO

-- ============================================================
-- BẢNG 8: chi_cart_items
-- ============================================================
CREATE TABLE chi_cart_items (
  id          INT      IDENTITY(1,1) PRIMARY KEY,
  customer_id INT      NOT NULL,
  product_id  INT      NOT NULL,
  quantity    INT      DEFAULT 1,
  added_at    DATETIME DEFAULT GETDATE(),
  FOREIGN KEY (customer_id) REFERENCES nga_customers(customer_id),
  FOREIGN KEY (product_id)  REFERENCES tra_products(product_id),
  CONSTRAINT chk_cart_qty CHECK (quantity > 0),
  CONSTRAINT uq_cart UNIQUE (customer_id, product_id)
);
GO

-- ============================================================
-- BẢNG 9: chi_order_logs
-- ============================================================
CREATE TABLE chi_order_logs (
  id         INT          IDENTITY(1,1) PRIMARY KEY,
  order_id   INT          NOT NULL,
  old_status VARCHAR(20),
  new_status VARCHAR(20)  NOT NULL,
  changed_by INT,
  note       NVARCHAR(200),
  updated_at DATETIME     DEFAULT GETDATE(),
  FOREIGN KEY (order_id)   REFERENCES tra_orders(order_id),
  FOREIGN KEY (changed_by) REFERENCES nga_users(user_id)
);
GO

-- ============================================================
-- TRIGGER 1: Tự tính total_amount
-- ============================================================
CREATE TRIGGER trg_sync_total_amount
ON chi_order_items
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
  SET NOCOUNT ON;
  UPDATE tra_orders
  SET total_amount = (
    SELECT ISNULL(SUM(quantity * unit_price), 0)
    FROM chi_order_items
    WHERE order_id = tra_orders.order_id
  )
  WHERE order_id IN (
    SELECT DISTINCT order_id FROM inserted
    UNION
    SELECT DISTINCT order_id FROM deleted
  );
END;
GO

-- ============================================================
-- TRIGGER 2: Tự cộng stock_qty khi nhập kho
-- ============================================================
CREATE TRIGGER trg_update_stock_on_import
ON tra_stock_imports
AFTER INSERT
AS
BEGIN
  SET NOCOUNT ON;
  UPDATE tra_products
  SET stock_qty  = stock_qty + i.quantity,
      updated_at = GETDATE()
  FROM tra_products p
  JOIN inserted i ON p.product_id = i.product_id
  WHERE i.status = 'done';
END;
GO

-- ============================================================
-- DML: INSERT DỮ LIỆU MẪU
-- ============================================================

-- 1. nga_users: 4 admin + 2 staff + 10 customer
INSERT INTO nga_users (username, password_hash, full_name, role, phone, email, status, last_login) VALUES
  ('huongtra',   '$2b$12$hash...tra',    N'Nguyễn Hương Trà',   'admin',    '0901000004', 'huongtra@chgiadung.vn',  'active', '2024-07-10 07:50:44'),
  ('linhchi',    '$2b$12$hash...chi',    N'Nguyễn Linh Chi',    'admin',    '0901000005', 'linhchi@chgiadung.vn',   'active', '2024-07-07 16:33:20'),
  ('quynhnga',   '$2b$12$hash...nga',    N'Bùi Thị Quỳnh Nga',  'admin',    '0901000006', 'quynhnga@chgiadung.vn',  'active', '2024-07-06 11:18:55'),
  ('admin123',   '$2b$12$abc...hash',    N'Nguyễn Quản Trị',    'admin',    '0901000001', 'admin@chgiadung.vn',     'active', '2024-07-10 08:15:00'),
  ('staff01',    '$2b$12$def...hash',    N'Trần Nhân Viên',     'staff',    '0901000002', 'staff01@chgiadung.vn',   'active', '2024-07-09 14:22:35'),
  ('staff02',    '$2b$12$ghi...hash',    N'Lê Thu Hà',          'staff',    '0901000003', 'staff02@chgiadung.vn',   'active', '2024-07-08 09:05:12'),
  ('an.nguyen',  '$2b$12$hash...an',     N'Nguyễn Văn An',      'customer', '0901234567', 'an.nguyen@email.vn',     'active', '2024-07-01 08:00:00'),
  ('binh.tran',  '$2b$12$hash...binh',   N'Trần Thị Bình',      'customer', '0912345678', 'binh.tran@gmail.com',    'active', '2024-07-02 09:30:00'),
  ('cuong.le',   '$2b$12$hash...cuong',  N'Lê Văn Cường',       'customer', '0923456789', 'cuong.le@yahoo.com',     'active', '2024-07-03 10:15:00'),
  ('dung.pham',  '$2b$12$hash...dung',   N'Phạm Thị Dung',      'customer', '0934567890', 'dung.pham@outlook.com',  'active', '2024-07-04 11:00:00'),
  ('em.hoang',   '$2b$12$hash...em',     N'Hoàng Văn Em',       'customer', '0945678901', 'em.hoang@gmail.com',     'active', '2024-07-05 13:00:00'),
  ('hoa.vu',     '$2b$12$hash...hoa',    N'Vũ Thị Hoa',         'customer', '0956789012', 'hoa.vu@gmail.com',       'active', '2024-07-06 14:30:00'),
  ('giang.dang', '$2b$12$hash...giang',  N'Đặng Văn Giang',     'customer', '0967890123', 'giang.dang@email.vn',    'active', '2024-07-07 08:45:00'),
  ('huong.bui',  '$2b$12$hash...huongb', N'Bùi Thị Hương',      'customer', '0978901234', 'huong.bui@gmail.com',    'active', '2024-07-08 10:00:00'),
  ('kiet.nm',    '$2b$12$hash...kiet',   N'Nguyễn Minh Kiệt',   'customer', '0989012345', 'kiet.nm@email.vn',       'active', '2024-07-09 07:30:00'),
  ('long.tran',  '$2b$12$hash...long',   N'Trần Văn Long',      'customer', '0990123456', 'long.tran@yahoo.com',    'active', '2024-07-10 09:00:00');
GO

-- 2a. nga_categories – danh mục GỐC (parent_id = NULL) trước
INSERT INTO nga_categories (parent_id, name, description, sort_order, status) VALUES
  (NULL, N'Nhà bếp',       N'Dụng cụ và thiết bị nấu nướng',         1, 'active'),   -- id=1
  (NULL, N'Phòng ngủ',     N'Đồ dùng và trang trí phòng ngủ',        2, 'active'),   -- id=2
  (NULL, N'Phòng khách',   N'Nội thất và phụ kiện phòng khách',       3, 'active'),   -- id=3
  (NULL, N'Vệ sinh',       N'Đồ dùng nhà tắm và vệ sinh',            4, 'active'),   -- id=4
  (NULL, N'Điện gia dụng', N'Thiết bị điện sử dụng trong nhà',       5, 'active'),   -- id=5
  (NULL, N'Làm vườn',      N'Dụng cụ chăm sóc cây cảnh và sân vườn', 6, 'active'),   -- id=6
  (NULL, N'Lưu trữ',       N'Hộp, kệ và giải pháp lưu trữ',          7, 'active'),   -- id=7
  (NULL, N'Làm sạch',      N'Sản phẩm vệ sinh và lau chùi',           8, 'inactive'); -- id=8
GO

-- 2b. nga_categories – danh mục CON (parent_id = 1) sau
INSERT INTO nga_categories (parent_id, name, description, sort_order, status) VALUES
  (1, N'Nồi & Chảo', N'Các loại nồi và chảo nấu nướng', 1, 'active'),  -- id=9
  (1, N'Dao & Thớt',  N'Dụng cụ cắt thái',               2, 'active');  -- id=10
GO

-- 3. tra_products (30 sản phẩm – đủ weight_kg + description)
-- category: 1=Nhà bếp 2=Phòng ngủ 3=Phòng khách 4=Vệ sinh
--           5=Điện gia dụng 6=Làm vườn 7=Lưu trữ 8=Làm sạch
--           9=Nồi & Chảo  10=Dao & Thớt
INSERT INTO tra_products
  (category_id,supp_name,supp_contact,supp_phone,supp_email,supp_status,
   name,unit,price,stock_qty,min_stock,is_featured,sold_qty,weight_kg,description)
VALUES
  (9, N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Nồi inox 3 lít',               N'cái', 285000,120,15,1,0,1.200,N'Nồi inox 304 cao cấp dung tích 3 lít, đáy 3 lớp phân tán nhiệt đều, nắp kính cường lực, tay cầm chịu nhiệt, phù hợp bếp từ và bếp gas.'),
  (9, N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Chảo chống dính 28cm',         N'cái', 420000, 85,10,1,0,0.750,N'Chảo chống dính đường kính 28cm lớp phủ Titanium 5 lớp bền bỉ, không chứa PFOA, tay cầm ergonomic chống trượt, dùng được trên mọi loại bếp.'),
  (10,N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Dao làm bếp Thái 6 inch',      N'cái',  95000,200,20,0,0,0.180,N'Dao bếp lưỡi thép không gỉ 6 inch sắc bén, cán nhựa PP chống trượt, bảo vệ màng chắn ngón tay, phù hợp thái rau, thịt hằng ngày.'),
  (10,N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Thớt gỗ cao su 40x30cm',       N'cái', 135000,150,15,0,0,1.500,N'Thớt gỗ cao su thiên nhiên kích thước 40x30cm, kháng khuẩn tự nhiên, bề mặt phẳng mịn không nứt, dễ vệ sinh, bền theo thời gian.'),
  (9, N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Bát sứ trắng 18cm',            N'cái',  45000,500,50,0,0,0.220,N'Bát sứ trắng đường kính 18cm men sứ cao cấp, an toàn thực phẩm, dùng được trong lò vi sóng và máy rửa chén, thiết kế đơn giản thanh lịch.'),
  (1, N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Bếp ga mini du lịch',          N'cái', 195000, 60,10,0,0,0.650,N'Bếp ga mini du lịch dùng bình gas loại nhỏ, đầu đốt inox chịu nhiệt tốt, van an toàn tự ngắt, nhỏ gọn tiện mang theo cắm trại, picnic.'),
  (1, N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Muỗng gạo inox 30cm',          N'cái',  35000,300,30,0,0,0.080,N'Muỗng gạo inox 304 dài 30cm, lỗ thoát hạt thừa, cán cong tiện múc, an toàn thực phẩm, không rỉ sét, dễ vệ sinh.'),
  (9, N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Nồi áp suất 5 lít',            N'cái', 850000, 30, 5,1,0,4.500,N'Nồi áp suất dung tích 5 lít, nấu nhanh gấp 3 lần thông thường, van an toàn 3 nấc, đáy từ dùng bếp từ, vỏ inox bóng dễ lau chùi.'),
  (2, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Gối bông cotton 50x70cm',      N'cái', 185000, 90,10,0,0,0.600,N'Gối bông cotton 100% tự nhiên kích thước 50x70cm, độ đàn hồi tốt, thông thoáng chống ẩm, vỏ gối dệt mềm mịn, phù hợp mọi tư thế ngủ.'),
  (2, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Ga trải giường 1m6',           N'bộ',  450000, 60, 8,0,0,1.800,N'Ga trải giường cotton 1m6, sợi 200 thread count mịn mát, co giãn 4 góc vừa vặn nệm dày đến 30cm, an toàn cho da nhạy cảm và trẻ nhỏ.'),
  (2, N'Philips VN',   N'Lê C',    '02811111111','philips@vn.com',   'active',N'Đèn ngủ LED cảm ứng',          N'cái', 265000, 75,10,1,0,0.350,N'Đèn ngủ LED cảm ứng chạm tắt-mở, 3 mức độ sáng điều chỉnh, ánh sáng vàng ấm bảo vệ mắt, sạc USB, pin dung lượng lớn dùng suốt đêm.'),
  (2, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Móc treo quần áo 6 móc',       N'cái',  75000,120,15,0,0,0.120,N'Móc treo quần áo 6 móc kim loại bọc nhựa, tải trọng 5kg/móc, gắn tường chắc chắn, thiết kế gọn tránh nhăn quần áo.'),
  (3, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Khung ảnh gỗ 20x30cm',         N'cái',  85000,180,20,0,0,0.280,N'Khung ảnh gỗ tự nhiên 20x30cm, mặt kính trong suốt chống bụi, gắn tường hoặc để bàn, phù hợp trưng bày ảnh gia đình và phong cảnh.'),
  (3, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Thảm trải sàn 160x230cm',      N'cái', 890000, 30, 5,1,0,4.200,N'Thảm trải sàn 160x230cm sợi polypropylene cao cấp, chống trượt mặt dưới, dễ hút bụi vệ sinh, họa tiết hình học hiện đại phù hợp phòng khách.'),
  (4, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Khăn tắm cotton 70x140cm',     N'cái', 125000,240,25,0,0,0.450,N'Khăn tắm cotton 70x140cm mật độ 550gsm thấm hút tốt, mềm mịn không xơ sau giặt nhiều lần, kháng khuẩn tự nhiên, an toàn cho da nhạy cảm.'),
  (4, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Bàn chải vệ sinh bồn cầu',     N'cái',  55000,300,30,0,0,0.180,N'Bàn chải vệ sinh bồn cầu lông nhựa PP cứng tẩy sạch vết bẩn cứng đầu, cán dài 40cm chống khuẩn, đế giữ đứng gọn gàng không tiếp đất.'),
  (5, N'Philips VN',   N'Lê C',    '02811111111','philips@vn.com',   'active',N'Quạt đứng 3 cánh 40W',         N'cái', 750000, 45, 5,0,0,6.500,N'Quạt đứng 3 cánh 40W 5 cấp độ gió, đầu quạt xoay 90 độ tự động, hẹn giờ tắt 1-8 tiếng, điều khiển từ xa, phù hợp phòng rộng đến 30m2.'),
  (5, N'Philips VN',   N'Lê C',    '02811111111','philips@vn.com',   'active',N'Ấm đun nước 1.7L 2200W',       N'cái', 385000, 60, 8,1,0,1.200,N'Ấm đun nước 1.7L công suất 2200W, sôi chỉ trong 5 phút, tự ngắt điện khi sôi, bộ lọc cáu cặn inox tháo lắp dễ, thân thép không gỉ bền đẹp.'),
  (5, N'Panasonic VN', N'Phạm D',  '02822222222','panasonic@vn.com', 'active',N'Máy xay sinh tố 500W',         N'cái', 495000, 40, 5,0,0,2.800,N'Máy xay sinh tố 500W lưỡi dao inox 4 cánh siêu sắc, cốc thủy tinh chịu nhiệt 1.5L, 3 chế độ tốc độ, chức năng tự làm sạch tiện lợi.'),
  (6, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Xẻng trồng cây inox',          N'cái',  65000,160,15,0,0,0.250,N'Xẻng trồng cây inox 304 bền bỉ không gỉ, đầu xẻng chắc chắn dễ đào đất, cán gỗ tự nhiên chống trượt, phù hợp trồng rau sân thượng và ban công.'),
  (6, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Bình tưới cây 5L',             N'cái',  78000,130,15,0,0,0.480,N'Bình tưới cây 5 lít nhựa HDPE an toàn, vòi phun mưa tháo rời được, tay cầm ergonomic cân bằng khi tưới đầy, dùng trong nhà lẫn ngoài vườn.'),
  (7, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Hộp nhựa đựng thực phẩm 1.5L',N'cái',  48000,400,40,0,0,0.090,N'Hộp nhựa đựng thực phẩm 1.5L nắp khóa kín chống thấm, nhựa PP không BPA an toàn sức khỏe, dùng được lò vi sóng và máy rửa chén, bảo quản thức ăn tươi lâu.'),
  (7, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Kệ sách 5 tầng 80x180cm',      N'cái', 650000, 20, 3,0,0,18.000,N'Kệ sách 5 tầng kích thước 80x180cm, khung thép phủ sơn tĩnh điện chịu lực 30kg/tầng, ván gỗ MDF dày 15mm, lắp ráp dễ dàng, phù hợp phòng làm việc và phòng ngủ.'),
  (8, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Chổi lau nhà cotton',          N'cái', 115000,220,25,0,0,0.550,N'Chổi lau nhà bằng sợi cotton tự nhiên siêu thấm nước, đầu chổi tháo giặt được, cán inox dài 120cm điều chỉnh độ cao, làm sạch không để vết.'),
  (8, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Nước lau sàn hương chanh 2L',  N'chai', 95000,180,20,0,0,1.900,N'Nước lau sàn hương chanh 2L diệt khuẩn 99.9%, an toàn bề mặt gỗ, gạch, đá hoa cương, không để lại vết mờ, hương thơm dịu nhẹ kéo dài suốt ngày.'),
  (8, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Nước rửa bát 750ml',           N'chai', 48000,350,40,0,0,0.780,N'Nước rửa bát 750ml chiết xuất chanh tươi, tẩy dầu mỡ mạnh không hại da tay, bọt mịn dễ xả sạch, an toàn cho bát đĩa trẻ em và đồ dùng nhà bếp.'),
  (5, N'Philips VN',   N'Lê C',    '02811111111','philips@vn.com',   'active',N'Quạt trần 5 cánh 60W',         N'cái',1250000, 25, 3,1,0,9.800,N'Quạt trần 5 cánh 60W đường kính cánh 132cm, động cơ DC tiết kiệm điện, 6 cấp tốc độ, điều khiển từ xa kèm hẹn giờ, phù hợp phòng khách lớn đến 50m2.'),
  (10,N'Sunhouse',     N'Nguyễn A','02887654321','info@sunhouse.com', 'active',N'Bộ dao nhà bếp 5 món',         N'bộ',  320000, 50, 8,0,0,0.620,N'Bộ dao nhà bếp 5 món gồm dao thái, dao chặt, dao bào, dao gọt và kéo cắt, lưỡi thép không gỉ sắc bén, cán ABS chống trơn, kèm giá cắm dao gỗ.'),
  (4, N'Lock&Lock VN', N'Trần B',  '02812345678','contact@lock.vn',  'active',N'Ghế nhựa đa năng',             N'cái', 125000,100,15,0,0,1.100,N'Ghế nhựa đa năng PP nguyên sinh tải trọng 120kg, 4 chân bạt chống trơn, xếp chồng tiết kiệm diện tích, dùng trong nhà và ngoài trời, nhiều màu sắc.'),
  (3, N'Philips VN',   N'Lê C',    '02811111111','philips@vn.com',   'active',N'Đèn bàn học LED 10W',          N'cái', 185000, 80,10,1,0,0.420,N'Đèn bàn học LED 10W ánh sáng trắng trung tính 4000K bảo vệ mắt, cần đèn gập 3 khớp điều chỉnh góc linh hoạt, cổng sạc USB phụ, phù hợp học sinh và dân văn phòng.');
GO

-- 4. nga_customers (10 khách – đầy đủ username, dob, gender)
INSERT INTO nga_customers (username,full_name,phone,email,address,dob,gender) VALUES
  ('an.nguyen', N'Nguyễn Văn An',    '0901234567','an.nguyen@email.vn',   N'12 Nguyễn Huệ, Q.1, TP.HCM',              '1990-03-15','Nam'),
  ('binh.tran', N'Trần Thị Bình',    '0912345678','binh.tran@gmail.com',  N'45 Lê Lợi, Q. Hải Châu, Đà Nẵng',         '1985-07-22','Nu'),
  ('cuong.le',  N'Lê Văn Cường',     '0923456789','cuong.le@yahoo.com',   N'78 Trần Phú, Ba Đình, Hà Nội',             '1992-11-08','Nam'),
  ('dung.pham', N'Phạm Thị Dung',    '0934567890','dung.pham@outlook.com',N'23 Nguyễn Trãi, Q.5, TP.HCM',             '1988-05-30','Nu'),
  ('em.hoang',  N'Hoàng Văn Em',     '0945678901','em.hoang@gmail.com',   N'56 Pasteur, Q.3, TP.HCM',                  '1995-01-17','Nam'),
  ('hoa.vu',    N'Vũ Thị Hoa',       '0956789012','hoa.vu@gmail.com',     N'90 CMT8, Q.3, TP.HCM',                     '1993-09-04','Nu'),
  ('giang.dang',N'Đặng Văn Giang',   '0967890123','giang.dang@email.vn',  N'34 Lý Thường Kiệt, Q. Hoàn Kiếm, Hà Nội', '1987-12-25','Nam'),
  ('huong.bui', N'Bùi Thị Hương',    '0978901234','huong.bui@gmail.com',  N'67 Nguyễn Văn Linh, Q.7, TP.HCM',         '1991-06-14','Nu'),
  ('kiet.nm',   N'Nguyễn Minh Kiệt', '0989012345','kiet.nm@email.vn',     N'11 Hai Bà Trưng, Q.1, TP.HCM',             '1996-04-03','Nam'),
  ('long.tran', N'Trần Văn Long',    '0990123456','long.tran@yahoo.com',  N'89 Trần Hưng Đạo, Q.5, TP.HCM',           '1984-10-19','Nam');
GO

-- 5. tra_stock_imports
INSERT INTO tra_stock_imports (batch_code,product_id,user_id,quantity,import_price,note,status) VALUES
  ('IMP-2024-001', 1,1,50,230000,N'Nhập bổ sung tháng 1','done'),
  ('IMP-2024-001', 2,1,30,350000,N'Nhập bổ sung tháng 1','done'),
  ('IMP-2024-001', 8,1,10,700000,N'Nhập bổ sung tháng 1','done'),
  ('IMP-2024-002',17,2,20,620000,N'Nhập quạt tháng 3',   'done'),
  ('IMP-2024-002',18,2,25,300000,N'Nhập ấm điện tháng 3','done');
GO

-- 6. tra_orders
INSERT INTO tra_orders
  (customer_id,user_id,order_date,status,shipping_address,shipping_fee,
   disc_code,disc_type,disc_value,disc_min_order,discount_amount,
   pay_method,pay_status,pay_amount,pay_txn_id,pay_at,note)
VALUES
  (1,1,'2024-01-05','done',    N'12 Nguyễn Huệ, Q.1, TP.HCM',    30000,NULL,      NULL,   NULL,  NULL,    0,'COD',    'paid',  815000,NULL,             '2024-01-05 15:00:00',N'Giao giờ hành chính (8h-17h), gọi trước 30 phút'),
  (2,1,'2024-01-07','done',    N'45 Lê Lợi, Đà Nẵng',            30000,'GIAM10','percent',  10,200000,176500,'Banking','paid', 1629500,'TXN20240107001','2024-01-07 16:00:00',N'Áp dụng mã giảm giá GIAM10, giao trong ngày'),
  (3,1,'2024-01-10','shipped', N'78 Trần Phú, Hà Nội',            25000,NULL,      NULL,   NULL,  NULL,    0,'Momo',  'pending',     0,NULL,             NULL,                N'Khách yêu cầu đóng gói cẩn thận, giao lầu 3 không thang máy'),
  (4,1,'2024-02-12','done',    N'23 Nguyễn Trãi, Q.5, TP.HCM',   30000,'FREESHIP','fixed',30000,150000,30000,'COD',   'paid', 1335000,NULL,             '2024-02-12 14:00:00',N'Áp dụng FREESHIP, giao buổi sáng'),
  (5,1,'2024-02-15','done',    N'56 Pasteur, Q.3, TP.HCM',        30000,NULL,      NULL,   NULL,  NULL,    0,'COD',   'paid', 1400000,NULL,             '2024-02-15 10:00:00',N'Khách thanh toán COD, chuẩn bị tiền lẻ'),
  (6,1,'2024-03-20','done',    N'90 CMT8, Q.3, TP.HCM',           30000,NULL,      NULL,   NULL,  NULL,    0,'Banking','paid',  560000,'TXN20240320001','2024-03-20 11:00:00',N'Giao cuối tuần, liên hệ trước khi giao'),
  (7,1,'2024-04-05','confirmed',N'34 Lý Thường Kiệt, Hà Nội',     35000,NULL,      NULL,   NULL,  NULL,    0,'VNPay', 'pending',     0,NULL,             NULL,                N'Đơn hàng lớn, cần xe tải nhỏ'),
  (8,1,'2024-05-18','done',    N'67 Nguyễn Văn Linh, Q.7',        30000,NULL,      NULL,   NULL,  NULL,    0,'Momo',  'paid',  700000,'MOMO20240518',   '2024-05-18 09:00:00',N'Khách đã thanh toán qua Momo, xác nhận mã giao dịch'),
  (9,1,'2024-06-22','done',    N'11 Hai Bà Trưng, Q.1, TP.HCM',   30000,NULL,      NULL,   NULL,  NULL,    0,'COD',   'paid',  650000,NULL,             '2024-06-22 13:00:00',N'Giao nhanh trong ngày, khách cần gấp'),
  (10,1,'2024-07-01','cancelled',N'89 Trần Hưng Đạo, Q.5, TP.HCM',30000,NULL,     NULL,   NULL,  NULL,    0,NULL,    'pending',     0,NULL,             NULL,                N'Đơn huỷ do khách đổi ý, hoàn tiền nếu đã thanh toán');
GO

-- 7. chi_order_items
INSERT INTO chi_order_items (order_id,product_id,quantity,unit_price) VALUES
  (1,1,2,285000),(1,3,1,95000),(1,22,3,48000),
  (2,18,1,385000),(2,19,1,495000),(2,14,1,890000),(2,16,5,55000),
  (3,9,2,185000),(3,10,1,450000),
  (4,17,1,750000),(4,26,3,95000),(4,15,2,125000),
  (5,8,1,850000),(5,28,1,320000),(5,24,2,115000),
  (6,11,1,265000),(6,12,2,75000),(6,25,2,95000),
  (7,27,1,1250000),(7,23,1,650000),
  (8,2,1,420000),(8,5,10,45000),(8,26,2,95000),
  (9,29,1,125000),(9,30,1,185000),(9,13,3,85000),
  (10,1,1,285000);
GO

-- Cập nhật pay_amount cho đơn shipped/confirmed theo total thực tế
UPDATE tra_orders
SET pay_amount = total_amount + shipping_fee - discount_amount
WHERE order_id IN (3,7);
GO

-- 8. Reviews – đầy đủ cho tất cả items của đơn done
-- Đơn 1
UPDATE chi_order_items SET rating=5,review_comment=N'Sản phẩm rất tốt, chắc chắn, đúng mô tả',   is_approved=1,reviewed_at='2024-01-18 10:00:00' WHERE order_id=1 AND product_id=1;
UPDATE chi_order_items SET rating=4,review_comment=N'Dao sắc, cầm chắc tay, đóng gói kỹ',          is_approved=1,reviewed_at='2024-01-20 10:30:00' WHERE order_id=1 AND product_id=3;
UPDATE chi_order_items SET rating=5,review_comment=N'Hộp nhựa chắc, nắp kín, bảo quản rất tốt',   is_approved=1,reviewed_at='2024-01-21 14:15:00' WHERE order_id=1 AND product_id=22;
-- Đơn 2
UPDATE chi_order_items SET rating=4,review_comment=N'Dùng ổn, chống dính tốt',                      is_approved=1,reviewed_at='2024-01-22 09:00:00' WHERE order_id=2 AND product_id=18;
UPDATE chi_order_items SET rating=5,review_comment=N'Máy xay mạnh, chạy êm, dễ vệ sinh',           is_approved=1,reviewed_at='2024-01-25 09:00:00' WHERE order_id=2 AND product_id=19;
UPDATE chi_order_items SET rating=4,review_comment=N'Thảm đẹp, dày dặn, không bị xô lệch',         is_approved=1,reviewed_at='2024-01-26 11:45:00' WHERE order_id=2 AND product_id=14;
UPDATE chi_order_items SET rating=3,review_comment=N'Bàn chải dùng được, cán hơi ngắn',             is_approved=0,reviewed_at='2024-01-27 16:20:00' WHERE order_id=2 AND product_id=16;
-- Đơn 4
UPDATE chi_order_items SET rating=3,review_comment=N'Tạm được, tiếng ồn hơi lớn',                  is_approved=1,reviewed_at='2024-02-28 08:00:00' WHERE order_id=4 AND product_id=17;
UPDATE chi_order_items SET rating=4,review_comment=N'Nước lau sàn thơm, lau sạch, không nhờn',     is_approved=1,reviewed_at='2024-02-28 08:30:00' WHERE order_id=4 AND product_id=26;
UPDATE chi_order_items SET rating=5,review_comment=N'Khăn tắm mềm mịn, thấm nước tốt',             is_approved=1,reviewed_at='2024-02-28 09:10:00' WHERE order_id=4 AND product_id=15;
-- Đơn 5
UPDATE chi_order_items SET rating=4,review_comment=N'Hài lòng, nồi áp suất nấu rất nhanh',         is_approved=0,reviewed_at='2024-03-05 12:00:00' WHERE order_id=5 AND product_id=8;
UPDATE chi_order_items SET rating=4,review_comment=N'Bộ dao đầy đủ, sắc bén, kèm giá cắm tiện',   is_approved=1,reviewed_at='2024-03-05 13:00:00' WHERE order_id=5 AND product_id=28;
UPDATE chi_order_items SET rating=3,review_comment=N'Chổi lau ổn, sợi cotton thấm nhưng hơi cứng', is_approved=0,reviewed_at='2024-03-06 10:45:00' WHERE order_id=5 AND product_id=24;
-- Đơn 6
UPDATE chi_order_items SET rating=5,review_comment=N'Đèn ngủ cảm ứng nhạy, ánh sáng dịu, pin trâu',is_approved=1,reviewed_at='2024-04-05 20:00:00' WHERE order_id=6 AND product_id=11;
UPDATE chi_order_items SET rating=4,review_comment=N'Móc treo chắc, chịu được nhiều quần áo',       is_approved=1,reviewed_at='2024-04-06 08:00:00' WHERE order_id=6 AND product_id=12;
UPDATE chi_order_items SET rating=4,review_comment=N'Nước rửa bát thơm chanh, tẩy dầu mỡ tốt',    is_approved=1,reviewed_at='2024-04-07 11:30:00' WHERE order_id=6 AND product_id=25;
-- Đơn 8
UPDATE chi_order_items SET rating=5,review_comment=N'Chảo chống dính xuất sắc, dầu mỡ không bám',  is_approved=1,reviewed_at='2024-06-01 09:30:00' WHERE order_id=8 AND product_id=2;
UPDATE chi_order_items SET rating=4,review_comment=N'Bát sứ trắng đẹp, dày dặn, không vỡ khi rơi', is_approved=1,reviewed_at='2024-06-02 14:00:00' WHERE order_id=8 AND product_id=5;
UPDATE chi_order_items SET rating=3,review_comment=N'Nước lau sàn ổn, mùi hơi gắt khi mới dùng',   is_approved=0,reviewed_at='2024-06-03 16:45:00' WHERE order_id=8 AND product_id=26;
-- Đơn 9
UPDATE chi_order_items SET rating=4,review_comment=N'Ghế nhựa chắc, nhẹ, xếp được, rất tiện',      is_approved=1,reviewed_at='2024-07-10 10:00:00' WHERE order_id=9 AND product_id=29;
UPDATE chi_order_items SET rating=5,review_comment=N'Đèn bàn học sáng, gập nhiều góc, có cổng USB', is_approved=1,reviewed_at='2024-07-11 08:30:00' WHERE order_id=9 AND product_id=30;
UPDATE chi_order_items SET rating=4,review_comment=N'Khung ảnh gỗ đẹp, kính trong, dễ gắn tường',  is_approved=1,reviewed_at='2024-07-12 15:20:00' WHERE order_id=9 AND product_id=13;
GO

-- 9. chi_cart_items
INSERT INTO chi_cart_items (customer_id,product_id,quantity) VALUES
  (1,1,2),(1,3,1),(2,5,4),(3,10,1),(4,15,2),(5,20,1);
GO

-- 10. chi_order_logs (đầy đủ tất cả đơn, kèm note)
INSERT INTO chi_order_logs (order_id,old_status,new_status,changed_by,note) VALUES
  (1,'pending',   'confirmed',1,N'Xác nhận đơn hàng'),
  (1,'confirmed', 'shipped',  1,N'Đã giao cho đơn vị vận chuyển'),
  (1,'shipped',   'done',     1,N'Khách nhận hàng thành công'),
  (2,'pending',   'done',     2,N'Xử lý nhanh, giao trong ngày'),
  (3,'pending',   'confirmed',1,N'Xác nhận đơn hàng'),
  (3,'confirmed', 'shipped',  2,N'Đã giao cho đơn vị vận chuyển'),
  (4,'pending',   'confirmed',1,N'Xác nhận đơn hàng'),
  (4,'confirmed', 'done',     1,N'Khách nhận hàng thành công'),
  (5,'pending',   'confirmed',1,N'Xác nhận đơn hàng'),
  (5,'confirmed', 'done',     2,N'Khách nhận hàng thành công'),
  (6,'pending',   'confirmed',1,N'Xác nhận đơn hàng'),
  (6,'confirmed', 'done',     1,N'Khách nhận hàng thành công'),
  (7,'pending',   'confirmed',1,N'Xác nhận đơn hàng'),
  (8,'pending',   'confirmed',2,N'Xác nhận đơn hàng'),
  (8,'confirmed', 'done',     2,N'Khách nhận hàng thành công'),
  (9,'pending',   'confirmed',1,N'Xác nhận đơn hàng'),
  (9,'confirmed', 'done',     1,N'Khách nhận hàng thành công'),
  (10,'pending',  'cancelled',1,N'Khách huỷ đơn trước khi giao');
GO

-- ============================================================
-- KIỂM TRA NULL SAU KHI INSERT
-- ============================================================
SELECT 'nga_users       - last_login NULL'                   AS kiem_tra, COUNT(*) AS so_dong FROM nga_users       WHERE last_login  IS NULL
UNION ALL
SELECT 'nga_customers   - username NULL',                     COUNT(*) FROM nga_customers   WHERE username     IS NULL
UNION ALL
SELECT 'nga_customers   - dob NULL',                          COUNT(*) FROM nga_customers   WHERE dob          IS NULL
UNION ALL
SELECT 'nga_customers   - gender NULL',                       COUNT(*) FROM nga_customers   WHERE gender       IS NULL
UNION ALL
SELECT 'tra_products    - weight_kg NULL',                    COUNT(*) FROM tra_products    WHERE weight_kg    IS NULL
UNION ALL
SELECT 'tra_products    - description NULL',                  COUNT(*) FROM tra_products    WHERE description  IS NULL
UNION ALL
SELECT 'tra_orders      - note NULL',                         COUNT(*) FROM tra_orders      WHERE note         IS NULL
UNION ALL
SELECT 'tra_orders      - pay_amount = 0 (done/paid)',        COUNT(*) FROM tra_orders      WHERE status='done' AND pay_status='paid' AND pay_amount = 0
UNION ALL
SELECT 'chi_order_items - rating NULL (don done)',            COUNT(*)
  FROM chi_order_items oi JOIN tra_orders o ON oi.order_id=o.order_id
  WHERE o.status='done' AND oi.rating IS NULL
UNION ALL
SELECT 'chi_order_logs  - note NULL',                         COUNT(*) FROM chi_order_logs  WHERE note         IS NULL;
GO

-- ============================================================
-- DASHBOARD TỔNG HỢP
-- ============================================================
SELECT
  (SELECT COUNT(*)  FROM tra_orders)                                             AS tong_don_hang,
  (SELECT COUNT(*)  FROM tra_orders   WHERE status='done')                       AS don_hoan_thanh,
  (SELECT ISNULL(SUM(total_amount+shipping_fee-discount_amount),0)
   FROM tra_orders WHERE status='done')                                          AS tong_doanh_thu,
  (SELECT COUNT(*)  FROM nga_customers)                                          AS tong_khach_hang,
  (SELECT COUNT(*)  FROM tra_products WHERE stock_qty < min_stock)               AS sp_sap_het_hang,
  (SELECT COUNT(*)  FROM chi_order_items WHERE is_approved=0)                    AS danh_gia_cho_duyet,
  (SELECT COUNT(DISTINCT disc_code) FROM tra_orders WHERE disc_code IS NOT NULL) AS voucher_da_dung;
GO

-- Kiểm tra total_amount khớp chi tiết
SELECT o.order_id, o.total_amount,
       SUM(oi.quantity*oi.unit_price) AS recalculated,
       CASE WHEN o.total_amount=SUM(oi.quantity*oi.unit_price) THEN N'OK' ELSE N'LECH' END AS kiem_tra
FROM tra_orders o
JOIN chi_order_items oi ON o.order_id=oi.order_id
GROUP BY o.order_id, o.total_amount;
GO
-- ============================================================
-- DEMO QUERIES – 60 CHỨC NĂNG
-- ============================================================

-- F01: Đăng nhập
SELECT user_id, username, full_name, role
FROM nga_users WHERE username = 'admin123' AND status = 'active';

-- F02: Danh sách nhân viên
SELECT user_id, username, full_name, role, status, last_login
FROM nga_users WHERE role IN ('admin','staff') ORDER BY role, full_name;

-- F03: Khoá tài khoản
-- UPDATE nga_users SET status = 'inactive' WHERE user_id = 2;

-- F04: Thêm nhân viên
-- INSERT INTO nga_users (username, password_hash, full_name, role) VALUES ('nv03','hash...', N'Họ Tên', 'staff');

-- F05: Sửa thông tin nhân viên
-- UPDATE nga_users SET full_name = N'Tên Mới', phone = '0909...' WHERE user_id = 2;

-- F06: Cập nhật last_login
-- UPDATE nga_users SET last_login = GETDATE() WHERE username = 'admin123';

-- F07: Phân quyền
-- UPDATE nga_users SET role = 'admin' WHERE user_id = 2;

-- F08: Tìm nhân viên theo phone/email
SELECT * FROM nga_users WHERE phone = '0901000001' OR email = 'admin@chgiadung.vn';

-- F09: Đăng ký khách hàng
-- INSERT INTO nga_customers (full_name, phone, email, address) VALUES (N'Khách Mới', '0911999888', 'new@mail.vn', N'HN');

-- F10: Xem thông tin KH
SELECT * FROM nga_customers WHERE customer_id = 1;

-- F11: Sửa thông tin KH
-- UPDATE nga_customers SET address = N'Địa chỉ mới' WHERE customer_id = 1;

-- F12: Tìm kiếm KH theo tên/phone/email
SELECT * FROM nga_customers
WHERE full_name LIKE N'%Nguyễn%' OR phone LIKE '090%' OR email LIKE '%gmail%';

-- F13: Lịch sử mua hàng của KH
SELECT order_id, order_date, status, total_amount, shipping_fee,
       total_amount + shipping_fee - discount_amount AS tong_phai_tra
FROM tra_orders WHERE customer_id = 1 ORDER BY order_date DESC;

-- F16: Thêm danh mục
-- INSERT INTO nga_categories (name, description, sort_order) VALUES (N'Danh mục mới', N'Mô tả', 9);

-- F17: Sửa danh mục
-- UPDATE nga_categories SET name = N'Tên mới' WHERE category_id = 1;

-- F18: Ẩn/hiện danh mục
-- UPDATE nga_categories SET status = 'inactive' WHERE category_id = 8;

-- F19: Danh mục dạng cây cha–con
SELECT c.category_id,
       ISNULL(p.name, N'(Gốc)') AS danh_muc_cha,
       c.name AS danh_muc_con,
       c.sort_order, c.status
FROM nga_categories c
LEFT JOIN nga_categories p ON c.parent_id = p.category_id
ORDER BY ISNULL(c.parent_id, c.category_id), c.sort_order;

-- F20: Sắp xếp thứ tự danh mục
-- UPDATE nga_categories SET sort_order = 3 WHERE category_id = 5;

-- F21: Thêm sản phẩm
-- INSERT INTO tra_products (category_id, supp_name, name, unit, price, stock_qty, min_stock) VALUES (...);

-- F22: Sửa sản phẩm
-- UPDATE tra_products SET price = 300000, updated_at = GETDATE() WHERE product_id = 1;

-- F23: Ẩn/hiện sản phẩm
-- UPDATE tra_products SET status = 'inactive' WHERE product_id = 1;

-- F24: Danh sách sản phẩm đang bán
SELECT p.product_id, p.name, c.name AS danh_muc,
       p.supp_name AS nha_cung_cap, p.price, p.stock_qty, p.is_featured
FROM tra_products p
JOIN nga_categories c ON p.category_id = c.category_id
WHERE p.status = 'active'
ORDER BY p.is_featured DESC, c.sort_order, p.name;

-- F25: Tìm kiếm sản phẩm theo tên/danh mục
SELECT p.product_id, p.name, c.name AS danh_muc, p.price, p.stock_qty
FROM tra_products p
JOIN nga_categories c ON p.category_id = c.category_id
WHERE (p.name LIKE N'%nồi%' OR c.name LIKE N'%bếp%') AND p.status = 'active';

-- F26: Sản phẩm nổi bật
SELECT product_id, name, price, stock_qty
FROM tra_products WHERE is_featured = 1 AND status = 'active';

-- F27: Cảnh báo tồn kho thấp
SELECT product_id, name, stock_qty, min_stock,
       (min_stock - stock_qty) AS can_nhap_them
FROM tra_products WHERE status = 'active' AND stock_qty < min_stock
ORDER BY can_nhap_them DESC;

-- F28: Sản phẩm theo danh mục
SELECT p.product_id, p.name, p.price, p.stock_qty
FROM tra_products p WHERE p.category_id = 1 AND p.status = 'active';

-- F29: Top sản phẩm bán chạy
SELECT TOP 10
       p.product_id, p.name, c.name AS danh_muc,
       SUM(oi.quantity)              AS tong_ban,
       SUM(oi.quantity * oi.unit_price) AS doanh_thu
FROM chi_order_items oi
JOIN tra_orders     o  ON oi.order_id   = o.order_id
JOIN tra_products   p  ON oi.product_id = p.product_id
JOIN nga_categories c  ON p.category_id = c.category_id
WHERE o.status = 'done'
GROUP BY p.product_id, p.name, c.name
ORDER BY tong_ban DESC;

-- F30: Danh sách nhà cung cấp (distinct từ tra_products)
SELECT DISTINCT supp_name, supp_contact, supp_phone, supp_email, supp_status
FROM tra_products WHERE supp_name IS NOT NULL ORDER BY supp_name;

-- F31: Xem sản phẩm theo NCC
SELECT p.product_id, p.name, p.price, p.stock_qty
FROM tra_products p WHERE p.supp_name = N'Sunhouse' AND p.status = 'active';

-- F32: Thông tin liên hệ NCC
SELECT DISTINCT supp_name, supp_contact, supp_phone, supp_email
FROM tra_products WHERE supp_name = N'Philips VN';

-- F33: Khoá NCC
-- UPDATE tra_products SET supp_status = 'inactive' WHERE supp_name = N'Sunhouse';

-- F34: Tạo phiếu nhập kho
-- INSERT INTO tra_stock_imports (batch_code, product_id, user_id, quantity, import_price) VALUES ('IMP-2024-003', 1, 1, 100, 230000);

-- F35: Lịch sử nhập kho
SELECT si.batch_code, p.supp_name AS nha_cung_cap,
       p.name AS san_pham, si.quantity, si.import_price,
       si.quantity * si.import_price AS thanh_tien,
       u.full_name AS nguoi_lap, si.import_date
FROM tra_stock_imports si
JOIN tra_products p ON si.product_id = p.product_id
JOIN nga_users    u ON si.user_id    = u.user_id
ORDER BY si.import_date DESC;

-- F36: Kiểm tra stock_qty sau nhập (trigger tự động)
SELECT product_id, name, stock_qty FROM tra_products WHERE product_id IN (1,2,8,17,18);

-- F37: Nhập kho theo NCC
SELECT p.supp_name AS ncc,
       COUNT(*)                         AS so_lan_nhap,
       SUM(si.quantity)                 AS tong_sl,
       SUM(si.quantity * si.import_price) AS tong_chi_phi
FROM tra_stock_imports si
JOIN tra_products p ON si.product_id = p.product_id
WHERE si.status = 'done'
GROUP BY p.supp_name ORDER BY tong_chi_phi DESC;

-- F38: Thêm SP vào giỏ
-- INSERT INTO chi_cart_items (customer_id, product_id, quantity) VALUES (1, 5, 2);
-- Nếu đã có SP → cập nhật số lượng:
-- UPDATE chi_cart_items SET quantity = quantity + 1 WHERE customer_id = 1 AND product_id = 5;

-- F39: Xem giỏ hàng của KH
SELECT ci.id, p.name, ci.quantity, p.price,
       ci.quantity * p.price AS thanh_tien, ci.added_at
FROM chi_cart_items ci
JOIN tra_products p ON ci.product_id = p.product_id
WHERE ci.customer_id = 1;

-- F40: Sửa số lượng trong giỏ
-- UPDATE chi_cart_items SET quantity = 3 WHERE customer_id = 1 AND product_id = 1;

-- F41: Xoá SP khỏi giỏ
-- DELETE FROM chi_cart_items WHERE customer_id = 1 AND product_id = 3;

-- F42: Tính tổng tiền giỏ hàng
SELECT SUM(ci.quantity * p.price) AS tong_gio_hang
FROM chi_cart_items ci
JOIN tra_products p ON ci.product_id = p.product_id
WHERE ci.customer_id = 1;

-- F43: Chuyển giỏ → đơn hàng
SELECT ci.customer_id, ci.product_id, ci.quantity, p.price AS unit_price
FROM chi_cart_items ci
JOIN tra_products p ON ci.product_id = p.product_id
WHERE ci.customer_id = 1 AND p.status = 'active';

-- F44: Tạo đơn hàng mới
BEGIN TRANSACTION;
DECLARE @oid INT;
INSERT INTO tra_orders (customer_id, user_id, status, shipping_address, shipping_fee, pay_method, pay_status)
VALUES (1, 1, 'pending', N'12 Nguyễn Huệ, Q.1', 30000, 'COD', 'pending');
SET @oid = SCOPE_IDENTITY();
INSERT INTO chi_order_items (order_id, product_id, quantity, unit_price) VALUES
  (@oid, 4, 1, 135000), (@oid, 6, 1, 195000);
UPDATE tra_orders SET status = 'confirmed' WHERE order_id = @oid;
COMMIT;

-- F45: Chi tiết đơn hàng
SELECT o.order_id, c.full_name, c.phone, o.order_date, o.status,
       o.shipping_address, o.disc_code, o.discount_amount, o.shipping_fee,
       p.name AS san_pham, oi.quantity, oi.unit_price,
       oi.quantity * oi.unit_price AS thanh_tien,
       o.total_amount + o.shipping_fee - o.discount_amount AS tong_phai_tra
FROM tra_orders o
JOIN nga_customers   c  ON o.customer_id  = c.customer_id
JOIN chi_order_items oi ON o.order_id     = oi.order_id
JOIN tra_products    p  ON oi.product_id  = p.product_id
WHERE o.order_id = 2;

-- F46: Cập nhật trạng thái đơn
-- UPDATE tra_orders SET status = 'shipped' WHERE order_id = 3;

-- F47: Huỷ đơn
-- UPDATE tra_orders SET status = 'cancelled' WHERE order_id = 3 AND status IN ('pending','confirmed');

-- F48: Danh sách đơn hàng + lọc theo trạng thái / khoảng ngày
SELECT o.order_id, c.full_name, o.order_date, o.status,
       o.total_amount, o.pay_method, o.pay_status
FROM tra_orders o
JOIN nga_customers c ON o.customer_id = c.customer_id
WHERE o.status = 'done'
  AND o.order_date BETWEEN '2024-01-01' AND '2024-06-30'
ORDER BY o.order_date DESC;

-- F49: Ghi chú đơn
-- UPDATE tra_orders SET note = N'Giao giờ hành chính' WHERE order_id = 7;

-- F50: Đổi địa chỉ giao hàng
-- UPDATE tra_orders SET shipping_address = N'Địa chỉ mới' WHERE order_id = 7 AND status = 'pending';

-- F51: Lịch sử thay đổi trạng thái đơn
SELECT ol.order_id, ol.old_status, ol.new_status,
       u.full_name AS nhan_vien, ol.note, ol.updated_at
FROM chi_order_logs ol
LEFT JOIN nga_users u ON ol.changed_by = u.user_id
WHERE ol.order_id = 1 ORDER BY ol.updated_at;

-- F52: Xem các voucher đang hoạt động
SELECT DISTINCT disc_code, disc_type, disc_value, disc_min_order
FROM tra_orders WHERE disc_code IS NOT NULL ORDER BY disc_code;

-- F53: Áp voucher vào đơn
-- UPDATE tra_orders SET disc_code='GIAM10', disc_type='percent', disc_value=10,
--   discount_amount = total_amount * 0.10 WHERE order_id = 7;

-- F54: Kiểm tra voucher đã dùng bao nhiêu lần
SELECT disc_code, COUNT(*) AS so_don_dung,
       SUM(discount_amount) AS tong_tien_giam
FROM tra_orders WHERE disc_code IS NOT NULL
GROUP BY disc_code ORDER BY so_don_dung DESC;

-- F55: Thống kê hiệu quả voucher
SELECT disc_code, disc_type, disc_value,
       COUNT(*) AS so_don,
       SUM(discount_amount) AS tong_giam,
       AVG(total_amount)    AS don_trung_binh
FROM tra_orders WHERE disc_code IS NOT NULL
GROUP BY disc_code, disc_type, disc_value;

-- F56: Ghi nhận thanh toán
-- UPDATE tra_orders SET pay_method='Banking', pay_status='paid', pay_amount=815000,
--   pay_txn_id='TXN001', pay_at=GETDATE() WHERE order_id = 3;

-- F57: Lịch sử thanh toán
SELECT o.order_id, c.full_name, o.pay_method, o.pay_status,
       o.pay_amount, o.pay_txn_id, o.pay_at
FROM tra_orders o
JOIN nga_customers c ON o.customer_id = c.customer_id
WHERE o.pay_at IS NOT NULL ORDER BY o.pay_at DESC;

-- F58: Đối soát (so sánh pay_amount vs tổng thực tế phải thu)
SELECT o.order_id, c.full_name, o.pay_method,
       o.total_amount + o.shipping_fee - o.discount_amount AS phai_thu,
       o.pay_amount AS da_thu,
       CASE WHEN o.pay_amount = o.total_amount + o.shipping_fee - o.discount_amount
            THEN N'Khớp' ELSE N'Lệch' END AS doi_soat
FROM tra_orders o
JOIN nga_customers c ON o.customer_id = c.customer_id
WHERE o.pay_status = 'paid';

-- F59: Thêm/xem đánh giá sản phẩm

SELECT p.name AS san_pham, oi.rating, oi.review_comment,
       c.full_name AS khach_hang, oi.reviewed_at
FROM chi_order_items oi
JOIN tra_orders     o  ON oi.order_id   = o.order_id
JOIN tra_products   p  ON oi.product_id = p.product_id
JOIN nga_customers  c  ON o.customer_id = c.customer_id
WHERE oi.rating IS NOT NULL ORDER BY oi.reviewed_at DESC;

-- F60: Duyệt / ẩn đánh giá
SELECT oi.order_id, oi.product_id, p.name, c.full_name,
       oi.rating, oi.review_comment,
       CASE oi.is_approved WHEN 1 THEN N'Đã duyệt'
                           WHEN 0 THEN N'Chờ duyệt'
                           ELSE        N'Chưa đánh giá' END AS trang_thai
FROM chi_order_items oi
JOIN tra_orders    o  ON oi.order_id   = o.order_id
JOIN tra_products  p  ON oi.product_id = p.product_id
JOIN nga_customers c  ON o.customer_id = c.customer_id
WHERE oi.rating IS NOT NULL ORDER BY oi.is_approved, oi.reviewed_at DESC;
UPDATE chi_order_items SET is_approved = 1 WHERE order_id=5 AND product_id=8;