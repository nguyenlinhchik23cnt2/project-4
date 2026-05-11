package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_cartItem;
import org.nguyenlinhchi.dogiadung.SERVICE.chi_cartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class chi_cartItemController {

    private final chi_cartItemService cartItemService;

    public chi_cartItemController(chi_cartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer productId,
            @RequestParam(defaultValue = "1") Integer quantity) {

        // Kiểm tra tham số
        if (customerId == null || productId == null) {
            return ResponseEntity.badRequest()
                    .body(" Thiếu customerId hoặc productId. Vui lòng nhập đầy đủ!");
        }

        if (customerId <= 0 || productId <= 0) {
            return ResponseEntity.badRequest().body(" Customer ID và Product ID phải lớn hơn 0!");
        }

        try {
            System.out.println("=== ADD TO CART ===");
            System.out.println("customerId = " + customerId);
            System.out.println("productId  = " + productId);
            System.out.println("quantity   = " + quantity);

            chi_cartItem item = cartItemService.addToCart(customerId, productId, quantity);
            System.out.println(" Thêm thành công! ID = " + item.getId());

            return ResponseEntity.ok(item);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Lỗi server: " + e.getMessage());
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<chi_cartItem>> getCart(@PathVariable Integer customerId) {
        return ResponseEntity.ok(cartItemService.getCartByCustomerId(customerId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestParam Integer customerId,
                                               @RequestParam Integer productId) {
        cartItemService.removeFromCart(customerId, productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update")
    public ResponseEntity<chi_cartItem> updateQuantity(@RequestParam Integer customerId,
                                                       @RequestParam Integer productId,
                                                       @RequestParam Integer quantity) {
        chi_cartItem item = cartItemService.updateQuantity(customerId, productId, quantity);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer customerId) {
        cartItemService.clearCart(customerId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<chi_cartItem>> getAllCarts() {
        List<chi_cartItem> allCarts = cartItemService.getAllCarts();
        return ResponseEntity.ok(allCarts);
    }
}