package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaCustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class NgaCustomerService {

    private final NgaCustomerRepository repository;

    public NgaCustomerService(NgaCustomerRepository repository) {
        this.repository = repository;
    }

    // LẤY THEO ID
    public NgaCustomer getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // CẬP NHẬT — bao gồm đổi username (F-USERNAME)
    public NgaCustomer update(Integer id, NgaCustomer newData) throws Exception {
        return repository.findById(id).map(customer -> {

            // ── Thông tin cá nhân ──
            customer.setFullName(newData.getFullName());
            customer.setPhone(newData.getPhone());
            customer.setEmail(newData.getEmail());
            customer.setAddress(newData.getAddress());
            customer.setDob(newData.getDob());
            customer.setGender(newData.getGender());

            // ── Đổi username ──
            // Chỉ cập nhật khi: gửi lên không rỗng VÀ khác username hiện tại
            String newUsername = newData.getUsername();
            if (newUsername != null && !newUsername.isBlank()
                    && !newUsername.equals(customer.getUsername())) {

                if (repository.existsByUsername(newUsername)) {
                    throw new RuntimeException("Username '" + newUsername + "' đã được sử dụng.");
                }
                customer.setUsername(newUsername);
            }

            // Giữ nguyên: password, createdAt, role
            return repository.save(customer);

        }).orElseThrow(() -> new Exception("Không tìm thấy khách hàng có ID: " + id));
    }

    // KIỂM TRA USERNAME CÓ SẴN KHÔNG (dùng cho profile.html debounce check)
    public boolean isUsernameAvailable(String username) {
        return !repository.existsByUsername(username);
    }
}