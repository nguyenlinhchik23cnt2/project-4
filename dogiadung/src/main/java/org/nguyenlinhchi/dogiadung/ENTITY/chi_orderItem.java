package org.nguyenlinhchi.dogiadung.ENTITY;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Entity
@Table(name = "chi_order_items")
@IdClass(chi_orderItemId.class)
public class chi_orderItem {

    // Getters & Setters
    @Id
    @Column(name = "order_id")
    private Integer orderId;

    @Id
    @Column(name = "product_id")
    private Integer productId;

    /**
     * Lưu customerId để truy vấn trực tiếp order items của 1 khách hàng
     * mà không cần JOIN qua bảng Order.
     * Điền khi tạo order item (trong chi_orderItemService.create).
     */
    @Column(name = "customer_id")
    @JsonProperty("customerId")
    private Integer customerId;

    private Integer quantity = 1;

    @Column(name = "unit_price", nullable = false)
    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;

    @JsonProperty("rating")
    private Integer rating;

    @Column(name = "review_comment")
    @JsonProperty("reviewComment")
    private String reviewComment;

    @Column(name = "is_approved")
    @JsonProperty("isApproved")
    private Boolean isApproved;

    @Column(name = "reviewed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewedAt;

    public chi_orderItem() {}

    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public void setProductId(Integer productId) { this.productId = productId; }

    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public void setRating(Integer rating) { this.rating = rating; }

    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }

    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public void setReviewedAt(Date reviewedAt) { this.reviewedAt = reviewedAt; }
}