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

  // Lấy danh sách + Tìm kiếm + Lọc đa năng: GET http://localhost:8080/api/customers
  // Ví dụ gọi: http://localhost:8080/api/customers?keyword=0987&gender=Nam
  @GetMapping
  public ResponseEntity<List<NgaCustomer>> getAllAndSearch(
          @RequestParam(required = false) String keyword,
          @RequestParam(required = false) String gender) {
    List<NgaCustomer> list = customerService.searchAndFilter(keyword, gender);
    return ResponseEntity.ok(list);
  }

  // Lấy chi tiết 1 khách hàng: GET http://localhost:8080/api/customers/{id}
  @GetMapping("/{id}")
  public ResponseEntity<NgaCustomer> getById(@PathVariable Integer id) {
    NgaCustomer customer = customerService.getById(id);
    return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
  }

  // Thêm mới khách hàng: POST http://localhost:8080/api/customers
  @PostMapping
  public ResponseEntity<?> create(@RequestBody NgaCustomer customer) {
    Map<String, Object> response = new HashMap<>();
    try {
      NgaCustomer saved = customerService.create(customer);
      return ResponseEntity.ok(saved);
    } catch (Exception e) {
      response.put("success", false);
      response.put("message", e.getMessage());
      return ResponseEntity.badRequest().body(response);
    }
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

  // Xóa khách hàng: DELETE http://localhost:8080/api/customers/{id}
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable Integer id) {
    Map<String, Object> response = new HashMap<>();
    if (customerService.delete(id)) {
      response.put("success", true);
      response.put("message", "Xóa hồ sơ khách hàng thành công!");
      return ResponseEntity.ok(response);
    }
    response.put("success", false);
    response.put("message", "Không tìm thấy khách hàng cần xóa!");
    return ResponseEntity.badRequest().body(response);
  }
}