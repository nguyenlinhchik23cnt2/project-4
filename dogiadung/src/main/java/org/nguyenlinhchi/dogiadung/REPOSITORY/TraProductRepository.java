package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TraProductRepository extends JpaRepository<TraProduct, Integer> {

    // Tìm theo status (active / inactive)
    List<TraProduct> findByStatus(String status);

    // Tìm sản phẩm nổi bật
    List<TraProduct> findByIsFeaturedTrue();

    // Tìm theo category
    List<TraProduct> findByCategoryId(Integer categoryId);

    // Tìm theo tên (chứa, không phân biệt hoa thường)
    List<TraProduct> findByNameContainingIgnoreCase(String keyword);

    // Lấy sản phẩm sắp hết hàng (stock < min_stock)
    @Query("SELECT p FROM TraProduct p WHERE p.stockQty < p.minStock AND p.status = 'active'")
    List<TraProduct> findLowStockProducts();

    // Tìm theo category + status (ví dụ kết hợp nhiều điều kiện)
    List<TraProduct> findByCategoryIdAndStatus(Integer categoryId, String status);
}
