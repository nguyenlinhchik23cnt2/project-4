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
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // HÀM TÌM KIẾM & LỌC ĐỘNG: Tìm theo Tên, SĐT, Email, Địa chỉ và lọc theo Giới tính
    @Query("SELECT c FROM NgaCustomer c WHERE " +
            "(:keyword IS NULL OR c.fullName LIKE %:keyword% OR c.phone LIKE %:keyword% " +
            "OR c.email LIKE %:keyword% OR c.address LIKE %:keyword%) AND " +
            "(:gender IS NULL OR c.gender = :gender)")
    List<NgaCustomer> searchAndFilterCustomers(@Param("keyword") String keyword,
                                               @Param("gender") String gender);
}