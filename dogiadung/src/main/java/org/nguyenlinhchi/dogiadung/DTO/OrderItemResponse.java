package org.nguyenlinhchi.dogiadung.DTO;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Integer orderId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;

    // Constructor đúng thứ tự và kiểu dữ liệu
    public OrderItemResponse(Integer orderId,
                             Integer productId,
                             String productName,
                             Integer quantity,
                             BigDecimal unitPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters
    public Integer getOrderId() {
        return orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}