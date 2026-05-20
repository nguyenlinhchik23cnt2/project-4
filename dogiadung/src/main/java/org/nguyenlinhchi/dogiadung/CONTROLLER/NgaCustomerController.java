package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaCustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*") // Chống lỗi Block CORS khi chạy Frontend cục bộ
public class NgaCustomerController {

    private final NgaCustomerService customerService;

    public NgaCustomerController(NgaCustomerService customerService) {
        this.customerService = customerService;
    }


    // Lấy chi tiết 1 khách hàng: GET http://localhost:8080/api/customers/{id}
    @GetMapping("/{id}")
    public ResponseEntity<NgaCustomer> getById(@PathVariable Integer id) {
        NgaCustomer customer = customerService.getById(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    // Cập nhật khách hàng: PUT http://localhost:8080/api/customers/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody NgaCustomer customer) {
        Map<String, Object> response = new HashMap<>();
        try {
            NgaCustomer updated = customerService.update(id, customer);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}