package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_order_logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface chi_order_logsRepository extends JpaRepository<chi_order_logs, Integer> {

    List<chi_order_logs> findByOrderIdOrderByUpdatedAtDesc(Integer orderId);
}