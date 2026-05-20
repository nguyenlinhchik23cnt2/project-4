package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.DTO.ProductDTO;
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraProductRepository;
import org.nguyenlinhchi.dogiadung.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Tra_productService {

    private final TraProductRepository repo;
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

        return repo.save(existing);
    }

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