package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCategory;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NgaCategoryService {

    private final NgaCategoryRepository repository;

    // Giữ nguyên Dependency Injection qua Constructor chuẩn của bạn
    public NgaCategoryService(NgaCategoryRepository repository) {
        this.repository = repository;
    }

    // 1. THÊM MỚI (CREATE)
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

    // 2. LẤY TOÀN BỘ (READ ALL)
    public List<NgaCategory> getAll() {
        return repository.findAll();
    }

    // 3. LẤY DANH MỤC GỐC
    public List<NgaCategory> getRootCategories() {
        return repository.findByParentIsNull();
    }

    // 4. LẤY THEO ID (READ BY ID)
    public NgaCategory getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // 5. CẬP NHẬT (UPDATE)
    public NgaCategory update(Integer id, NgaCategory newData) {
        return repository.findById(id).map(category -> {
            category.setName(newData.getName());
            category.setDescription(newData.getDescription());
            category.setImageUrl(newData.getImageUrl());
            category.setSortOrder(newData.getSortOrder());
            category.setStatus(newData.getStatus());
            category.setParent(newData.getParent());
            // Giữ nguyên ngày tạo ban đầu không cho sửa
            return repository.save(category);
        }).orElse(null);
    }

    // 6. XÓA DANH MỤC (DELETE)
    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // 7. TÌM KIẾM VÀ LỌC DANH MỤC (SEARCH & FILTER)
    // Hỗ trợ tìm kiếm theo tên/mô tả và lọc theo trạng thái (active/inactive)
    public List<NgaCategory> searchAndFilter(String keyword, String status) {
        // Nếu chuỗi truyền vào rỗng thì chuyển thành null để Hibernate bỏ qua điều kiện lọc đó trong câu lệnh SQL
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String searchStatus = (status != null && !status.trim().isEmpty()) ? status.trim() : null;

        return repository.searchAndFilterCategories(searchKeyword, searchStatus);
    }
}