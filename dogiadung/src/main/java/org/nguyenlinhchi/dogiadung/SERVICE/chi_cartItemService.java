//package org.nguyenlinhchi.dogiadung.SERVICE;
//
//import org.nguyenlinhchi.dogiadung.ENTITY.chi_cartItem;
//import org.nguyenlinhchi.dogiadung.REPOSITORY.chi_cartItemRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import java.util.List;
//
//@Service
//public class chi_cartItemService {
//
//    private final chi_cartItemRepository cartRepository;
//
//    public chi_cartItemService(chi_cartItemRepository cartRepository) {
//        this.cartRepository = cartRepository;
//    }
//
//
//    @Transactional
//    public chi_cartItem addToCart(Integer customerId, Integer productId, Integer quantity) {
//        chi_cartItem existing = cartRepository.findByCustomerIdAndProductId(customerId, productId)
//                .orElse(null);
//
//        if (existing != null) {
//            existing.setQuantity(existing.getQuantity() + quantity);
//            return cartRepository.save(existing);
//        } else {
//            chi_cartItem newItem = new chi_cartItem(customerId, productId, quantity);
//            return cartRepository.save(newItem);
//        }
//    }
//    public List<chi_cartItem> getCartByCustomerId(Integer customerId) {
//        return cartRepository.findByCustomerId(customerId);
//    }
//
//    @Transactional
//    public void removeFromCart(Integer customerId, Integer productId) {
//        cartRepository.deleteByCustomerIdAndProductId(customerId, productId);
//    }
//
//    @Transactional
//    public void clearCart(Integer customerId) {
//        cartRepository.deleteAllByCustomerId(customerId);
//    }
//
//    @Transactional
//    public chi_cartItem updateQuantity(Integer customerId, Integer productId, Integer newQuantity) {
//        chi_cartItem item = cartRepository.findByCustomerIdAndProductId(customerId, productId)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));
//
//        item.setQuantity(newQuantity);
//        return cartRepository.save(item);
//    }
//    public List<chi_cartItem> getAllCarts() {
//        return cartRepository.findAll();
//    }
//}



package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.DTO.CartItemResponse;
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_cartItem;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraProductRepository;
import org.nguyenlinhchi.dogiadung.REPOSITORY.chi_cartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
            newQuantity = 1;
        }

        System.out.println(" [updateQuantity] customerId=" + customerId + ", productId=" + productId + ", newQty=" + newQuantity);

        // Tìm item trong giỏ hàng
        Optional<chi_cartItem> optionalItem = cartRepository.findByCustomerIdAndProductId(customerId, productId);

        chi_cartItem item;

        if (optionalItem.isPresent()) {
            item = optionalItem.get();
            System.out.println("Tìm thấy item → Cập nhật quantity thành " + newQuantity);
        } else {
            // FALLBACK QUAN TRỌNG: Nếu không tìm thấy thì tạo mới
            System.out.println(" Không tìm thấy item → Tạo mới trong giỏ hàng");
            return addToCart(customerId, productId, newQuantity);   // Gọi hàm addToCart
        }

        // Kiểm tra tồn kho
        TraProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại"));

        if (product.getStockQty() < newQuantity) {
            throw new IllegalArgumentException("Số lượng tồn kho không đủ");
        }

        // Cập nhật số lượng
        item.setQuantity(newQuantity);
        chi_cartItem saved = cartRepository.save(item);

        System.out.println(" Đã lưu quantity = " + saved.getQuantity());

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