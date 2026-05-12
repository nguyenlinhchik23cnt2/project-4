package org.nguyenlinhchi.dogiadung.REPOSITORY;


import org.nguyenlinhchi.dogiadung.ENTITY.TraStockImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraStockImportRepository extends JpaRepository<TraStockImport, Integer> {

    List<TraStockImport> findByProductId(Integer productId);

    List<TraStockImport> findByUserId(Integer userId);

    List<TraStockImport> findByStatus(String status);

    Optional<TraStockImport> findByBatchCode(String batchCode);

    List<TraStockImport> findByImportDateBetween(LocalDateTime from, LocalDateTime to);

    @Query("SELECT s FROM TraStockImport s WHERE s.productId = :productId AND s.status = 'done' ORDER BY s.importDate DESC")
    List<TraStockImport> findDoneByProductId(@Param("productId") Integer productId);
}
