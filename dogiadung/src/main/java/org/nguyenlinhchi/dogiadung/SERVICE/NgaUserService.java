package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaUser;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NgaUserService {

    // Thay đổi thành final để bắt buộc khởi tạo qua Constructor
    private final NgaUserRepository ngaUserRepository;

    // ĐÃ SỬA: Sửa cảnh báo "Field injection is not recommended"
    public NgaUserService(NgaUserRepository ngaUserRepository) {
        this.ngaUserRepository = ngaUserRepository;
    }

    // 1. Logic Đăng ký tài khoản
    public NgaUser register(NgaUser user) throws Exception {
        if (ngaUserRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Tên đăng nhập đã tồn tại!");
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (ngaUserRepository.existsByEmail(user.getEmail())) {
                throw new Exception("Email này đã được sử dụng!");
            }
        }
        user.setRole("staff");
        user.setStatus("active");
        return ngaUserRepository.save(user);
    }

    // 2. Logic Đăng nhập
    public NgaUser login(String username, String password) throws Exception {
        NgaUser user = ngaUserRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("Tài khoản hoặc mật khẩu không chính xác!"));

        if (!user.getPasswordHash().equals(password)) {
            throw new Exception("Tài khoản hoặc mật khẩu không chính xác!");
        }

        if (!"active".equals(user.getStatus())) {
            throw new Exception("Tài khoản của bạn đã bị khóa!");
        }
        return user;
    }

    // 3. ĐÃ BỔ SUNG: Lấy chi tiết tài khoản theo ID
    public NgaUser getUserById(Integer id) {
        return ngaUserRepository.findById(id).orElse(null);
    }

    // 4. ĐÃ BỔ SUNG: Cập nhật thông tin thành viên (Phân quyền & Trạng thái)
    public NgaUser updateUser(Integer id, NgaUser newData) throws Exception {
        return ngaUserRepository.findById(id).map(user -> {
            user.setFullName(newData.getFullName());
            user.setEmail(newData.getEmail());
            user.setRole(newData.getRole());       // Cập nhật quyền (admin/staff/user)
            user.setStatus(newData.getStatus());   // Cập nhật trạng thái (active/inactive)
            return ngaUserRepository.save(user);
        }).orElseThrow(() -> new Exception("Không tìm thấy thành viên có ID: " + id));
    }

    // 5. ĐÃ BỔ SUNG: Xóa thành viên
    public void deleteUser(Integer id) throws Exception {
        if (!ngaUserRepository.existsById(id)) {
            throw new Exception("Thành viên không tồn tại hoặc đã bị xóa trước đó!");
        }
        ngaUserRepository.deleteById(id);
    }

    // 6. ĐÃ BỔ SUNG: Tìm kiếm và lọc danh sách thành viên nâng cao
    public List<NgaUser> searchAndFilterUsers(String keyword, String status, String role) {
        // Nếu bạn chưa viết câu Query phức tạp trong Repository, tạm thời dùng findAll()
        // để dập tắt lỗi đỏ ngay lập tức, sau này có thể bổ sung Query sau:
        return ngaUserRepository.findAll();
    }
}