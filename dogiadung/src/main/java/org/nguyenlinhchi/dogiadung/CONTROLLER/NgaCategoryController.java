package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCategory;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")   // Cho phép tất cả frontend truy cập (dễ test)
public class NgaCategoryController {

    private final NgaCategoryService service;

    public NgaCategoryController(NgaCategoryService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<NgaCategory> create(@RequestBody NgaCategory category) {
        return ResponseEntity.ok(service.create(category));
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<NgaCategory>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // GET ROOT
    @GetMapping("/root")
    public ResponseEntity<List<NgaCategory>> getRoot() {
        return ResponseEntity.ok(service.getRootCategories());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<NgaCategory> getById(@PathVariable Integer id) {
        NgaCategory category = service.getById(id);
        return category != null ? ResponseEntity.ok(category)
                : ResponseEntity.notFound().build();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<NgaCategory> update(@PathVariable Integer id,
                                              @RequestBody NgaCategory category) {
        NgaCategory updated = service.update(id, category);
        return updated != null ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}