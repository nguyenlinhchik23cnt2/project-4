package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItem;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItemId;
import org.nguyenlinhchi.dogiadung.REPOSITORY.chi_orderItemRepository; // ✅ THÊM DÒNG NÀY
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class chi_orderItemService {

    @Autowired
    private chi_orderItemRepository repo;

    public chi_orderItem create(chi_orderItem item) {
        return repo.save(item);
    }

    public List<chi_orderItem> getAll() {
        return repo.findAll();
    }

    public List<chi_orderItem> getByOrderId(Integer orderId) {
        return repo.findByOrderId(orderId);
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

    // ===== F59: thêm đánh giá =====
    public chi_orderItem addReview(Integer orderId, Integer productId,
                                   Integer rating, String comment) {

        chi_orderItem item = getOne(orderId, productId);
        if (item == null) return null;

        item.setRating(rating);
        item.setReviewComment(comment);
        item.setReviewedAt(new Date());
        item.setIsApproved(null); // chờ duyệt

        return repo.save(item);
    }

    // ===== F60: duyệt đánh giá =====
    public chi_orderItem approveReview(Integer orderId, Integer productId, Boolean approved) {

        chi_orderItem item = getOne(orderId, productId);
        if (item == null) return null;

        item.setIsApproved(approved);
        return repo.save(item);
    }
}
