Trải nghiệm AI ngay trong các ứng dụng bạn yêu thích … Dùng Gemini để tạo bản nháp và tinh chỉnh nội dung, đồng thời sử dụng Gemini Pro để khai thác AI thế hệ mới của Google với giá 489.000 ₫ 0 ₫ cho 1 tháng
﻿DROP DATABASE CHGiaDun1g
CREATE DATABASE CHGiaDung1;
USE CHGiaDung1;
USE master;
GO

ALTER DATABASE CHGiaDung1
SET SINGLE_USER
WITH ROLLBACK IMMEDIATE;
GO

DROP DATABASE CHGiaDung;

-- ─────────────────────────────────────────────────────────────
-- 1. BẢNG: categories
-- ─────────────────────────────────────────────────────────────
CREATE TABLE categories (
  category_id INT IDENTITY(1,1) PRIMARY KEY,
  name NVARCHAR(100) NOT NULL,
  description NVARCHAR(1000),
  status VARCHAR(20) DEFAULT 'active',
  created_at DATETIME DEFAULT GETDATE(),
  CONSTRAINT chk_cat_status 
    CHECK (status IN ('active', 'inactive'))
);
---Bảng nhà cung cấp:
CREATE TABLE suppliers (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100),
    contact NVARCHAR(100)
);
-- ─────────────────────────────────────────────────────────────
-- 2. BẢNG: products
-- ─────────────────────────────────────────────────────────────
CREATE TABLE products (
  product_id INT IDENTITY(1,1) PRIMARY KEY,
  category_id INT NOT NULL,
  name NVARCHAR(200) NOT NULL,
  unit NVARCHAR(50) NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  stock_qty INT DEFAULT 0,
  min_stock INT DEFAULT 10,
  description NVARCHAR(MAX),
  image_url NVARCHAR(255),
  status VARCHAR(20) DEFAULT 'active',
  created_at DATETIME DEFAULT GETDATE(),
  updated_at DATETIME DEFAULT GETDATE(),
   supplier_id INT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
  CONSTRAINT fk_prod_cat 
    FOREIGN KEY (category_id)
    REFERENCES categories(category_id),
  CONSTRAINT chk_price CHECK (price >= 0),
  CONSTRAINT chk_stock CHECK (stock_qty >= 0),
  CONSTRAINT chk_minstk CHECK (min_stock >= 0),
);

-- ─────────────────────────────────────────────────────────────
-- 3. BẢNG: customers
-- ─────────────────────────────────────────────────────────────
CREATE TABLE customers (
  customer_id INT IDENTITY(1,1) PRIMARY KEY,
   username unique,
  full_name NVARCHAR(150) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(150),
  address NVARCHAR(200),
  FOREIGN KEY (username) REFERENCES users(username),
  created_at DATETIME DEFAULT GETDATE(),
  CONSTRAINT uq_phone UNIQUE (phone),
  CONSTRAINT uq_email UNIQUE (email),
  CONSTRAINT chk_phone 
    CHECK (phone IS NULL OR phone LIKE '[0-9]%'),
  CONSTRAINT chk_email 
    CHECK (email IS NULL OR email LIKE '%@%')
);

