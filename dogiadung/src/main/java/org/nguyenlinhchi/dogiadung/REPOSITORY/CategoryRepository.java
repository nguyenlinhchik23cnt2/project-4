package org.nguyenlinhchi.dogiadung.REPOSITORY;



import org.springframework.data.jpa.repository.JpaRepository;
import org.nguyenlinhchi.dogiadung.ENTITY.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}