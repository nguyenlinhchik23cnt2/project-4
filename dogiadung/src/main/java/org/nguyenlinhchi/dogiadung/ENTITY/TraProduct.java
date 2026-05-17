package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
<<<<<<< HEAD
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

=======

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;


>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
@Entity
@Table(name = "tra_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

<<<<<<< HEAD
    /* ---- Quan hệ với Category (Lấy tên danh mục) ---- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)  // ← SỬA Ở ĐÂY
    private NgaCategory category;

    /* ---- Thông tin nhà cung cấp ---- */
    @Column(name = "supp_name", length = 100)
=======
    /* ---- Thông tin nhà cung cấp ---- */
    @Column(name = "supp_name",    length = 100)
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
    private String suppName;

    @Column(name = "supp_contact", length = 100)
    private String suppContact;

<<<<<<< HEAD
    @Column(name = "supp_phone", length = 20)
    private String suppPhone;

    @Column(name = "supp_email", length = 100)
    private String suppEmail;

    @Column(name = "supp_status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'active'")
    private String suppStatus = "active";

    /* ---- Thông tin sản phẩm ---- */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "unit", nullable = false, length = 50)
    private String unit;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_qty", columnDefinition = "INT DEFAULT 0")
    private Integer stockQty = 0;

    @Column(name = "min_stock", columnDefinition = "INT DEFAULT 10")
    private Integer minStock = 10;

    @Column(name = "sold_qty", columnDefinition = "INT DEFAULT 0")
    private Integer soldQty = 0;

    @Column(name = "is_featured", columnDefinition = "BIT DEFAULT 0")
    private Boolean isFeatured = false;

    @Column(name = "weight_kg", precision = 8, scale = 3)
    private BigDecimal weightKg;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'active'")
    private String status = "active";

    /* ---- Timestamps ---- */
    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "DATETIME DEFAULT GETDATE()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

=======
    @Column(name = "supp_phone",   length = 20)
    private String suppPhone;

    @Column(name = "supp_email",   length = 100)
    private String suppEmail;

    @Column(name = "supp_status",  length = 20,  columnDefinition = "VARCHAR(20) DEFAULT 'active'")
    private String suppStatus = "active";

    /* ---- Thông tin sản phẩm ---- */
    @Column(name = "name",         nullable = false, length = 200)
    private String name;

    @Column(name = "unit",         nullable = false, length = 50)
    private String unit;

    @Column(name = "price",        nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_qty",    columnDefinition = "INT DEFAULT 0")
    private Integer stockQty = 0;

    @Column(name = "min_stock",    columnDefinition = "INT DEFAULT 10")
    private Integer minStock = 10;

    @Column(name = "sold_qty",     columnDefinition = "INT DEFAULT 0")
    private Integer soldQty = 0;

    @Column(name = "is_featured",  columnDefinition = "BIT DEFAULT 0")
    private Boolean isFeatured = false;

    @Column(name = "weight_kg",    precision = 8, scale = 3)
    private BigDecimal weightKg;

    @Column(name = "description",  columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "image_url",    length = 255)
    private String imageUrl;

    @Column(name = "status",       length = 20,  columnDefinition = "VARCHAR(20) DEFAULT 'active'")
    private String status = "active";

    /* ---- Timestamps ---- */
    @Column(name = "created_at",   updatable = false,
            columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime createdAt;

    @Column(name = "updated_at",
            columnDefinition = "DATETIME DEFAULT GETDATE()")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    //tự gán create_at cho insert đầu//
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
