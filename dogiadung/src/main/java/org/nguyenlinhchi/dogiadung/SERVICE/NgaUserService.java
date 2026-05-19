package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaUser;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaUserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NgaUserService {

    private final NgaUserRepository ngaUserRepository;

    public NgaUserService(NgaUserRepository ngaUserRepository) {
        this.ngaUserRepository = ngaUserRepository;
    }

    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();
        try {
            NgaUser user = ngaUserRepository.findByUsername(username)
                    .orElseThrow(() -> new Exception("Tài khoản hoặc mật khẩu không chính xác!"));

            if (!user.getPasswordHash().equals(password)) {
                throw new Exception("Tài khoản hoặc mật khẩu không chính xác!");
            }

            if (!"active".equals(user.getStatus())) {
                throw new Exception("Tài khoản của bạn đã bị khóa!");
            }

            String rawTokenInfo = user.getUsername() + ":" + user.getRole();
            String generatedToken = Base64.getEncoder().encodeToString(rawTokenInfo.getBytes());

            result.put("success", true);
            result.put("message", "Đăng nhập thành công!");
            result.put("role", user.getRole());
            result.put("token", generatedToken);
            result.put("user", user);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }

    public NgaUser register(NgaUser user) throws Exception {
        if (ngaUserRepository.existsByUsername(user.getUsername())) {
            throw new Exception("Tên đăng nhập này đã tồn tại!");
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (ngaUserRepository.existsByEmail(user.getEmail())) {
                throw new Exception("Địa chỉ Email này đã được sử dụng!");
            }
        }
        user.setRole("USER"); // Đồng bộ chữ IN HOA khớp với bộ lọc Front-end
        user.setStatus("active");
        return ngaUserRepository.save(user);
    }

    // FIX LỖI: Thêm hàm xử lý lọc danh sách thực tế
    public List<NgaUser> searchAndFilterUsers(String keyword, String status, String role) {
        String cleanKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();
        String cleanStatus = (status == null || status.trim().isEmpty()) ? "all" : status.trim();
        String cleanRole = (role == null || role.trim().isEmpty()) ? "all" : role.trim();

        return ngaUserRepository.searchAndFilterUsers(cleanKeyword, cleanStatus, cleanRole);
    }

    // FIX LỖI: Thêm phương thức updateStatus
    public boolean updateStatus(Integer id, String newStatus) {
        return ngaUserRepository.findById(id).map(user -> {
            user.setStatus(newStatus);
            ngaUserRepository.save(user);
            return true;
        }).orElse(false);
    }

    // FIX LỖI: Thêm phương thức deleteById
    public boolean deleteById(Integer id) {
        if (ngaUserRepository.existsById(id)) {
            ngaUserRepository.deleteById(id);
            return true;
        }
        return false;
    }
}