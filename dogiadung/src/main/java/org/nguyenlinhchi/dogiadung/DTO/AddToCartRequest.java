package org.nguyenlinhchi.dogiadung.DTO;

import lombok.Data;

// AddToCartRequest.java
@Data
public class AddToCartRequest {
    private Integer customerId;
    private Integer productId;
    private Integer quantity = 1;
}
