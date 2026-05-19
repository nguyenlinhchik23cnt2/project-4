package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "nga_users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NgaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, length = 60)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    private String phone;

    @Column(name = "role", length = 20)
    private String role = "staff";
    @Column(name = "status", length = 20)
    private String status = "active";

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
