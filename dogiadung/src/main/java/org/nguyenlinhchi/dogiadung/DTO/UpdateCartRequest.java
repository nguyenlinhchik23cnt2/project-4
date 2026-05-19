package org.nguyenlinhchi.dogiadung.DTO;

import lombok.Data;

// UpdateCartRequest.java
@Data
public class UpdateCartRequest {
    private Integer customerId;
    private Integer productId;
    private Integer quantity;
}
