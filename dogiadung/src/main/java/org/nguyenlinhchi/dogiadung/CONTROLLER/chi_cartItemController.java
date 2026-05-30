//package org.nguyenlinhchi.dogiadung.CONTROLLER;
//
//import org.nguyenlinhchi.dogiadung.ENTITY.chi_cartItem;
//import org.nguyenlinhchi.dogiadung.SERVICE.chi_cartItemService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/cart")
//@CrossOrigin(origins = "*")
//public class chi_cartItemController {
//
//    private final chi_cartItemService cartItemService;
//
//    public chi_cartItemController(chi_cartItemService cartItemService) {
//        this.cartItemService = cartItemService;
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<?> addToCart(
//            @RequestParam(required = false) Integer customerId,
//            @RequestParam(required = false) Integer productId,
//            @RequestParam(defaultValue = "1") Integer quantity) {
//
//        // Kiểm tra tham số
//        if (customerId == null || productId == null) {
//            return ResponseEntity.badRequest()
//                    .body(" Thiếu customerId hoặc productId. Vui lòng nhập đầy đủ!");
//        }
//
//        if (customerId <= 0 || productId <= 0) {
//            return ResponseEntity.badRequest().body(" Customer ID và Product ID phải lớn hơn 0!");
//        }
//
//        try {
//            System.out.println("=== ADD TO CART ===");
//            System.out.println("customerId = " + customerId);
//            System.out.println("productId  = " + productId);
//            System.out.println("quantity   = " + quantity);
//
//            chi_cartItem item = cartItemService.addToCart(customerId, productId, quantity);
//            System.out.println(" Thêm thành công! ID = " + item.getId());
//
//            return ResponseEntity.ok(item);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError()
//                    .body("Lỗi server: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/{customerId}")
//    public ResponseEntity<List<chi_cartItem>> getCart(@PathVariable Integer customerId) {
//        return ResponseEntity.ok(cartItemService.getCartByCustomerId(customerId));
//    }
//
//    @DeleteMapping("/remove")
//    public ResponseEntity<Void> removeFromCart(@RequestParam Integer customerId,
//                                               @RequestParam Integer productId) {
//        cartItemService.removeFromCart(customerId, productId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/update")
//    public ResponseEntity<chi_cartItem> updateQuantity(@RequestParam Integer customerId,
//                                                       @RequestParam Integer productId,
//                                                       @RequestParam Integer quantity) {
//        chi_cartItem item = cartItemService.updateQuantity(customerId, productId, quantity);
//        return ResponseEntity.ok(item);
//    }
//
//    @DeleteMapping("/clear/{customerId}")
//    public ResponseEntity<Void> clearCart(@PathVariable Integer customerId) {
//        cartItemService.clearCart(customerId);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/all")
//    public ResponseEntity<List<chi_cartItem>> getAllCarts() {
//        List<chi_cartItem> allCarts = cartItemService.getAllCarts();
//        return ResponseEntity.ok(allCarts);
//    }
//}

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
    /**
     * Cập nhật / Thêm mới sản phẩm vào giỏ hàng (hỗ trợ cả 2 trường hợp)
     */
    @PutMapping("/update")
    public ResponseEntity<?> updateCart(@RequestBody UpdateCartRequest request) {
        try {
            System.out.println("=== UPDATE REQUEST RECEIVED ===");
            System.out.println("Raw Request Body: customerId=" + request.getCustomerId()
                    + ", productId=" + request.getProductId()
                    + ", quantity=" + request.getQuantity());

            CartItemResponse response = cartService.updateQuantity(
                    request.getCustomerId(),
                    request.getProductId(),
                    request.getQuantity()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("LỖI 400: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
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

    /**
     * Đếm tổng số lượng sản phẩm trong giỏ hàng (dùng cho badge icon)
     */
    @GetMapping("/count/{customerId}")
    public ResponseEntity<Integer> getCartCount(@PathVariable Integer customerId) {
        if (customerId == null || customerId <= 0) {
            return ResponseEntity.badRequest().build();
        }
        List<CartItemResponse> cart = cartService.getCartByCustomerId(customerId);
        int totalQuantity = cart.stream()
                .mapToInt(item -> item.getQuantity() != null ? item.getQuantity() : 0)
                .sum();
        return ResponseEntity.ok(totalQuantity);
    }

    // ==================== KHÔNG CUNG CẤP API CHO ADMIN/STAFF ====================
    // Không có endpoint /all hoặc lấy giỏ theo user khác
}