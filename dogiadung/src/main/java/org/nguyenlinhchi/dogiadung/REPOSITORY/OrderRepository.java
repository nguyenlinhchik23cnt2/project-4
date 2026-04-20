package org.nguyenlinhchi.dogiadung.REPOSITORY;



import org.nguyenlinhchi.dogiadung.ENTITY.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}