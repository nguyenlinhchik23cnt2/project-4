package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NgaUserRepository extends JpaRepository<NgaUser, Integer> {

    Optional<NgaUser> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // HÀM TÌM KIẾM & LỌC NÂNG CAO CHO ADMIN: Tìm theo tài khoản, tên, email; lọc theo trạng thái và chức vụ
    @Query("SELECT u FROM NgaUser u WHERE " +
            "(:keyword IS NULL OR u.username LIKE %:keyword% OR u.fullName LIKE %:keyword% OR u.email LIKE %:keyword%) AND " +
            "(:status IS NULL OR u.status = :status) AND " +
            "(:role IS NULL OR u.role = :role)")
    List<NgaUser> searchAndFilterUsers(@Param("keyword") String keyword,
                                       @Param("status") String status,
                                       @Param("role") String role);
}