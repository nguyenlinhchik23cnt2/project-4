package org.nguyenlinhchi.dogiadung.ENTITY;
import java.io.Serializable;
import java.util.Objects;

public class chi_orderItemId implements Serializable {

    private Integer orderId;
    private Integer productId;

    public chi_orderItemId() {}

    public chi_orderItemId(Integer orderId, Integer productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof chi_orderItemId)) return false;
        chi_orderItemId that = (chi_orderItemId) o;
        return Objects.equals(orderId, that.orderId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId);
    }
}