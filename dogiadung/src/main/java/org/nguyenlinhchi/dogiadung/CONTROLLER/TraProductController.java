package org.nguyenlinhchi.dogiadung.CONTROLLER;

import lombok.RequiredArgsConstructor;
import org.nguyenlinhchi.dogiadung.DTO.ProductDTO;
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.SERVICE.Tra_productService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TraProductController {

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