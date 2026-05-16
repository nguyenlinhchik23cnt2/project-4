package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tra_stock_imports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraStockImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "batch_code", length = 50)
    private String batchCode;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "import_date", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime importDate;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "import_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal importPrice;

    @Column(name = "note", length = 200)
    private String note;

    @Column(name = "status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'done'")
    private String status = "done";

    @PrePersist
    protected void onCreate() {
        if (importDate == null) importDate = LocalDateTime.now();
    }
}
