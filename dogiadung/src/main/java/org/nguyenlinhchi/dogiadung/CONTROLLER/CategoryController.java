package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.nguyenlinhchi.dogiadung.ENTITY.Category;
import org.nguyenlinhchi.dogiadung.SERVICE.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")   // Cho phép frontend gọi từ port khác
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping
    public List<Category> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable int id) {
        return service.getById(id);
    }

    @PostMapping
    public Category create(@RequestBody Category c) {
        return service.add(c);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable int id, @RequestBody Category c) {
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        return service.delete(id);
    }
}