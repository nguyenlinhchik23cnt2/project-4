/*
package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaUser;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class NgaUserController {

    private final NgaUserService ngaUserService;

    public NgaUserController(NgaUserService ngaUserService) {
        this.ngaUserService = ngaUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Map<String, Object> loginResult = ngaUserService.login(username, password);
        if (Boolean.TRUE.equals(loginResult.get("success"))) {
            return ResponseEntity.ok(loginResult);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResult);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody NgaUser user) {
        Map<String, Object> response = new HashMap<>();
        try {
            NgaUser savedUser = ngaUserService.register(user);
            response.put("success", true);
            response.put("message", "Đăng ký tài khoản nhân viên nội bộ thành công!");
            response.put("user", savedUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Đăng ký thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "status", required = false) String status,
            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {

        Map<String, Object> response = new HashMap<>();

        // Kiểm tra Token tường minh, tối ưu hóa logic đảo ngược
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ") || !hasAccessPrivilege(tokenHeader)) {
            response.put("success", false);
            response.put("message", "Lỗi bảo mật: Bạn không có quyền truy cập chức năng này!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            List<NgaUser> users = ngaUserService.searchAndFilterUsers(keyword, status, role);
            response.put("success", true);
            response.put("message", "Tải danh sách thành công!");
            response.put("data", users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi hệ thống: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/users/{id}/status")
    public ResponseEntity<Map<String, Object>> updateUserStatus(
            @PathVariable Integer id, // FIX: Đã lược bỏ khai báo trùng lặp ("id") gây ra cảnh báo thừa
            @RequestBody Map<String, String> requestBody,
            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {

        Map<String, Object> response = new HashMap<>();

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ") || !hasAccessPrivilege(tokenHeader)) {
            response.put("success", false);
            response.put("message", "Lỗi bảo mật: Bạn không có quyền thực hiện thao tác này!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            String newStatus = requestBody.get("status");
            boolean isUpdated = ngaUserService.updateStatus(id, newStatus);

            if (isUpdated) {
                response.put("success", true);
                response.put("message", "Cập nhật trạng thái người dùng thành công!");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy người dùng mang mã ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable Integer id, // FIX: Đã lược bỏ khai báo trùng lặp ("id") gây ra cảnh báo thừa
            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {

        Map<String, Object> response = new HashMap<>();

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ") || !hasAccessPrivilege(tokenHeader)) {
            response.put("success", false);
            response.put("message", "Lỗi bảo mật: Bạn không có quyền xóa dữ liệu!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        try {
            boolean isDeleted = ngaUserService.deleteById(id);
            if (isDeleted) {
                response.put("success", true);
                response.put("message", "Đã xóa tài khoản người dùng thành công!");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy người dùng cần xóa trong hệ thống!");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private boolean hasAccessPrivilege(String tokenHeader) {
        try {
            String base64Token = tokenHeader.substring(7);
            byte[] decodedBytes = Base64.getDecoder().decode(base64Token);
            String decodedString = new String(decodedBytes);
            String lowerDecoded = decodedString.toLowerCase();
            return lowerDecoded.endsWith(":admin") || lowerDecoded.endsWith(":staff");
        } catch (Exception e) {
            return false;
        }
    }
}
*/
