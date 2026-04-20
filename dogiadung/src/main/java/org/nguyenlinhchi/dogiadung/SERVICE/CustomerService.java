package org.nguyenlinhchi.dogiadung.SERVICE;



import org.nguyenlinhchi.dogiadung.ENTITY.Customer;
import org.nguyenlinhchi.dogiadung.REPOSITORY.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Customer getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Customer add(Customer customer) {
        return repository.save(customer);
    }

    public Customer update(Integer id, Customer customer) {
        if (!repository.existsById(id)) {
            return null;
        }
        customer.setCustomerId(id);
        return repository.save(customer);
    }

    public String delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Xóa khách hàng thành công!";
        }
        return "Khách hàng không tồn tại!";
    }
}