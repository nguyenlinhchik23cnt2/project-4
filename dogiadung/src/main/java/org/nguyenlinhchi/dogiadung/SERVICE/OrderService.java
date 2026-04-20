package org.nguyenlinhchi.dogiadung.SERVICE;



import org.nguyenlinhchi.dogiadung.ENTITY.Order;
import org.nguyenlinhchi.dogiadung.REPOSITORY.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public List<Order> getAll() {
        return repository.findAll();
    }

    public Order getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Order add(Order order) {
        return repository.save(order);
    }

    public Order update(Integer id, Order order) {
        if (!repository.existsById(id)) {
            return null;
        }
        order.setOrderId(id);
        return repository.save(order);
    }

    public String delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Xóa đơn hàng thành công!";
        }
        return "Đơn hàng không tồn tại!";
    }
}