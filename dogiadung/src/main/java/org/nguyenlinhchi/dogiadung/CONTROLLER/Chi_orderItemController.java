package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItem;
import org.nguyenlinhchi.dogiadung.SERVICE.chi_orderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin(origins = "*")
public class Chi_orderItemController {

    @Autowired
    private chi_orderItemService service;

    // ─── GET ALL ─────────────────────────────────────────────
    @GetMapping
    public List<chi_orderItem> getAll() {
        return service.getAll();
    }

    // ─── GET BY ORDER ─────────────────────────────────────────
    @GetMapping("/by-order/{orderId}")
    public List<chi_orderItem> getByOrder(@PathVariable Integer orderId) {
        return service.getByOrderId(orderId);
    }

    // ─── GET BY CUSTOMER (dùng cho profile.html) ─────────────
    /**
     * GET /api/order-items/by-customer/{customerId}
     * profile.html dùng endpoint này để load lịch sử đơn hàng / đánh giá.
     */
    @GetMapping("/by-customer/{customerId}")
    public List<chi_orderItem> getByCustomer(@PathVariable Integer customerId) {
        return service.getByCustomerId(customerId);
    }

    /**
     * GET /api/order-items/by-customer/{customerId}/reviews
     * Chỉ lấy các item đã có đánh giá của khách hàng.
     */
    @GetMapping("/by-customer/{customerId}/reviews")
    public List<chi_orderItem> getReviewsByCustomer(@PathVariable Integer customerId) {
        return service.getReviewedByCustomerId(customerId);
    }

    // ─── CREATE ──────────────────────────────────────────────
    @PostMapping("/create")
    public chi_orderItem create(@RequestBody chi_orderItem item) {
        return service.create(item);
    }

    // ─── DELETE ──────────────────────────────────────────────
    @DeleteMapping("/{orderId}/{productId}")
    public String delete(@PathVariable Integer orderId,
                         @PathVariable Integer productId) {
        service.delete(orderId, productId);
        return "Deleted!";
    }

    // ─── REVIEW ──────────────────────────────────────────────

    /** POST /api/order-items/review — khách hàng gửi đánh giá */
    @PostMapping("/review")
    public chi_orderItem addReview(@RequestBody Map<String, Object> request) {
        Integer orderId   = Integer.valueOf(request.get("orderId").toString());
        Integer productId = Integer.valueOf(request.get("productId").toString());
        Integer rating    = Integer.valueOf(request.get("rating").toString());
        String  comment   = (String) request.get("comment");
        return service.addReview(orderId, productId, rating, comment);
    }

    /** PUT /api/order-items/review/approve — admin duyệt đánh giá */
    @PutMapping("/review/approve")
    public chi_orderItem approve(@RequestBody Map<String, Object> request) {
        Integer orderId   = Integer.valueOf(request.get("orderId").toString());
        Integer productId = Integer.valueOf(request.get("productId").toString());
        Boolean approved  = Boolean.valueOf(request.get("approved").toString());
        return service.approveReview(orderId, productId, approved);
    }
}