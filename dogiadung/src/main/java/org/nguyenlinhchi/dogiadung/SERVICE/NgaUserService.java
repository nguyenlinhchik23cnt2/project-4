package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.DTO.ChangePasswordRequest;
import org.nguyenlinhchi.dogiadung.DTO.UserDTO;
import org.nguyenlinhchi.dogiadung.ENTITY.NgaUser;
import org.nguyenlinhchi.dogiadung.REPOSITORY.NgaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NgaUserService {

    private final NgaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public NgaUserService(NgaUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ====================== LOGIN ======================
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();

        Optional<NgaUser> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            result.put("success", false);
            result.put("message", "Không tìm thấy tài khoản!");
            System.out.println(" Không tìm thấy user: " + username);
            return result;
        }

        NgaUser user = optUser.get();

        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Username: " + username);
        System.out.println("Input Password: " + password);
        System.out.println("Stored Hash: " + user.getPasswordHash());

        // === TẠM TẮT KIỂM TRA MẬT KHẨU ĐỂ TEST ===
        boolean passwordOK = true;   // <--- Tạm cho qua tất cả

        // Bỏ comment dòng dưới nếu muốn test bình thường sau này
        // boolean passwordOK = passwordEncoder.matches(password, user.getPasswordHash());

        if (!"active".equals(user.getStatus())) {
            result.put("success", false);
            result.put("message", "Tài khoản đã bị khóa!");
            return result;
        }

        if (!passwordOK) {
            result.put("success", false);
            result.put("message", "Tài khoản hoặc mật khẩu không đúng!");
            System.out.println(" Sai mật khẩu");
            return result;
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        result.put("success", true);
        result.put("message", "Đăng nhập thành công!");
        result.put("user", user);
        System.out.println(" LOGIN THÀNH CÔNG: " + username);
        return result;
    }

    // ====================== REGISTER ======================
    @Transactional
    public NgaUser register(NgaUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setStatus("active");
        if (user.getRole() == null) user.setRole("staff");

        return userRepository.save(user);
    }

    // ====================== CHANGE PASSWORD ======================
    @Transactional
    public boolean changePassword(Integer id, String oldPassword, String newPassword) {
        NgaUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            return false;
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setLastLogin(null);
        userRepository.save(user);
        return true;
    }

    // ====================== RESET PASSWORD (Admin) ======================
    @Transactional
    public boolean resetPassword(Integer id, String newPassword) {
        NgaUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        String hashed = passwordEncoder.encode(newPassword);

        System.out.println("RESET PASSWORD DEBUG");
        System.out.println("Username     : " + user.getUsername());
        System.out.println("Plain password : " + newPassword);
        System.out.println("Hashed password: " + hashed);
        System.out.println("Old hash was   : " + user.getPasswordHash());

        user.setPasswordHash(hashed);
        user.setLastLogin(null);
        userRepository.save(user);

        System.out.println(" Đã lưu hash mới thành công!");
        return true;
    }

    // ====================== SEARCH & FILTER ======================
    public List<NgaUser> searchAndFilterUsers(String keyword, String status, String role) {
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = "";
        }
        return userRepository.searchUsers(keyword.trim(), status, role);
    }

    // ====================== UPDATE STATUS ======================
    @Transactional
    public boolean updateStatus(Integer id, String newStatus) {
        Optional<NgaUser> opt = userRepository.findById(id);
        if (opt.isEmpty()) return false;

        NgaUser user = opt.get();
        if (!"active".equals(newStatus) && !"inactive".equals(newStatus)) {
            throw new RuntimeException("Trạng thái không hợp lệ (active/inactive)");
        }

        user.setStatus(newStatus);
        userRepository.save(user);
        return true;
    }

    // ====================== DELETE ======================
    @Transactional
    public boolean deleteById(Integer id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    // Các method cũ giữ lại để tránh lỗi
    public NgaUser getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public NgaUser createUser(NgaUser user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }
}