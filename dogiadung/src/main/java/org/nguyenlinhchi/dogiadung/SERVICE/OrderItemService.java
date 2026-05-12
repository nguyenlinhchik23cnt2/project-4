package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.DTO.OrderItemResponse;
import org.nguyenlinhchi.dogiadung.ENTITY.OrderItem;
import org.nguyenlinhchi.dogiadung.ENTITY.OrderItemId;
import org.nguyenlinhchi.dogiadung.ENTITY.TraOrder;
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
    private TraOrderService traorderService;

    @Autowired
    private TraProductService traproductService;

    // ==================== CREATE ====================
    @Transactional
    public OrderItem createFromRequest(OrderItemResponse req) {
        OrderItem item = new OrderItem();

        // Lấy Order và Product
        TraOrder order = traorderService.findById(req.getOrderId());
        if (order == null) {
            throw new RuntimeException("Order not found with id: " + req.getOrderId());
        }
        item.setOrder(order);

        TraProduct product = traproductService.findById(req.getProductId());
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

            TraOrder order = TraOrderService.findById(req.getOrderId());
            if (order == null) {
                throw new RuntimeException("Order not found with id: " + req.getOrderId());
            }
            item.setOrder(order);

            TraProduct product = TraProductService.findById(req.getProductId());
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

    public TraOrderService  getTraorderService() {
        return traorderService;
    }

    public void setTra_orderService(TraOrderService traorderService) {
        this.traorderService = traorderService;
    }

    public TraProductService getTraProductService() {
        return traproductService;
    }

    public void setTraProductService(TraProductService traProductService) {
        this.traproductService = traProductService;
    }

    public TraProductService getTra_productService() {
        return traproductService;
    }

    public void setTraproductService(traproductService traproductService) {
        this.traproductService = traproductService;
    }
}