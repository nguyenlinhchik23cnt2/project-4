package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NgaCategoryRepository extends JpaRepository<NgaCategory, Integer> {

    List<NgaCategory> findByParentIsNull();

    List<NgaCategory> findByStatus(String status);
}