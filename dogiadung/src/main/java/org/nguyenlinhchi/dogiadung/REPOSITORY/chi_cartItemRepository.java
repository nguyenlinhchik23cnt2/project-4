package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_cartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface chi_cartItemRepository extends JpaRepository<chi_cartItem, Integer> {

    List<chi_cartItem> findByCustomerId(Integer customerId);

    List<chi_cartItem> findAllByOrderByCustomerIdAsc();
    Optional<chi_cartItem> findByCustomerIdAndProductId(Integer customerId, Integer productId);

    @Modifying
    @Query("DELETE FROM chi_cartItem c WHERE c.customerId = :customerId AND c.productId = :productId")
    void deleteByCustomerIdAndProductId(Integer customerId, Integer productId);

    @Modifying
    @Query("DELETE FROM chi_cartItem c WHERE c.customerId = :customerId")
    void deleteAllByCustomerId(Integer customerId);
}