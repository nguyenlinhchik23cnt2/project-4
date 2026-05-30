package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "nga_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgaUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, unique = true, length = 60)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "role", length = 20)
    private String role = "staff";

    @Column(name = "status", length = 20)
    private String status = "active";

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}