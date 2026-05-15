package org.nguyenlinhchi.dogiadung.SERVICE;


import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraProductRepository;
import org.nguyenlinhchi.dogiadung.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Tra_productService {

    private final TraProductRepository repo;

    // ───── READ ─────────────────────────────────────────────────

    public List<TraProduct> findAll() {
        return repo.findAll();
    }

    public TraProduct findById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy sản phẩm với id=" + id));
    }

    public List<TraProduct> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    public List<TraProduct> findFeatured() {
        return repo.findByIsFeaturedTrue();
    }

    public List<TraProduct> findByCategory(Integer categoryId) {
        return repo.findByCategoryId(categoryId);
    }

    public List<TraProduct> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    public List<TraProduct> findLowStock() {
        return repo.findLowStockProducts();
    }

    // ───── CREATE ───────────────────────────────────────────────

    @Transactional
    public TraProduct create(TraProduct product) {
        log.info("Tạo sản phẩm mới: {}", product.getName());
        product.setProductId(null); // đảm bảo ID do DB tự sinh
        return repo.save(product);
    }

    // ───── UPDATE ───────────────────────────────────────────────

    @Transactional
    public TraProduct update(Integer id, TraProduct incoming) {
        log.info("Cập nhật sản phẩm id={}", id);
        TraProduct existing = findById(id);

        if (incoming.getCategoryId()  != null) existing.setCategoryId(incoming.getCategoryId());
        if (incoming.getName()        != null) existing.setName(incoming.getName());
        if (incoming.getUnit()        != null) existing.setUnit(incoming.getUnit());
        if (incoming.getPrice()       != null) existing.setPrice(incoming.getPrice());
        if (incoming.getStockQty()    != null) existing.setStockQty(incoming.getStockQty());
        if (incoming.getMinStock()    != null) existing.setMinStock(incoming.getMinStock());
        if (incoming.getSoldQty()     != null) existing.setSoldQty(incoming.getSoldQty());
        if (incoming.getIsFeatured()  != null) existing.setIsFeatured(incoming.getIsFeatured());
        if (incoming.getWeightKg()    != null) existing.setWeightKg(incoming.getWeightKg());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        if (incoming.getImageUrl()    != null) existing.setImageUrl(incoming.getImageUrl());
        if (incoming.getStatus()      != null) existing.setStatus(incoming.getStatus());
        if (incoming.getSuppName()    != null) existing.setSuppName(incoming.getSuppName());
        if (incoming.getSuppContact() != null) existing.setSuppContact(incoming.getSuppContact());
        if (incoming.getSuppPhone()   != null) existing.setSuppPhone(incoming.getSuppPhone());
        if (incoming.getSuppEmail()   != null) existing.setSuppEmail(incoming.getSuppEmail());
        if (incoming.getSuppStatus()  != null) existing.setSuppStatus(incoming.getSuppStatus());

        return repo.save(existing);
    }

    // ───── DELETE ───────────────────────────────────────────────

    @Transactional
    public void delete(Integer id) {
        log.info("Xóa sản phẩm id={}", id);
        findById(id); // ném 404 nếu không tồn tại
        repo.deleteById(id);
    }
}
