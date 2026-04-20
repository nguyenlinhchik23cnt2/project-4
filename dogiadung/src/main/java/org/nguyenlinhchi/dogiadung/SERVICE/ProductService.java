package org.nguyenlinhchi.dogiadung.SERVICE;



import org.nguyenlinhchi.dogiadung.ENTITY.Product;
import org.nguyenlinhchi.dogiadung.REPOSITORY.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Product add(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return repository.save(product);
    }

    public Product update(Integer id, Product product) {
        Optional<Product> existing = repository.findById(id);
        if (existing.isPresent()) {
            Product p = existing.get();
            p.setCategory(product.getCategory());
            p.setName(product.getName());
            p.setUnit(product.getUnit());
            p.setPrice(product.getPrice());
            p.setStockQty(product.getStockQty());
            p.setMinStock(product.getMinStock());
            p.setDescription(product.getDescription());
            p.setImageUrl(product.getImageUrl());
            p.setStatus(product.getStatus());
            p.setUpdatedAt(LocalDateTime.now());
            return repository.save(p);
        }
        return null;
    }

    public String delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Xóa thành công!";
        }
        return "Không tìm thấy sản phẩm!";
    }
}