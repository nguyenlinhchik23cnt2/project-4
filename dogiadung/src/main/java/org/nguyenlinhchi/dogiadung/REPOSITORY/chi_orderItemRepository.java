package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItem;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface chi_orderItemRepository extends JpaRepository<chi_orderItem, chi_orderItemId> {
    /** Lấy tất cả sản phẩm trong 1 đơn hàng */
    List<chi_orderItem> findByOrderId(Integer orderId);

    /**
     * Lấy tất cả order items của 1 khách hàng (dùng field customerId mới thêm).
     * Dùng cho profile.html → tab Lịch sử / Đánh giá.
     */
    List<chi_orderItem> findByCustomerId(Integer customerId);

    /**
     * Lấy order items của 1 khách hàng, chỉ những item đã có đánh giá.
     */
    List<chi_orderItem> findByCustomerIdAndRatingIsNotNull(Integer customerId);
}