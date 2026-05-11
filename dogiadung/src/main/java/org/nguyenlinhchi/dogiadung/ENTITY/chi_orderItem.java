package org.nguyenlinhchi.dogiadung.ENTITY;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "chi_order_items")
@IdClass(chi_orderItemId.class)
public class chi_orderItem {

    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Id
    @Column(name = "product_id")
    private Integer productId;

    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false)
    @JsonProperty("unitPrice")           // ← Sửa cho khớp với Frontend
    private BigDecimal unitPrice;

    @JsonProperty("rating")
    private Integer rating;

    @Column(name = "review_comment")
    @JsonProperty("reviewComment")       // ← Sửa
    private String reviewComment;

    @Column(name = "is_approved")
    @JsonProperty("isApproved")          // ← Sửa
    private Boolean isApproved;

    @Column(name = "reviewed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt;

    public chi_orderItem() {}

    // Getters & Setters
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public Date getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Date reviewedAt) { this.reviewedAt = reviewedAt; }
}