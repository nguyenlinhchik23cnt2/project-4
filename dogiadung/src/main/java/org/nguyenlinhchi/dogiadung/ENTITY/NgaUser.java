package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "nga_users") // Khớp chính xác với tên bảng trong ảnh của bạn
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NgaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId; // Trong database là kiểu 'int', nên dùng Integer hoặc Long đều được (Integer chuẩn hơn với int)

    @Column(name = "username", nullable = false, length = 60) // Khớp varchar(60)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255) // Khớp tên cột password_hash
    private String passwordHash;

    @Column(name = "full_name", length = 150) // Khớp nvarchar(150) và tên cột full_name
    private String fullName;

    @Column(name = "email", length = 100) // Khớp nvarchar(100)
    private String email;

    @Column(name = "phone", length = 20) // Khớp varchar(20)
    private String phone;

    @Column(name = "role", length = 20) // Khớp nvarchar(20)
    private String role = "staff"; // Mặc định trong database của bạn là 'staff'

    @Column(name = "status", length = 20) // Khớp nvarchar(20)
    private String status = "active"; // Mặc định trong database của bạn là 'active' (dạng chuỗi chứ không phải boolean)

    @Column(name = "last_login")
    private LocalDateTime lastLogin; // Khớp datetime

    @Column(name = "avatar_url", length = 255) // Khớp nvarchar(255)
    private String avatarUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt; // Khớp datetime = getdate()

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}