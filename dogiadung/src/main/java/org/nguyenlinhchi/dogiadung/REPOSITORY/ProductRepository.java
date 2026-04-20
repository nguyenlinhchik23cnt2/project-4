package org.nguyenlinhchi.dogiadung.REPOSITORY;



import org.nguyenlinhchi.dogiadung.ENTITY.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}