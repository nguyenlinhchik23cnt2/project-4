package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_cartItem;
import org.nguyenlinhchi.dogiadung.REPOSITORY.chi_cartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class chi_cartItemService {

    private final chi_cartItemRepository cartRepository;

    public chi_cartItemService(chi_cartItemRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    @Transactional
    public chi_cartItem addToCart(Integer customerId, Integer productId, Integer quantity) {
        chi_cartItem existing = cartRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
            return cartRepository.save(existing);
        } else {
            chi_cartItem newItem = new chi_cartItem(customerId, productId, quantity);
            return cartRepository.save(newItem);
        }
    }
    public List<chi_cartItem> getCartByCustomerId(Integer customerId) {
        return cartRepository.findByCustomerId(customerId);
    }

    @Transactional
    public void removeFromCart(Integer customerId, Integer productId) {
        cartRepository.deleteByCustomerIdAndProductId(customerId, productId);
    }

    @Transactional
    public void clearCart(Integer customerId) {
        cartRepository.deleteAllByCustomerId(customerId);
    }

    @Transactional
    public chi_cartItem updateQuantity(Integer customerId, Integer productId, Integer newQuantity) {
        chi_cartItem item = cartRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng"));

        item.setQuantity(newQuantity);
        return cartRepository.save(item);
    }
    public List<chi_cartItem> getAllCarts() {
        return cartRepository.findAll();
    }
}