package org.nguyenlinhchi.dogiadung.SERVICE;

<<<<<<< HEAD

=======
import org.nguyenlinhchi.dogiadung.DTO.ProductDTO;
>>>>>>> main
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraProductRepository;
import org.nguyenlinhchi.dogiadung.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
<<<<<<< HEAD
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
=======
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
>>>>>>> main

@Service
@RequiredArgsConstructor
@Slf4j
public class Tra_productService {

    private final TraProductRepository repo;
<<<<<<< HEAD

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
=======
    private final FileUploadService fileUploadService;

    // ==================== READ ====================
    public List<ProductDTO> findAll() {
        return repo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Integer id) {
        TraProduct product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id=" + id));
        return convertToDTO(product);
    }

    public List<ProductDTO> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ==================== PUBLIC CONVERT METHOD ====================
    public ProductDTO convertToDTO(TraProduct product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .categoryId(product.getCategoryId())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .name(product.getName())
                .unit(product.getUnit())
                .price(product.getPrice())
                .stockQty(product.getStockQty())
                .minStock(product.getMinStock())
                .soldQty(product.getSoldQty())
                .isFeatured(product.getIsFeatured())
                .weightKg(product.getWeightKg())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .suppName(product.getSuppName())
                .suppContact(product.getSuppContact())
                .suppPhone(product.getSuppPhone())
                .suppEmail(product.getSuppEmail())
                .suppStatus(product.getSuppStatus())
                .build();
    }

    // ==================== CREATE ====================
    @Transactional
    public TraProduct create(TraProduct product, MultipartFile imageFile) throws IOException {
        log.info("Tạo sản phẩm mới: {} | CategoryID = {}", product.getName(), product.getCategoryId());

        if (product.getCategoryId() == null) {
            throw new IllegalArgumentException("Category ID không được để trống");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileUploadService.saveImage(imageFile);
            product.setImageUrl(imageUrl);
        }

        product.setProductId(null);
        return repo.save(product);
    }

    // ==================== UPDATE ====================
    @Transactional
    public TraProduct update(Integer id, TraProduct incoming, MultipartFile imageFile) throws IOException {
        TraProduct existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id=" + id));

        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getUnit() != null) existing.setUnit(incoming.getUnit());
        if (incoming.getPrice() != null) existing.setPrice(incoming.getPrice());
        if (incoming.getCategoryId() != null) existing.setCategoryId(incoming.getCategoryId());
        if (incoming.getStockQty() != null) existing.setStockQty(incoming.getStockQty());
        if (incoming.getMinStock() != null) existing.setMinStock(incoming.getMinStock());
        if (incoming.getIsFeatured() != null) existing.setIsFeatured(incoming.getIsFeatured());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        if (incoming.getStatus() != null) existing.setStatus(incoming.getStatus());

        if (imageFile != null && !imageFile.isEmpty()) {
            if (existing.getImageUrl() != null) {
                fileUploadService.deleteImage(existing.getImageUrl());
            }
            String newImageUrl = fileUploadService.saveImage(imageFile);
            existing.setImageUrl(newImageUrl);
        }
>>>>>>> main

        return repo.save(existing);
    }

<<<<<<< HEAD
    // ───── DELETE ───────────────────────────────────────────────

    @Transactional
    public void delete(Integer id) {
        log.info("Xóa sản phẩm id={}", id);
        findById(id); // ném 404 nếu không tồn tại
        repo.deleteById(id);
    }
}
=======
    // ==================== DELETE ====================
    @Transactional
    public void delete(Integer id) {
        TraProduct product = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm với id=" + id));

        if (product.getImageUrl() != null) {
            fileUploadService.deleteImage(product.getImageUrl());
        }
        repo.deleteById(id);
    }
}
>>>>>>> main
