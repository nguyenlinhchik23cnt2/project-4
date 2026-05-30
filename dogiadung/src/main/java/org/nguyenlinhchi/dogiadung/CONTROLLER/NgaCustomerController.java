package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaCustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin("*")
public class NgaCustomerController {

    private final NgaCustomerService customerService;

    public NgaCustomerController(NgaCustomerService customerService) {
        this.customerService = customerService;
    }

<<<<<<< HEAD

    // Lấy chi tiết 1 khách hàng: GET http://localhost:8080/api/customers/{id}
=======
    // GET /api/customers/{id}
>>>>>>> 931edeeeb1e324e527ae9e0feee350a772cd7ed6
    @GetMapping("/{id}")
    public ResponseEntity<NgaCustomer> getById(@PathVariable Integer id) {
        NgaCustomer customer = customerService.getById(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

<<<<<<< HEAD
    // Cập nhật khách hàng: PUT http://localhost:8080/api/customers/{id}
=======
    // PUT /api/customers/{id}  — cập nhật thông tin + đổi username
>>>>>>> 931edeeeb1e324e527ae9e0feee350a772cd7ed6
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody NgaCustomer customer) {
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

<<<<<<< HEAD
=======
    // GET /api/customers/check-username?username=abc
    // profile.html gọi endpoint này khi người dùng gõ username mới (debounce 600ms)
    // Trả về: { "available": true } hoặc { "available": false }
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        boolean available = customerService.isUsernameAvailable(username);
        return ResponseEntity.ok(Map.of("available", available));
    }
>>>>>>> 931edeeeb1e324e527ae9e0feee350a772cd7ed6
}