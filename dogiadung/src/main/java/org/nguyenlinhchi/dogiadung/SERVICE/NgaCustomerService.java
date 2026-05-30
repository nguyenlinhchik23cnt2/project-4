package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaCustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class NgaCustomerService {

    private final NgaCustomerRepository repository;

    // Giữ nguyên Dependency Injection qua Constructor chuẩn của bạn
    public NgaCustomerService(NgaCustomerRepository repository) {
        this.repository = repository;
    }

    // 3. LẤY THEO ID (READ BY ID)
    public NgaCustomer getById(Integer id) {
        return repository.findById(id).orElse(null);
    }
    // 4. CẬP NHẬT (UPDATE)
    public NgaCustomer update(Integer id, NgaCustomer newData) throws Exception {
        return repository.findById(id).map(customer -> {
            customer.setFullName(newData.getFullName());
            customer.setPhone(newData.getPhone());
            customer.setEmail(newData.getEmail());
            customer.setAddress(newData.getAddress());
            customer.setDob(newData.getDob());
            customer.setGender(newData.getGender());
            // Giữ nguyên username và ngày tạo (createdAt) gốc
            return repository.save(customer);
        }).orElseThrow(() -> new Exception("Không tìm thấy khách hàng có ID: " + id));
    }

}