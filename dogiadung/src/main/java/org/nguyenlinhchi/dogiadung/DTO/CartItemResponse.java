package org.nguyenlinhchi.dogiadung.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class CartItemResponse {
        private Integer cartItemId;
        private Integer productId;
        private String productName;
        private BigDecimal price;
        private String imageUrl;
        private Integer quantity;
        private BigDecimal subtotal;

}

