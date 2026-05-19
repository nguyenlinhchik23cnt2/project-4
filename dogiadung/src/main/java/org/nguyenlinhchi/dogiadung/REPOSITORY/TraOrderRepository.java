package org.nguyenlinhchi.dogiadung.REPOSITORY;


import org.nguyenlinhchi.dogiadung.ENTITY.TraOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TraOrderRepository extends JpaRepository<TraOrder, Integer> {

    List<TraOrder> findByCustomerId(Integer customerId);

    List<TraOrder> findByUserId(Integer userId);

    List<TraOrder> findByStatus(String status);

    List<TraOrder> findByPayStatus(String payStatus);

    List<TraOrder> findByPayMethod(String payMethod);

    List<TraOrder> findByOrderDateBetween(LocalDateTime from, LocalDateTime to);

    List<TraOrder> findByCustomerIdAndStatus(Integer customerId, String status);

    @Query("SELECT o FROM TraOrder o WHERE o.discCode = :discCode")
    List<TraOrder> findByDiscCode(@Param("discCode") String discCode);
}
