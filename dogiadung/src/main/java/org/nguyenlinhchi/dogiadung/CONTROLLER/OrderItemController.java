package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.DTO.OrderItemResponse;
import org.nguyenlinhchi.dogiadung.ENTITY.OrderItem;
import org.nguyenlinhchi.dogiadung.ENTITY.OrderItemId;
import org.nguyenlinhchi.dogiadung.SERVICE.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService service;

    public OrderItemController(OrderItemService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderItemResponse> create(@RequestBody OrderItemResponse request) {
        OrderItem item = service.createFromRequest(request);
        return ResponseEntity.ok(toResponse(item));
    }

    @GetMapping
    public List<OrderItemResponse> getAll() {
        return service.getAll().stream()
                .map(this::toResponse)
                .toList();
    }

    // ==================== GET BY ID ====================
    @GetMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderItemResponse> getById(
            @PathVariable("orderId") String orderIdStr,
            @PathVariable("productId") String productIdStr) {

        Integer orderId = Integer.parseInt(orderIdStr);
        Integer productId = Integer.parseInt(productIdStr);

        OrderItemId id = new OrderItemId(orderId, productId);
        return service.getById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== UPDATE ====================
    @PutMapping("/{orderId}/{productId}")
    public ResponseEntity<OrderItemResponse> update(
            @PathVariable("orderId") String orderIdStr,
            @PathVariable("productId") String productIdStr,
            @RequestBody OrderItemResponse request) {

        Integer orderId = Integer.parseInt(orderIdStr);
        Integer productId = Integer.parseInt(productIdStr);

        OrderItemId id = new OrderItemId(orderId, productId);
        OrderItem updated = service.updateFromRequest(id, request);
        return ResponseEntity.ok(toResponse(updated));
    }

    // ==================== DELETE ====================
    @DeleteMapping("/{orderId}/{productId}")
    public ResponseEntity<String> delete(
            @PathVariable("orderId") String orderIdStr,
            @PathVariable("productId") String productIdStr) {

        Integer orderId = Integer.parseInt(orderIdStr);
        Integer productId = Integer.parseInt(productIdStr);

        OrderItemId id = new OrderItemId(orderId, productId);
        service.delete(id);
        return ResponseEntity.ok("OrderItem deleted successfully");
    }

    // ================== CONVERTER ==================
    private OrderItemResponse toResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getOrderId(),
                item.getProductId(),
                item.getProduct() != null ? item.getProduct().getName() : null,
                item.getQuantity(),
                item.getUnitPrice()
        );
    }
}