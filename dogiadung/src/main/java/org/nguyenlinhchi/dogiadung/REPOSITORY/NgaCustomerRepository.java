package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NgaCustomerRepository extends JpaRepository<NgaCustomer, Integer> {

    Optional<NgaCustomer> findByUsername(String username);

    // Dùng khi đổi username — kiểm tra trùng lặp toàn bảng
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

<<<<<<< HEAD
=======
    // Dùng khi đổi email — loại trừ chính customer đang sửa
    boolean existsByEmailAndIdNot(String email, Integer id);

>>>>>>> 931edeeeb1e324e527ae9e0feee350a772cd7ed6
    @Query("SELECT c FROM NgaCustomer c WHERE " +
            "(:keyword IS NULL OR c.fullName LIKE %:keyword% OR c.phone LIKE %:keyword% " +
            "OR c.email LIKE %:keyword% OR c.address LIKE %:keyword%) AND " +
            "(:gender IS NULL OR c.gender = :gender)")
    List<NgaCustomer> searchAndFilterCustomers(@Param("keyword") String keyword,
                                               @Param("gender") String gender);
}