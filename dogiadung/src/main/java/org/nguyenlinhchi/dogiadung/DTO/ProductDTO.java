package com.example.product.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO dùng cho request (POST/PUT) và response.
 * Tách khỏi Entity để tránh expose toàn bộ DB schema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Integer productId;       // null khi tạo mới

    @NotNull(message = "category_id không được để trống")
    private Integer categoryId;

    private String suppName;
    private String suppContact;
    private String suppPhone;
    private String suppEmail;
    private String suppStatus;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 200)
    private String name;

    @NotBlank(message = "Đơn vị không được để trống")
    private String unit;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải >= 0")
    private BigDecimal price;

    @Min(value = 0, message = "Tồn kho phải >= 0")
    private Integer stockQty;

    @Min(value = 0, message = "Tồn kho tối thiểu phải >= 0")
    private Integer minStock;

    @Min(value = 0, message = "Số đã bán phải >= 0")
    private Integer soldQty;

    private Boolean isFeatured;
    private BigDecimal weightKg;
    private String description;
    private String imageUrl;

    @Pattern(regexp = "active|inactive", message = "status phải là 'active' hoặc 'inactive'")
    private String status;
}
