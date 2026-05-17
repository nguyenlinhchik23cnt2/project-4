package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_order_logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface chi_order_logsRepository extends JpaRepository<chi_order_logs, Integer> {

    List<chi_order_logs> findByOrderIdOrderByUpdatedAtDesc(Integer orderId);
<<<<<<< HEAD
    // Sắp xếp theo ID tăng dần
    List<chi_order_logs> findAllByOrderByIdAsc();


=======
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
}