package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.DTO.CartItemResponse;
import org.nguyenlinhchi.dogiadung.ENTITY.TraOrder;
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_cartItem;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItem;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraOrderRepository;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraProductRepository;
import org.nguyenlinhchi.dogiadung.REPOSITORY.chi_cartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class chi_cartItemService {

    private final chi_cartItemRepository cartRepository;
    private final TraProductRepository productRepository;

    public chi_cartItemService(chi_cartItemRepository cartRepository,
                               TraProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CartItemResponse addToCart(Integer customerId, Integer productId, Integer quantity) {
        if (customerId == null || productId == null || quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }

        // Kiểm tra sản phẩm tồn tại và đang active
        TraProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

        if (!"active".equals(product.getStatus())) {
            throw new IllegalArgumentException("Sản phẩm hiện không khả dụng");
        }

        // Kiểm tra stock
        if (product.getStockQty() < quantity) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ");
        }

        chi_cartItem existing = cartRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            chi_cartItem saved = cartRepository.save(existing);
            return mapToResponse(saved, product);
        } else {
            chi_cartItem newItem = new chi_cartItem(customerId, productId, quantity);
            chi_cartItem saved = cartRepository.save(newItem);
            return mapToResponse(saved, product);
        }
    }

    public List<CartItemResponse> getCartByCustomerId(Integer customerId) {
        List<chi_cartItem> items = cartRepository.findByCustomerId(customerId);
        return items.stream()
                .map(item -> {
                    TraProduct product = productRepository.findById(item.getProductId()).orElse(null);
                    return mapToResponse(item, product);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public CartItemResponse updateQuantity(Integer customerId, Integer productId, Integer newQuantity) {
        if (newQuantity == null || newQuantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }

        chi_cartItem item = cartRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không có trong giỏ hàng"));

        TraProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

        if (product.getStockQty() < newQuantity) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ");
        }

        item.setQuantity(newQuantity);
        chi_cartItem saved = cartRepository.save(item);
        return mapToResponse(saved, product);
    }

    @Transactional
    public void removeFromCart(Integer customerId, Integer productId) {
        cartRepository.deleteByCustomerIdAndProductId(customerId, productId);
    }

    @Transactional
    public void clearCart(Integer customerId) {
        cartRepository.deleteAllByCustomerId(customerId);
    }

    // Helper method
    private CartItemResponse mapToResponse(chi_cartItem item, TraProduct product) {
        BigDecimal subtotal = product != null
                ? product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                : BigDecimal.ZERO;

        return new CartItemResponse(
                item.getId(),
                item.getProductId(),
                product != null ? product.getName() : "Unknown",
                product != null ? product.getPrice() : BigDecimal.ZERO,
                product != null ? product.getImageUrl() : null,
                item.getQuantity(),
                subtotal
        );
    }
}
