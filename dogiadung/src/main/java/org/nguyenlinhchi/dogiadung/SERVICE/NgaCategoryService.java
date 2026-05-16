package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCategory;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NgaCategoryService {

    private final NgaCategoryRepository repository;

    public NgaCategoryService(NgaCategoryRepository repository) {
        this.repository = repository;
    }

    // CREATE
    public NgaCategory create(NgaCategory category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(new Date());
        }
        if (category.getStatus() == null || category.getStatus().isEmpty()) {
            category.setStatus("active");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        return repository.save(category);
    }

    // READ
    public List<NgaCategory> getAll() {
        return repository.findAll();
    }

    public List<NgaCategory> getRootCategories() {
        return repository.findByParentIsNull();
    }

    public NgaCategory getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public NgaCategory update(Integer id, NgaCategory newData) {
        return repository.findById(id).map(category -> {
            category.setName(newData.getName());
            category.setDescription(newData.getDescription());
            category.setImageUrl(newData.getImageUrl());
            category.setSortOrder(newData.getSortOrder());
            category.setStatus(newData.getStatus());
            category.setParent(newData.getParent());
            // Không cho sửa createdAt
            return repository.save(category);
        }).orElse(null);
    }

    // DELETE
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}