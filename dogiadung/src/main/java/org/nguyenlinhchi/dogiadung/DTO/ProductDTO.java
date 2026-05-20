package org.nguyenlinhchi.dogiadung.DTO;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * DTO cho Product - Dùng cho cả Request và Response
 * Tách biệt với Entity để không expose thông tin database
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Integer productId;       // null khi tạo mới

    @NotNull(message = "Danh mục không được để trống")
    private Integer categoryId;
    private String categoryName;
    // Thông tin nhà cung cấp
    private String suppName;
    private String suppContact;
    private String suppPhone;
    private String suppEmail;
    private String suppStatus;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 200, message = "Tên sản phẩm tối đa 200 ký tự")
    private String name;

    @NotBlank(message = "Đơn vị không được để trống")
    @Size(max = 50, message = "Đơn vị tối đa 50 ký tự")
    private String unit;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.0", message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @Min(value = 0, message = "Số lượng tồn kho phải >= 0")
    private Integer stockQty;

    @Min(value = 0, message = "Tồn kho tối thiểu phải >= 0")
    private Integer minStock;

    @Min(value = 0, message = "Số lượng đã bán phải >= 0")
    private Integer soldQty;

    private Boolean isFeatured;

    @DecimalMin(value = "0.0", message = "Trọng lượng phải >= 0")
    private BigDecimal weightKg;

    @Size(max = 2000, message = "Mô tả tối đa 2000 ký tự")
    private String description;

    @Size(max = 500, message = "Đường dẫn hình ảnh tối đa 500 ký tự")
    private String imageUrl;

    @Pattern(regexp = "active|inactive", message = "Trạng thái chỉ được là 'active' hoặc 'inactive'")
    private String status;
}