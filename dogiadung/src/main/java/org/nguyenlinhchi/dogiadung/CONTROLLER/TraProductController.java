package org.nguyenlinhchi.dogiadung.CONTROLLER;

import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import org.nguyenlinhchi.dogiadung.DTO.ProductDTO;
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.SERVICE.Tra_productService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
=======
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.SERVICE.TraProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TraProductController {

<<<<<<< HEAD
    private final Tra_productService service;

    @GetMapping
    public List<ProductDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @GetMapping("/search")
    public List<ProductDTO> search(@RequestParam("q") String keyword) {
        return service.search(keyword);
    }

    // CREATE
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> create(
            @RequestParam("name") String name,
            @RequestParam("unit") String unit,
            @RequestParam("price") String price,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "stockQty", defaultValue = "0") Integer stockQty,
            @RequestParam(value = "minStock", defaultValue = "10") Integer minStock,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", defaultValue = "active") String status,
            @RequestParam(value = "isFeatured", defaultValue = "false") Boolean isFeatured,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        TraProduct product = new TraProduct();
        product.setName(name);
        product.setUnit(unit);
        product.setPrice(new java.math.BigDecimal(price));
        product.setCategoryId(categoryId);
        product.setStockQty(stockQty);
        product.setMinStock(minStock);
        product.setDescription(description);
        product.setStatus(status);
        product.setIsFeatured(isFeatured);

        TraProduct savedEntity = service.create(product, imageFile);
        return new ResponseEntity<>(service.convertToDTO(savedEntity), HttpStatus.CREATED);
    }

    // UPDATE
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductDTO> update(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("unit") String unit,
            @RequestParam("price") String price,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam(value = "stockQty", defaultValue = "0") Integer stockQty,
            @RequestParam(value = "minStock", defaultValue = "10") Integer minStock,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", defaultValue = "active") String status,
            @RequestParam(value = "isFeatured", defaultValue = "false") Boolean isFeatured,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        TraProduct product = new TraProduct();
        product.setName(name);
        product.setUnit(unit);
        product.setPrice(new java.math.BigDecimal(price));
        product.setCategoryId(categoryId);
        product.setStockQty(stockQty);
        product.setMinStock(minStock);
        product.setDescription(description);
        product.setStatus(status);
        product.setIsFeatured(isFeatured);

        TraProduct updatedEntity = service.update(id, product, imageFile);
        return ResponseEntity.ok(service.convertToDTO(updatedEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
=======
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

>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
