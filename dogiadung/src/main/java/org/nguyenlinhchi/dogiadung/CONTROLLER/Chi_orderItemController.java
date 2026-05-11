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

    // GET ALL
    @GetMapping
    public List<chi_orderItem> getAll() {
        return service.getAll();
    }

    // CREATE
    @PostMapping("/create")
    public chi_orderItem create(@RequestBody chi_orderItem item) {
        return service.create(item);
    }

    // DELETE
    @DeleteMapping("/{orderId}/{productId}")
    public String delete(@PathVariable Integer orderId, @PathVariable Integer productId) {
        service.delete(orderId, productId);
        return "Deleted!";
    }

    // ================= REVIEW =================
    @PostMapping("/review")
    public chi_orderItem addReview(@RequestBody Map<String, Object> request) {
        Integer orderId = Integer.valueOf(request.get("orderId").toString());
        Integer productId = Integer.valueOf(request.get("productId").toString());
        Integer rating = Integer.valueOf(request.get("rating").toString());
        String comment = (String) request.get("comment");

        return service.addReview(orderId, productId, rating, comment);
    }

    // APPROVE REVIEW
    @PutMapping("/review/approve")
    public chi_orderItem approve(@RequestBody Map<String, Object> request) {
        Integer orderId = Integer.valueOf(request.get("orderId").toString());
        Integer productId = Integer.valueOf(request.get("productId").toString());
        Boolean approved = Boolean.valueOf(request.get("approved").toString());

        return service.approveReview(orderId, productId, approved);
    }
}