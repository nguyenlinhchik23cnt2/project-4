package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.DTO.LoginRequest;
import org.nguyenlinhchi.dogiadung.DTO.LoginResponse;
import org.nguyenlinhchi.dogiadung.ENTITY.User;
import org.nguyenlinhchi.dogiadung.REPOSITORY.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Username: " + loginRequest.getUsername());
        System.out.println("Password length: " + loginRequest.getPassword().length());

        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

        if (user == null) {
            System.out.println("❌ Không tìm thấy username: " + loginRequest.getUsername());
            return ResponseEntity.badRequest().body("❌ Sai tên đăng nhập!");
        }

        System.out.println("✅ Tìm thấy user: " + user.getUsername());
        System.out.println("Role: " + user.getRole());
        System.out.println("Status: " + user.getStatus());
        System.out.println("Password hash trong DB: " + user.getPasswordHash());

        String inputPassword = loginRequest.getPassword();
        String storedPassword = user.getPasswordHash();

        boolean passwordMatch = passwordEncoder.matches(inputPassword, storedPassword);

        System.out.println("BCrypt match result: " + passwordMatch);

        // Fallback cho mật khẩu plain text (tài khoản cũ)
        if (!passwordMatch && inputPassword.equals(storedPassword)) {
            passwordMatch = true;
            System.out.println("⚠️ Sử dụng mật khẩu plain text");
        }

        if (!passwordMatch) {
            System.out.println("❌ Sai mật khẩu!");
            return ResponseEntity.badRequest().body("❌ Sai mật khẩu!");
        }

        if (!"active".equals(user.getStatus())) {
            System.out.println("❌ Tài khoản không active");
            return ResponseEntity.badRequest().body("❌ Tài khoản đã bị khóa!");
        }

        System.out.println("✅ ĐĂNG NHẬP THÀNH CÔNG!");
        user.setRole("ADMIN");
        LoginResponse response = new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getRole()

        );

        return ResponseEntity.ok(response);
    }
}