package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItem;
import org.nguyenlinhchi.dogiadung.ENTITY.chi_orderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface chi_orderItemRepository extends JpaRepository<chi_orderItem, chi_orderItemId> {

    List<chi_orderItem> findByOrderId(Integer orderId);
}