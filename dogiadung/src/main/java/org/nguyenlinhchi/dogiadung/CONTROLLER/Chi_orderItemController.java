package org.nguyenlinhchi.dogiadung.CONTROLLER;

import lombok.RequiredArgsConstructor;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItem;
import org.nguyenlinhchi.dogiadung.SERVICE.chi_orderItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class Chi_orderItemController {

    private final chi_orderItemService service;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<chi_orderItem>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // CREATE
    @PostMapping
    public ResponseEntity<chi_orderItem> create(@RequestBody chi_orderItem item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(item));
    }

    // DELETE by Composite Key
    @DeleteMapping("/{orderId}/{productId}")
    public ResponseEntity<Void> delete(@PathVariable Integer orderId,
                                       @PathVariable Integer productId) {
        service.delete(orderId, productId);
        return ResponseEntity.noContent().build();
    }

    // ================= REVIEW =================
    @PostMapping("/review")
    public ResponseEntity<chi_orderItem> addReview(@RequestBody Map<String, Object> request) {
        try {
            Integer orderId = Integer.valueOf(request.get("orderId").toString());
            Integer productId = Integer.valueOf(request.get("productId").toString());
            Integer rating = Integer.valueOf(request.get("rating").toString());
            String comment = (String) request.get("comment");

            chi_orderItem result = service.addReview(orderId, productId, rating, comment);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // APPROVE REVIEW
    @PutMapping("/review/approve")
    public ResponseEntity<chi_orderItem> approveReview(@RequestBody Map<String, Object> request) {
        try {
            Integer orderId = Integer.valueOf(request.get("orderId").toString());
            Integer productId = Integer.valueOf(request.get("productId").toString());
            Boolean approved = Boolean.valueOf(request.get("approved").toString());

            chi_orderItem result = service.approveReview(orderId, productId, approved);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}