package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaCustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NgaCustomerService {

    private final NgaCustomerRepository repository;

    public NgaCustomerService(NgaCustomerRepository repository) {
        this.repository = repository;
    }

    public NgaCustomer create(NgaCustomer customer) {
        return repository.save(customer);
    }

    public List<NgaCustomer> getAll() {
        return repository.findAll();
    }

    public NgaCustomer getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public NgaCustomer update(Integer id, NgaCustomer newData) {
        return repository.findById(id).map(customer -> {
            customer.setFullName(newData.getFullName());
            customer.setPhone(newData.getPhone());
            customer.setEmail(newData.getEmail());
            customer.setAddress(newData.getAddress());
            customer.setDob(newData.getDob());
            customer.setGender(newData.getGender());
            // Không cho sửa username và createdAt
            return repository.save(customer);
        }).orElse(null);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}