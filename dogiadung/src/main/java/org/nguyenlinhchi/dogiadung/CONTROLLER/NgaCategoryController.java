package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCategory;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class NgaCategoryController {

    private final NgaCategoryService categoryService;
    public NgaCategoryController(NgaCategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping
    public ResponseEntity<List<NgaCategory>> getAllAndSearch(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status) {
        List<NgaCategory> list = categoryService.searchAndFilter(keyword, status);
        return ResponseEntity.ok(list);
    }

    // Lấy chi tiết 1 danh mục: GET http://localhost:8080/api/categories/{id}
    @GetMapping("/{id}")
    public ResponseEntity<NgaCategory> getById(@PathVariable Integer id) {
        NgaCategory category = categoryService.getById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    // Thêm mới danh mục: POST http://localhost:8080/api/categories
    @PostMapping
    public ResponseEntity<NgaCategory> create(@RequestBody NgaCategory category) {
        return ResponseEntity.ok(categoryService.create(category));
    }

    // Sửa danh mục: PUT http://localhost:8080/api/categories/{id}
    @PutMapping("/{id}")
    public ResponseEntity<NgaCategory> update(@PathVariable Integer id, @RequestBody NgaCategory category) {
        NgaCategory updated = categoryService.update(id, category);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // Xóa danh mục: DELETE http://localhost:8080/api/categories/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Map<String, Object> res = new HashMap<>();
        if (categoryService.delete(id)) {
            res.put("success", true);
            res.put("message", "Xóa danh mục thành công!");
            return ResponseEntity.ok(res);
        }
        res.put("success", false);
        res.put("message", "Không tìm thấy danh mục để xóa!");
        return ResponseEntity.badRequest().body(res);
    }
}