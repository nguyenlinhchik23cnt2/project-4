package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tra_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TraOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "order_date", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private LocalDateTime orderDate;

    @Builder.Default
    @Column(name = "status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'pending'")
    private String status = "pending";

    @Column(name = "shipping_address", length = 200)
    private String shippingAddress;

    @Builder.Default
    @Column(name = "shipping_fee", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @Column(name = "note", columnDefinition = "NVARCHAR(MAX)")
    private String note;

    @Builder.Default
    @Column(name = "total_amount", precision = 15, scale = 2, columnDefinition = "DECIMAL(15,2) DEFAULT 0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    // ── Discount ──────────────────────────────────────────────────
    @Column(name = "disc_code", length = 50)
    private String discCode;

    @Column(name = "disc_type", length = 10)
    private String discType;           // 'percent' | 'fixed'

    @Column(name = "disc_value", precision = 12, scale = 2)
    private BigDecimal discValue;

    @Column(name = "disc_min_order", precision = 12, scale = 2)
    private BigDecimal discMinOrder;

    @Builder.Default
    @Column(name = "discount_amount", precision = 12, scale = 2, columnDefinition = "DECIMAL(12,2) DEFAULT 0")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    // ── Payment ───────────────────────────────────────────────────
    @Column(name = "pay_method", length = 50)
    private String payMethod;          // 'COD' | 'Banking' | 'Momo' | 'VNPay'

    @Builder.Default
    @Column(name = "pay_status", length = 20, columnDefinition = "NVARCHAR(20) DEFAULT 'pending'")
    private String payStatus = "pending"; // 'pending' | 'paid' | 'failed' | 'refunded'

    @Column(name = "pay_amount", precision = 15, scale = 2)
    private BigDecimal payAmount;

    @Column(name = "pay_txn_id", length = 100)
    private String payTxnId;

    @Column(name = "pay_at")
    private LocalDateTime payAt;

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) orderDate = LocalDateTime.now();
    }
}