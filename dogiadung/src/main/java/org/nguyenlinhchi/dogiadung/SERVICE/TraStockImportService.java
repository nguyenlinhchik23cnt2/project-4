package org.nguyenlinhchi.dogiadung.SERVICE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nguyenlinhchi.dogiadung.ENTITY.TraStockImport;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraStockImportRepository;
import org.nguyenlinhchi.dogiadung.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraStockImportService {

    private final TraStockImportRepository repo;

    // ───── READ ─────────────────────────────────────────────────

    public List<TraStockImport> findAll() {
        return repo.findAll();
    }

    public TraStockImport findById(Integer id) {
        return repo.findById(id)
                   .orElseThrow(() -> new ResourceNotFoundException(
                       "Không tìm thấy phiếu nhập với id=" + id));
    }

    public List<TraStockImport> findByProduct(Integer productId) {
        return repo.findByProductId(productId);
    }

    public List<TraStockImport> findByUser(Integer userId) {
        return repo.findByUserId(userId);
    }

    public List<TraStockImport> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    public TraStockImport findByBatchCode(String batchCode) {
        return repo.findByBatchCode(batchCode)
                   .orElseThrow(() -> new ResourceNotFoundException(
                       "Không tìm thấy phiếu nhập với batch_code=" + batchCode));
    }

    public List<TraStockImport> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return repo.findByImportDateBetween(from, to);
    }

    // ───── CREATE ───────────────────────────────────────────────

    @Transactional
    public TraStockImport create(TraStockImport stockImport) {
        log.info("Tạo phiếu nhập mới, product_id={}", stockImport.getProductId());
        stockImport.setId(null);
        return repo.save(stockImport);
    }

    // ───── UPDATE ───────────────────────────────────────────────

    @Transactional
    public TraStockImport update(Integer id, TraStockImport incoming) {
        log.info("Cập nhật phiếu nhập id={}", id);
        TraStockImport existing = findById(id);

        if (incoming.getBatchCode()   != null) existing.setBatchCode(incoming.getBatchCode());
        if (incoming.getProductId()   != null) existing.setProductId(incoming.getProductId());
        if (incoming.getUserId()      != null) existing.setUserId(incoming.getUserId());
        if (incoming.getImportDate()  != null) existing.setImportDate(incoming.getImportDate());
        if (incoming.getQuantity()    != null) existing.setQuantity(incoming.getQuantity());
        if (incoming.getImportPrice() != null) existing.setImportPrice(incoming.getImportPrice());
        if (incoming.getNote()        != null) existing.setNote(incoming.getNote());
        if (incoming.getStatus()      != null) existing.setStatus(incoming.getStatus());

        return repo.save(existing);
    }

    // ───── DELETE ───────────────────────────────────────────────

    @Transactional
    public void delete(Integer id) {
        log.info("Xóa phiếu nhập id={}", id);
        findById(id);
        repo.deleteById(id);
    }

    // ───── CANCEL (soft) ────────────────────────────────────────

    @Transactional
    public TraStockImport cancel(Integer id) {
        log.info("Hủy phiếu nhập id={}", id);
        TraStockImport existing = findById(id);
        if ("cancelled".equals(existing.getStatus())) {
            throw new IllegalArgumentException("Phiếu nhập id=" + id + " đã bị hủy trước đó");
        }
        existing.setStatus("cancelled");
        return repo.save(existing);
    }
}
