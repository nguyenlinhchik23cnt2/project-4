package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItem;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItemId;
import org.nguyenlinhchi.dogiadung.REPOSITORY.chi_orderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class chi_orderItemService {

    @Autowired
    private chi_orderItemRepository repo;

    // ─── CRUD ────────────────────────────────────────────────

    /**
     * Tạo order item mới.
     * customerId nên được điền trong item trước khi gọi hàm này
     * để liên kết với khách hàng.
     */
    public chi_orderItem create(chi_orderItem item) {
        return repo.save(item);
    }

    public List<chi_orderItem> getAll() {
        return repo.findAll();
    }

    public List<chi_orderItem> getByOrderId(Integer orderId) {
        return repo.findByOrderId(orderId);
    }

    /**
     * Lấy tất cả order items của 1 khách hàng.
     * Dùng cho profile.html để hiển thị lịch sử đơn hàng.
     */
    public List<chi_orderItem> getByCustomerId(Integer customerId) {
        return repo.findByCustomerId(customerId);
    }

    /**
     * Lấy order items đã có đánh giá của 1 khách hàng.
     */
    public List<chi_orderItem> getReviewedByCustomerId(Integer customerId) {
        return repo.findByCustomerIdAndRatingIsNotNull(customerId);
    }

    public chi_orderItem getOne(Integer orderId, Integer productId) {
        return repo.findById(new chi_orderItemId(orderId, productId)).orElse(null);
    }

    public chi_orderItem update(chi_orderItem item) {
        return repo.save(item);
    }

    public void delete(Integer orderId, Integer productId) {
        repo.deleteById(new chi_orderItemId(orderId, productId));
    }

    // ─── REVIEW ──────────────────────────────────────────────

    /** F59: Thêm / cập nhật đánh giá sản phẩm */
    public chi_orderItem addReview(Integer orderId, Integer productId,
                                   Integer rating, String comment) {
        chi_orderItem item = getOne(orderId, productId);
        if (item == null) return null;

        item.setRating(rating);
        item.setReviewComment(comment);
        item.setReviewedAt(new Date());
        item.setIsApproved(null);   // chờ admin duyệt

        return repo.save(item);
    }

    /** F60: Admin duyệt / từ chối đánh giá */
    public chi_orderItem approveReview(Integer orderId, Integer productId,
                                       Boolean approved) {
        chi_orderItem item = getOne(orderId, productId);
        if (item == null) return null;

        item.setIsApproved(approved);
        return repo.save(item);
    }
}