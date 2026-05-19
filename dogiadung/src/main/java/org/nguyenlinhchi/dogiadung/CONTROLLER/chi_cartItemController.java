package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.DTO.AddToCartRequest;
import org.nguyenlinhchi.dogiadung.DTO.CartItemResponse;
import org.nguyenlinhchi.dogiadung.DTO.UpdateCartRequest;
import org.nguyenlinhchi.dogiadung.SERVICE.chi_cartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class chi_cartItemController {

    private final chi_cartItemService cartService;

    public chi_cartItemController(chi_cartItemService cartService) {
        this.cartService = cartService;
    }

    // ==================== API DÀNH CHO KHÁCH HÀNG ====================

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        try {
            CartItemResponse response = cartService.addToCart(
                    request.getCustomerId(),
                    request.getProductId(),
                    request.getQuantity()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    /**
     * Lấy giỏ hàng của khách hàng
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<List<CartItemResponse>> getCart(@PathVariable Integer customerId) {
        if (customerId == null || customerId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        List<CartItemResponse> cart = cartService.getCartByCustomerId(customerId);
        return ResponseEntity.ok(cart);
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(@RequestBody UpdateCartRequest request) {
        try {
            CartItemResponse response = cartService.updateQuantity(
                    request.getCustomerId(),
                    request.getProductId(),
                    request.getQuantity()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Xóa một sản phẩm khỏi giỏ hàng
     */
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestParam Integer customerId,
                                               @RequestParam Integer productId) {
        cartService.removeFromCart(customerId, productId);
        return ResponseEntity.ok().build();
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.ok().build();
    }

    // ==================== KHÔNG CUNG CẤP API CHO ADMIN/STAFF ====================
    // Không có endpoint /all hoặc lấy giỏ theo user khác
}