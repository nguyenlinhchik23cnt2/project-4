package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaCustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NgaCustomerService {

    private final NgaCustomerRepository repository;

    // Giữ nguyên Dependency Injection qua Constructor chuẩn của bạn
    public NgaCustomerService(NgaCustomerRepository repository) {
        this.repository = repository;
    }

    // 1. THÊM MỚI (CREATE) - Có kiểm tra ràng buộc dữ liệu
    public NgaCustomer create(NgaCustomer customer) throws Exception {
        if (repository.existsByUsername(customer.getUsername())) {
            throw new Exception("Tên đăng nhập (Username) của khách hàng đã tồn tại!");
        }
        if (customer.getEmail() != null && !customer.getEmail().isEmpty() && repository.existsByEmail(customer.getEmail())) {
            throw new Exception("Địa chỉ Email khách hàng này đã được sử dụng!");
        }
        if (customer.getCreatedAt() == null) {
            customer.setCreatedAt(new Date()); // Tự động lưu ngày tạo nếu trống
        }
        return repository.save(customer);
    }

    // 2. LẤY TOÀN BỘ DANH SÁCH (READ ALL)
    public List<NgaCustomer> getAll() {
        return repository.findAll();
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

    // 5. XÓA KHÁCH HÀNG (DELETE) - Có phản hồi kết quả trực quan
    public boolean delete(Integer id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // 6. TÌM KIẾM VÀ LỌC KHÁCH HÀNG (SEARCH & FILTER)
    public List<NgaCustomer> searchAndFilter(String keyword, String gender) {
        // Chuẩn hóa chuỗi rỗng về null để JPA bỏ qua điều kiện lọc trống
        String searchKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        String searchGender = (gender != null && !gender.trim().isEmpty()) ? gender.trim() : null;

        return repository.searchAndFilterCustomers(searchKeyword, searchGender);
    }
}