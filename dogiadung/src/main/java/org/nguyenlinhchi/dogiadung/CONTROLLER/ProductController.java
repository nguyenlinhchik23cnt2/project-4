package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.Category;
import org.nguyenlinhchi.dogiadung.ENTITY.Product;
import org.nguyenlinhchi.dogiadung.SERVICE.FileStorageService;
import org.nguyenlinhchi.dogiadung.SERVICE.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService service;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Integer id) {
        Product product = service.getById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // ==================== THÊM SẢN PHẨM (Upload ảnh) ====================
    @PostMapping
    public ResponseEntity<Product> create(
            @RequestParam("name") String name,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("unit") String unit,
            @RequestParam("price") Double price,
            @RequestParam("stockQty") Integer stockQty,
            @RequestParam(value = "minStock", defaultValue = "10") Integer minStock,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", defaultValue = "active") String status,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Product product = new Product();
            product.setName(name);
            product.setUnit(unit);
            product.setPrice(BigDecimal.valueOf(price));
            product.setStockQty(stockQty);
            product.setMinStock(minStock);
            product.setDescription(description);
            product.setStatus(status);

            // Liên kết với Category
            Category category = new Category();
            category.setCategoryId(categoryId);
            product.setCategory(category);

            // Upload ảnh nếu có
            if (image != null && !image.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(image);
                product.setImageUrl(imageUrl);
            }

            Product savedProduct = service.add(product);
            return ResponseEntity.ok(savedProduct);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ==================== CẬP NHẬT SẢN PHẨM ====================
    // ==================== CẬP NHẬT SẢN PHẨM (Hỗ trợ đổi ảnh) ====================
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable Integer id,
            @RequestParam("name") String name,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("unit") String unit,
            @RequestParam("price") Double price,
            @RequestParam("stockQty") Integer stockQty,
            @RequestParam(value = "minStock", defaultValue = "10") Integer minStock,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", defaultValue = "active") String status,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Product product = service.getById(id);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }

            // Cập nhật thông tin
            product.setName(name);
            product.setUnit(unit);
            product.setPrice(BigDecimal.valueOf(price));
            product.setStockQty(stockQty);
            product.setMinStock(minStock);
            product.setDescription(description);
            product.setStatus(status);

            // Cập nhật category
            Category category = new Category();
            category.setCategoryId(categoryId);
            product.setCategory(category);

            // Nếu có ảnh mới thì upload và thay thế
            if (image != null && !image.isEmpty()) {
                String imageUrl = fileStorageService.storeFile(image);
                product.setImageUrl(imageUrl);
            }

            Product updated = service.update(id, product);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    // ==================== XÓA SẢN PHẨM ====================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        String message = service.delete(id);
        return ResponseEntity.ok(message);
    }
}