package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaUser;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final NgaUserService userService;

    public AuthController(NgaUserService userService) {
        this.userService = userService;
    }

    // ĐĂNG NHẬP
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Map<String, Object> result = userService.login(username, password);
        return Boolean.TRUE.equals(result.get("success"))
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }

    // ĐĂNG KÝ
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody NgaUser user) {
        Map<String, Object> response = new HashMap<>();
        try {
            NgaUser savedUser = userService.register(user);
            response.put("success", true);
            response.put("message", "Đăng ký tài khoản thành công!");
            response.put("user", savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Đăng ký thất bại: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // ĐỔI MẬT KHẨU (Người dùng tự đổi)
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer userId = Integer.parseInt(request.get("userId"));
            String oldPassword = request.get("oldPassword");
            String newPassword = request.get("newPassword");

            boolean success = userService.changePassword(userId, oldPassword, newPassword);

            if (success) {
                response.put("success", true);
                response.put("message", "Đổi mật khẩu thành công!");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Mật khẩu cũ không chính xác!");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}