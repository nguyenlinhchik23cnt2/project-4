package org.nguyenlinhchi.dogiadung.CONTROLLER;

import lombok.RequiredArgsConstructor;
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.SERVICE.TraProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TraProductController {

    private final TraProductService service;

    // GET /api/products
    @GetMapping
    public ResponseEntity<List<TraProduct>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<TraProduct> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<TraProduct> create(@RequestBody TraProduct product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(product));
    }

    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<TraProduct> update(@PathVariable Integer id,
                                             @RequestBody TraProduct product) {
        return ResponseEntity.ok(service.update(id, product));
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công sản phẩm id=" + id));
    }

    // GET /api/products/featured
    @GetMapping("/featured")
    public ResponseEntity<List<TraProduct>> getFeatured() {
        return ResponseEntity.ok(service.findFeatured());
    }

    // GET /api/products/low-stock
    @GetMapping("/low-stock")
    public ResponseEntity<List<TraProduct>> getLowStock() {
        return ResponseEntity.ok(service.findLowStock());
    }

    // GET /api/products/search?q=keyword
    @GetMapping("/search")
    public ResponseEntity<List<TraProduct>> search(@RequestParam("q") String keyword) {
        return ResponseEntity.ok(service.search(keyword));
    }

    // GET /api/products/status/active
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TraProduct>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    // GET /api/products/category/3
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TraProduct>> getByCategory(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(service.findByCategory(categoryId));
    }
}

