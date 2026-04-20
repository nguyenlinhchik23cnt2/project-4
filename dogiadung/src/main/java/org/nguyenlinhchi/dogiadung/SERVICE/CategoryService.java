package org.nguyenlinhchi.dogiadung.SERVICE;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.nguyenlinhchi.dogiadung.ENTITY.Category;
import org.nguyenlinhchi.dogiadung.REPOSITORY.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repo;

    public List<Category> getAll() {
        return repo.findAll();
    }

    public Category getById(int id) {
        return repo.findById(id).orElse(null);
    }

    public Category add(Category c) {
        return repo.save(c);
    }

    public Category update(int id, Category c) {
        Category old = getById(id);
        if (old != null) {
            old.setName(c.getName());
            old.setDescription(c.getDescription());
            old.setStatus(c.getStatus());
            return repo.save(old);
        }
        return null;
    }

    public String delete(int id) {
        if (!repo.existsById(id)) {
            return "❌ Không tồn tại ID";
        }

        try {
            repo.deleteById(id);
            return "✅ Xóa thành công";
        } catch (Exception e) {
            return "❌ Không thể xóa (có dữ liệu liên quan)";
        }
    }
}