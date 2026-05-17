package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chi_cart_items",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_cart",
                columnNames = {"customer_id", "product_id"}
        ))
public class chi_cartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_id", nullable = false)
    private Integer customerId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "added_at",
            columnDefinition = "DATETIME",
            updatable = false,
            insertable = false)
    private LocalDateTime addedAt;
    // Constructors
    public chi_cartItem() {}

    public chi_cartItem(Integer customerId, Integer productId, Integer quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity != null ? quantity : 1;

    }

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}