-- ─────────────────────────────────────────────────────────────
-- 4. BẢNG: users
-- ─────────────────────────────────────────────────────────────
CREATE TABLE users (
  user_id INT IDENTITY(1,1) PRIMARY KEY,
  username VARCHAR(60) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  full_name NVARCHAR(150),
  email NVARCHAR(100) UNIQUE,
  role NVARCHAR(20),
  status VARCHAR(20) DEFAULT 'active',
  created_at DATETIME DEFAULT GETDATE(),
  CONSTRAINT chk_user_status 
    CHECK (status IN ('active', 'inactive'))
);
-- ─────────────────────────────────────────────────────────────
-- 5. BẢNG: orders
-- ─────────────────────────────────────────────────────────────
CREATE TABLE orders (
  order_id INT IDENTITY(1,1) PRIMARY KEY,
  customer_id INT NOT NULL,
  user_id INT NOT NULL,
  order_date DATETIME DEFAULT GETDATE(),
  status VARCHAR(20) DEFAULT 'pending', 
  total_amount DECIMAL(15,2) DEFAULT 0,
  note NVARCHAR(MAX),
  -- FOREIGN KEY
  CONSTRAINT fk_ord_cust 
    FOREIGN KEY (customer_id)
    REFERENCES customers(customer_id),
  CONSTRAINT fk_ord_user 
    FOREIGN KEY (user_id)
    REFERENCES users(user_id),
  -- CHECK
  CONSTRAINT chk_status 
    CHECK (status IN ('pending','confirmed','shipped','done','cancelled')),
  CONSTRAINT chk_total 
    CHECK (total_amount >= 0)
);
-- ─────────────────────────────────────────────────────────────
-- 6. BẢNG: order_items
-- ─────────────────────────────────────────────────────────────
CREATE TABLE order_items (
  order_id INT,
  product_id INT,
  quantity INT DEFAULT 1,
  unit_price DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (order_id, product_id),
  -- FOREIGN KEY
  CONSTRAINT fk_item_order 
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
  CONSTRAINT fk_item_product 
    FOREIGN KEY (product_id) REFERENCES products(product_id),
  -- CHECK
  CONSTRAINT chk_quantity 
    CHECK (quantity > 0),
  CONSTRAINT chk_unit_price 
    CHECK (unit_price >= 0)
);
---trạng thái đơn hàng
CREATE TABLE order_logs (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    status NVARCHAR(20),
    updated_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
--đổi trả hàng
CREATE TABLE returns (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    reason NVARCHAR(MAX),
    status NVARCHAR(20),
    refund_amount DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
---giỏ hàng và chi tiết giỏ hàng 
CREATE TABLE carts (
    id INT IDENTITY(1,1) PRIMARY KEY,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE cart_items (
    id INT IDENTITY(1,1) PRIMARY KEY,
    cart_id INT,
    product_id INT,
    quantity INT,
    FOREIGN KEY (cart_id) REFERENCES carts(id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
--thanh toán
CREATE TABLE payments (
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    method NVARCHAR(50),
    status NVARCHAR(20),
    paid_at DATETIME,
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
--KHUYẾN MÃI
CREATE TABLE promotions (
    id INT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100),
    discount_percent INT,
    start_date DATE,
    end_date DATE
);

CREATE TABLE customer_promotions (
    customer_id INT,
    promotion_id INT,
    PRIMARY KEY (customer_id, promotion_id),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);

CREATE TABLE product_promotions (
    product_id INT,
    promotion_id INT,
    PRIMARY KEY (product_id, promotion_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id)
);
--content
CREATE TABLE contents (
    id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(255),
    content NVARCHAR(MAX),
    type NVARCHAR(20), -- blog / news
    created_at DATETIME DEFAULT GETDATE()
);
--đánh  giá sản phẩm:
CREATE TABLE reviews (
    id INT IDENTITY(1,1) PRIMARY KEY,
    product_id INT,
    customer_id INT,
    rating INT,
    comment NVARCHAR(MAX),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
-- ─────────────────────────────────────────────────────────────
-- 10. INSERT categories
-- ─────────────────────────────────────────────────────────────
INSERT INTO categories (name, description, status) VALUES
  (N'Nhà bếp',       N'Dụng cụ và thiết bị nấu nướng', 'active'),
  (N'Phòng ngủ',     N'Đồ dùng và trang trí phòng ngủ', 'active'),
  (N'Phòng khách',   N'Nội thất và phụ kiện phòng khách', 'active'),
  (N'Ve sinh',       N'Đồ dùng nhà tắm và vệ sinh', 'active'),
  (N'Điện gia dụng', N'Thiết bị điện sử dụng trong nhà', 'active'),
  (N'Làm vườn',      N'Dụng cụ chăm sóc cây cảnh và sân vườn', 'active'),
  (N'Lưu trữ',       N'Hộp, kệ và giải pháp lưu trữ', 'active'),
  (N'Làm sạch',      N'Sản phẩm vệ sinh và lau chùi', 'inactive');
-- ─────────────────────────────────────────────────────────────
-- 11.INSERT users 
-- ─────────────────────────────────────────────────────────────
INSERT INTO users (username, password_hash, full_name, status)
VALUES ('admin123', '12345a.', N'Nguyễn Quản Trị', 'active');
-- ─────────────────────────────────────────────────────────────
-- 12. DML: INSERT products (20 mẫu – script Faker bổ sung 280)
-- ─────────────────────────────────────────────────────────────
INSERT INTO products (category_id, name, unit, price, stock_qty, min_stock) VALUES
  (1, N'Nồi inox 3 lít',              N'cái', 285000, 120, 15),
  (1, N'Chảo chống dính 28cm',        N'cái', 420000,  85, 10),
  (1, N'Dao làm bếp Thái 6 inch',     N'cái',  95000, 200, 20),
  (1, N'Thớt gỗ cao su 40x30cm',      N'cái', 135000, 150, 15),
  (1, N'Bát sứ trắng 18cm',           N'cái',  45000, 500, 50),
  (1, N'Bếp ga mini du lịch',         N'cái', 195000,  60, 10),
  (1, N'Muỗng gạo inox 30cm',         N'cái',  35000, 300, 30),
  (1, N'Nồi áp suất 5 lít',           N'cái', 850000,  30,  5),

  (2, N'Gối bông cotton 50x70cm',     N'cái', 185000,  90, 10),
  (2, N'Ga trải giường 1m6',          N'bộ',  450000,  60,  8),
  (2, N'Đèn ngủ LED cảm ứng',         N'cái', 265000,  75, 10),
  (2, N'Móc treo quần áo 6 móc',      N'cái',  75000, 120, 15),

  (3, N'Khung ảnh gỗ 20x30cm',        N'cái',  85000, 180, 20),
  (3, N'Thảm trải sàn 160x230cm',     N'cái', 890000,  30,  5),

  (4, N'Khăn tắm cotton 70x140cm',    N'cái', 125000, 240, 25),
  (4, N'Bàn chải vệ sinh bồn cầu',    N'cái',  55000, 300, 30),

  (5, N'Quạt đứng 3 cánh 40W',        N'cái', 750000,  45,  5),
  (5, N'Ấm đun nước 1.7L 2200W',      N'cái', 385000,  60,  8),
  (5, N'Máy xay sinh tố 500W',        N'cái', 495000,  40,  5),

  (6, N'Xẻng trồng cây inox',         N'cái',  65000, 160, 15),
  (6, N'Bình tưới cây 5L',            N'cái',  78000, 130, 15),

  (7, N'Hộp nhựa đựng thực phẩm 1.5L',N'cái',  48000, 400, 40),
  (7, N'Kệ sách 5 tầng 80x180cm',     N'cái', 650000,  20,  3),

  (8, N'Chổi lau nhà cotton',         N'cái', 115000, 220, 25),
  (8, N'Nước lau sàn hương chanh 2L', N'chai', 95000, 180, 20),
  (8, N'Nước rửa bát 750ml',          N'chai', 48000, 350, 40),

  (5, N'Quạt trần 5 cánh 60W',        N'cái',1250000,  25,  3),
  (1, N'Bộ dao nhà bếp 5 món',        N'bộ',  320000,  50,  8),
  (4, N'Ghế nhựa đa năng',            N'cái', 125000, 100, 15),
  (3, N'Đèn bàn học LED 10W',         N'cái', 185000,  80, 10);

-- ─────────────────────────────────────────────────────────────
-- 13. INSERT customers
-- ─────────────────────────────────────────────────────────────
INSERT INTO customers (full_name, phone, email, address) VALUES
  (N'Nguyễn Văn An',    '0901234567', 'an.nguyen@email.vn',    N'12 Nguyễn Huệ, Q.1, TP.HCM'),
  (N'Trần Thị Bình',    '0912345678', 'binh.tran@gmail.com',   N'45 Lê Lợi, Q. Hải Châu, Đà Nẵng'),
  (N'Lê Văn Cường',     '0923456789', 'cuong.le@yahoo.com',    N'78 Trần Phú, Ba Đình, Hà Nội'),
  (N'Phạm Thị Dung',    '0934567890', 'dung.pham@outlook.com', N'23 Nguyễn Trãi, Q.5, TP.HCM'),
  (N'Hoàng Văn Em',     '0945678901', 'em.hoang@gmail.com',    N'56 Pasteur, Q.3, TP.HCM'),
  (N'Vũ Thị Hoa',       '0956789012', 'hoa.vu@gmail.com',      N'90 CMT8, Q.3, TP.HCM'),
  (N'Đặng Văn Giang',   '0967890123', 'giang.dang@email.vn',   N'34 Lý Thường Kiệt, Q. Hoàn Kiếm, Hà Nội'),
  (N'Bùi Thị Hương',    '0978901234', 'huong.bui@gmail.com',   N'67 Nguyễn Văn Linh, Q.7, TP.HCM'),
  (N'Nguyễn Minh Kiệt', '0989012345', 'kiet.nm@email.vn',      N'11 Hai Bà Trưng, Q.1, TP.HCM'),
  (N'Trần Văn Long',    '0990123456', 'long.tran@yahoo.com',   N'89 Trần Hưng Đạo, Q.5, TP.HCM');
-- ─────────────────────────────────────────────────────────────
-- 14. DML: INSERT orders + order_items 
-- Đơn 1 → 10
INSERT INTO orders (customer_id, user_id, order_date, status, total_amount) VALUES
(1, 1, '2024-01-05 09:30:00', 'done', 0),
(2, 1, '2024-01-07 14:15:00', 'done', 0),
(3, 1, '2024-01-10 10:00:00', 'shipped', 0),
(4, 1, '2024-02-12 16:45:00', 'done', 0),
(5, 1, '2024-02-15 08:20:00', 'done', 0),
(6, 1, '2024-03-20 11:30:00', 'done', 0),
(7, 1, '2024-04-05 09:00:00', 'confirmed', 0),
(8, 1, '2024-05-18 15:00:00', 'done', 0),
(9, 1, '2024-06-22 10:45:00', 'done', 0),
(10,1, '2024-07-01 14:00:00', 'cancelled', 0);

-- Chi tiết đơn
INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES
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

--insert suppliers
INSERT INTO suppliers (name, contact) VALUES
(N'Lock&Lock Việt Nam', 'contact@lock.vn'),
(N'Sunhouse', 'support@sunhouse.com.vn'),
(N'Philips VN', 'philips@vn.com'),
(N'Panasonic VN', 'pana@vn.com');
--insert cart
INSERT INTO carts (customer_id) VALUES
(1),(2),(3),(4),(5);
---cart item
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES
(1,1,2),
(1,3,1),
(2,5,4),
(3,10,1),
(4,15,2),
(5,20,1);
---payment
INSERT INTO payments (order_id, method, status, paid_at) VALUES
(1, N'COD', N'paid', GETDATE()),
(2, N'Banking', N'paid', GETDATE()),
(3, N'Momo', N'pending', NULL),
(4, N'COD', N'paid', GETDATE());
---promotions
INSERT INTO promotions (name, discount_percent, start_date, end_date) VALUES
(N'Giảm giá Tết', 10, '2024-01-01', '2024-02-01'),
(N'Sale mùa hè', 15, '2024-06-01', '2024-07-01'),
(N'Khách hàng VIP', 20, '2024-01-01', '2025-01-01');
---cus-pro
INSERT INTO customer_promotions VALUES
(1,1),
(2,2),
(3,3);
--prod-prom
INSERT INTO product_promotions VALUES
(1,1),
(2,1),
(10,2),
(15,2),
(20,3);
--content
INSERT INTO contents (title, content, type) VALUES
(N'Mẹo nấu ăn ngon', N'Cách nấu ăn nhanh gọn...', 'blog'),
(N'Tin khuyến mãi tháng 6', N'Giảm giá toàn bộ sản phẩm...', 'news');
---review
INSERT INTO reviews (product_id, customer_id, rating, comment) VALUES
(1,1,5,N'Sản phẩm rất tốt'),
(2,2,4,N'Dùng ổn'),
(3,3,3,N'Tạm được'),
(4,4,5,N'Rất đáng tiền'),
(5,5,4,N'Hài lòng');
--order log
INSERT INTO order_logs (order_id, status) VALUES
(1,'done'),
(2,'done'),
(3,'shipped'),
(4,'done');
--return
INSERT INTO returns (order_id, reason, status, refund_amount) VALUES
(10, N'Lỗi sản phẩm', N'pending', 285000),
(9, N'Không đúng mô tả', N'approved', 125000);
-- ─────────────────────────────────────────────────────────────
-- 15. DEMO QUERIES
-- ─────────────────────────────────────────────────────────────

SELECT p.product_id, p.name, c.name AS category,
       p.price, p.stock_qty, p.unit
FROM products p
JOIN categories c ON p.category_id = c.category_id
WHERE p.status = 'active'
ORDER BY p.category_id, p.name;
------------------------------------------
SELECT product_id, name, stock_qty, min_stock,
       (min_stock - stock_qty) AS can_nhap_them
FROM products
WHERE status = 'active'
  AND stock_qty < min_stock
ORDER BY can_nhap_them DESC;
------------------------------------------------
SELECT o.order_id,
       c.full_name AS khach_hang,
       c.phone,
       o.order_date,
       o.status,
       p.name AS san_pham,
       oi.quantity,
       oi.unit_price AS don_gia,
       (oi.quantity * oi.unit_price) AS thanh_tien
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id
WHERE o.order_id = 2;
---------------------------------------------
SELECT 
  FORMAT(order_date, 'yyyy-MM') AS thang,
  COUNT(*) AS so_don,
  SUM(total_amount) AS doanh_thu,
  AVG(total_amount) AS trung_binh
FROM orders
WHERE status = 'done'
GROUP BY FORMAT(order_date, 'yyyy-MM')
ORDER BY thang;
---------------------------------------
SELECT TOP 10
       p.product_id,
       p.name,
       c.name AS danh_muc,
       SUM(oi.quantity) AS tong_ban,
       SUM(oi.quantity * oi.unit_price) AS tong_doanh_thu
FROM order_items oi
JOIN products p ON oi.product_id = p.product_id
JOIN categories c ON c.category_id = p.category_id
JOIN orders o ON oi.order_id = o.order_id
WHERE o.status = 'done'
GROUP BY p.product_id, p.name, c.name
ORDER BY tong_ban DESC;
----------------------------------------------
SELECT status,
       COUNT(*) AS so_don,
       SUM(total_amount) AS tong_tien
FROM orders
GROUP BY status
ORDER BY so_don DESC;
---------------------------------------------
BEGIN TRANSACTION;

DECLARE @new_order_id INT;

INSERT INTO orders (customer_id, user_id, status, total_amount)
VALUES (1, 1, 'pending', 0);

SET @new_order_id = SCOPE_IDENTITY();

INSERT INTO order_items (order_id, product_id, quantity, unit_price)
VALUES
(@new_order_id, 4, 1, 135000),
(@new_order_id, 6, 1, 195000),
(@new_order_id, 21, 2, 78000);

UPDATE orders
SET total_amount = (
    SELECT SUM(quantity * unit_price)
    FROM order_items
    WHERE order_id = @new_order_id
),
status = 'confirmed'
WHERE order_id = @new_order_id;

COMMIT;


SELECT 'categories' AS table_name, COUNT(*) AS total FROM categories
UNION ALL
SELECT 'products', COUNT(*) FROM products
UNION ALL
SELECT 'customers', COUNT(*) FROM customers
UNION ALL
SELECT 'users', COUNT(*) FROM users
UNION ALL
SELECT 'orders', COUNT(*) FROM orders
UNION ALL
SELECT 'order_items', COUNT(*) FROM order_items;

----Sản phẩm không có danh mục (lỗi FK logic)
SELECT p.*
FROM products p
LEFT JOIN categories c ON p.category_id = c.category_id
WHERE c.category_id IS NULL;

SELECT oi.*
FROM order_items oi
LEFT JOIN orders o ON oi.order_id = o.order_id
WHERE o.order_id IS NULL;
----tính total amount
SELECT o.order_id,
       o.total_amount,
       SUM(oi.quantity * oi.unit_price) AS recalculated
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id
GROUP BY o.order_id, o.total_amount
HAVING o.total_amount <> SUM(oi.quantity * oi.unit_price);
---Khách mua nhiều nhất 
SELECT c.customer_id, c.full_name
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.customer_id IS NULL;

----kiểm tra user
SELECT *
FROM users
WHERE status = 'active';

SELECT 
  (SELECT COUNT(*) FROM orders) AS total_orders,
  (SELECT COUNT(*) FROM order_items) AS total_items,
  (SELECT SUM(total_amount) FROM orders WHERE status='done') AS total_revenue,
  (SELECT COUNT(*) FROM products WHERE stock_qty < min_stock) AS low_stock_products;

  ---kiểm tra hết hàng
  SELECT name, stock_qty, min_stock
FROM products
WHERE stock_qty < min_stock;