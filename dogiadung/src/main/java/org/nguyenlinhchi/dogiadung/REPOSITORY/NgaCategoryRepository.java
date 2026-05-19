package org.nguyenlinhchi.dogiadung.REPOSITORY;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NgaCategoryRepository extends JpaRepository<NgaCategory, Integer> {

    // Tìm các danh mục gốc không có danh mục cha
    List<NgaCategory> findByParentIsNull();

    // HÀM TÌM KIẾM & LỌC NÂNG CAO: Tìm theo tên gần đúng và trạng thái danh mục
    @Query("SELECT c FROM NgaCategory c WHERE " +
            "(:keyword IS NULL OR c.name LIKE %:keyword% OR c.description LIKE %:keyword%) AND " +
            "(:status IS NULL OR c.status = :status)")
    List<NgaCategory> searchAndFilterCategories(@Param("keyword") String keyword,
                                                @Param("status") String status);
}