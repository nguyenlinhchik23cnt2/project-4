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

    @Query("SELECT u FROM NgaUser u WHERE " +
            "(:keyword = '' OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR u.status = :status) " +
            "AND (:role IS NULL OR u.role = :role) " +
            "ORDER BY u.createdAt DESC")
    List<NgaUser> searchUsers(@Param("keyword") String keyword,
                              @Param("status") String status,
                              @Param("role") String role);
}