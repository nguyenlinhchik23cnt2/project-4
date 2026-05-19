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

    // Thuật toán tìm kiếm nâng cao theo từ khóa, vai trò và trạng thái
    @Query("SELECT u FROM NgaUser u WHERE " +
            "(:keyword IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:role = 'all' OR u.role = :role) " +
            "AND (:status = 'all' OR u.status = :status)")
    List<NgaUser> searchAndFilterUsers(@Param("keyword") String keyword,
                                       @Param("status") String status,
                                       @Param("role") String role);
}