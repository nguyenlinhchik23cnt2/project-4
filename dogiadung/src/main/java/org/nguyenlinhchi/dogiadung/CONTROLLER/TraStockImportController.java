package org.nguyenlinhchi.dogiadung.CONTROLLER;

<<<<<<< HEAD
import org.nguyenlinhchi.dogiadung.SERVICE.TraStockImportService;

=======
import com.example.product.service.StockImportService;
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
import lombok.RequiredArgsConstructor;
import org.nguyenlinhchi.dogiadung.ENTITY.TraStockImport;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
=======

>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock-imports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TraStockImportController {

<<<<<<< HEAD
    private final TraStockImportService service;
=======
    private final StockImportService service;
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0

    // GET /api/stock-imports
    @GetMapping
    public ResponseEntity<List<TraStockImport>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // GET /api/stock-imports/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TraStockImport> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // POST /api/stock-imports
    @PostMapping
    public ResponseEntity<TraStockImport> create(@RequestBody TraStockImport stockImport) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(stockImport));
    }

    // PUT /api/stock-imports/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TraStockImport> update(@PathVariable Integer id,
                                                 @RequestBody TraStockImport stockImport) {
        return ResponseEntity.ok(service.update(id, stockImport));
    }

    // DELETE /api/stock-imports/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công phiếu nhập id=" + id));
    }

    // PATCH /api/stock-imports/{id}/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TraStockImport> cancel(@PathVariable Integer id) {
        return ResponseEntity.ok(service.cancel(id));
    }

    // GET /api/stock-imports/product/{productId}
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TraStockImport>> getByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(service.findByProduct(productId));
    }

    // GET /api/stock-imports/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TraStockImport>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    // GET /api/stock-imports/status/done
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TraStockImport>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    // GET /api/stock-imports/batch/BATCH001
    @GetMapping("/batch/{batchCode}")
    public ResponseEntity<TraStockImport> getByBatchCode(@PathVariable String batchCode) {
        return ResponseEntity.ok(service.findByBatchCode(batchCode));
    }

    // GET /api/stock-imports/date-range?from=2024-01-01T00:00:00&to=2024-12-31T23:59:59
    @GetMapping("/date-range")
    public ResponseEntity<List<TraStockImport>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(service.findByDateRange(from, to));
    }
}
