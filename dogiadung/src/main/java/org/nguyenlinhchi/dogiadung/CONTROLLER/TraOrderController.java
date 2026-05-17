package org.nguyenlinhchi.dogiadung.CONTROLLER;

import lombok.RequiredArgsConstructor;
import org.nguyenlinhchi.dogiadung.ENTITY.TraOrder;
import org.nguyenlinhchi.dogiadung.SERVICE.TraOrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TraOrderController {

    private final TraOrderService service;

    // GET /api/orders
    @GetMapping
    public ResponseEntity<List<TraOrder>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // GET /api/orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TraOrder> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // POST /api/orders
    @PostMapping
    public ResponseEntity<TraOrder> create(@RequestBody TraOrder order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(order));
    }

    // PUT /api/orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TraOrder> update(@PathVariable Integer id,
                                           @RequestBody TraOrder order) {
        return ResponseEntity.ok(service.update(id, order));
    }

    // DELETE /api/orders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công đơn hàng id=" + id));
    }

    // PATCH /api/orders/{id}/status?value=confirmed
    @PatchMapping("/{id}/status")
    public ResponseEntity<TraOrder> changeStatus(@PathVariable Integer id,
                                                 @RequestParam String value) {
        return ResponseEntity.ok(service.changeStatus(id, value));
    }

    // PATCH /api/orders/{id}/pay?txnId=TXN123
    @PatchMapping("/{id}/pay")
    public ResponseEntity<TraOrder> markPaid(@PathVariable Integer id,
                                             @RequestParam(required = false) String txnId) {
        return ResponseEntity.ok(service.markPaid(id, txnId));
    }

    // GET /api/orders/customer/{customerId}
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TraOrder>> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(service.findByCustomer(customerId));
    }

    // GET /api/orders/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TraOrder>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    // GET /api/orders/status/pending
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TraOrder>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    // GET /api/orders/pay-status/paid
    @GetMapping("/pay-status/{payStatus}")
    public ResponseEntity<List<TraOrder>> getByPayStatus(@PathVariable String payStatus) {
        return ResponseEntity.ok(service.findByPayStatus(payStatus));
    }

    // GET /api/orders/pay-method/COD
    @GetMapping("/pay-method/{payMethod}")
    public ResponseEntity<List<TraOrder>> getByPayMethod(@PathVariable String payMethod) {
        return ResponseEntity.ok(service.findByPayMethod(payMethod));
    }

    // GET /api/orders/disc-code/SUMMER10
    @GetMapping("/disc-code/{discCode}")
    public ResponseEntity<List<TraOrder>> getByDiscCode(@PathVariable String discCode) {
        return ResponseEntity.ok(service.findByDiscCode(discCode));
    }

    // GET /api/orders/date-range?from=2024-01-01T00:00:00&to=2024-12-31T23:59:59
    @GetMapping("/date-range")
    public ResponseEntity<List<TraOrder>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(service.findByDateRange(from, to));
    }
}
