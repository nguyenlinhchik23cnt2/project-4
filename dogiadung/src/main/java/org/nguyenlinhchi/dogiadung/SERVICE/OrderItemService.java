package org.nguyenlinhchi.dogiadung.SERVICE;

import jakarta.persistence.criteria.Order;
import org.nguyenlinhchi.dogiadung.DTO.OrderItemResponse;
import org.nguyenlinhchi.dogiadung.ENTITY.OrderItem;
import org.nguyenlinhchi.dogiadung.ENTITY.OrderItemId;
import org.nguyenlinhchi.dogiadung.ENTITY.TraProduct;
import org.nguyenlinhchi.dogiadung.REPOSITORY.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository repository;

    @Autowired
    private Tra_orderService tra_orderService;

    @Autowired
    private tra_productService tra_productService;

    // ==================== CREATE ====================
    @Transactional
    public OrderItem createFromRequest(OrderItemResponse req) {
        OrderItem item = new OrderItem();

        // Lấy Order và Product
        Order order = tra_orderService.getById(req.getOrderId());
        if (order == null) {
            throw new RuntimeException("Order not found with id: " + req.getOrderId());
        }
        item.setOrder(order);

        TraProduct product = tra_productService.getById(req.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + req.getProductId());
        }
        item.setProduct(product);

        item.setQuantity(req.getQuantity());
        item.setUnitPrice(req.getUnitPrice());

        return repository.save(item);
    }

    // ==================== READ ====================
    public List<OrderItem> getAll() {
        return repository.findAll();
    }

    public Optional<OrderItem> getById(OrderItemId id) {        // ← SỬA Ở ĐÂY
        return repository.findById(id);
    }

    // ==================== UPDATE ====================
    @Transactional
    public OrderItem updateFromRequest(OrderItemId id, OrderItemResponse req) {   // ← SỬA Ở ĐÂY
        return repository.findById(id).map(item -> {

            Order order = orderService.getById(req.getOrderId());
            if (order == null) {
                throw new RuntimeException("Order not found with id: " + req.getOrderId());
            }
            item.setOrder(order);

            Product product = productService.getById(req.getProductId());
            if (product == null) {
                throw new RuntimeException("Product not found with id: " + req.getProductId());
            }
            item.setProduct(product);

            item.setQuantity(req.getQuantity());
            item.setUnitPrice(req.getUnitPrice());

            return repository.save(item);
        }).orElseThrow(() -> new RuntimeException("OrderItem not found with id: " + id));
    }

    // ==================== DELETE ====================
    @Transactional
    public void delete(OrderItemId id) {        // ← SỬA Ở ĐÂY
        if (!repository.existsById(id)) {
            throw new RuntimeException("OrderItem not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public Tra_orderService getTra_orderService() {
        return tra_orderService;
    }

    public void setTra_orderService(Tra_orderService tra_orderService) {
        this.tra_orderService = tra_orderService;
    }

    public tra_productService getTraProductService() {
        return traProductService;
    }

    public void setTraProductService(tra_productService traProductService) {
        this.traProductService = traProductService;
    }

    public tra_productService getTra_productService() {
        return tra_productService;
    }

    public void setTra_productService(tra_productService tra_productService) {
        this.tra_productService = tra_productService;
    }
}