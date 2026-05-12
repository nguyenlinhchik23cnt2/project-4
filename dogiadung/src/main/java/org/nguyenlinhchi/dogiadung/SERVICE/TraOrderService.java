package org.nguyenlinhchi.dogiadung.SERVICE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nguyenlinhchi.dogiadung.ENTITY.TraOrder;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraOrderRepository;
import org.nguyenlinhchi.dogiadung.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraOrderService {

    private final TraOrderRepository repo;

    // ───── READ ─────────────────────────────────────────────────

    public List<TraOrder> findAll() {
        return repo.findAll();
    }

    public TraOrder findById(Integer id) {
        return repo.findById(id)
                   .orElseThrow(() -> new ResourceNotFoundException(
                       "Không tìm thấy đơn hàng với id=" + id));
    }

    public List<TraOrder> findByCustomer(Integer customerId) {
        return repo.findByCustomerId(customerId);
    }

    public List<TraOrder> findByUser(Integer userId) {
        return repo.findByUserId(userId);
    }

    public List<TraOrder> findByStatus(String status) {
        return repo.findByStatus(status);
    }

    public List<TraOrder> findByPayStatus(String payStatus) {
        return repo.findByPayStatus(payStatus);
    }

    public List<TraOrder> findByPayMethod(String payMethod) {
        return repo.findByPayMethod(payMethod);
    }

    public List<TraOrder> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return repo.findByOrderDateBetween(from, to);
    }

    public List<TraOrder> findByCustomerAndStatus(Integer customerId, String status) {
        return repo.findByCustomerIdAndStatus(customerId, status);
    }

    public List<TraOrder> findByDiscCode(String discCode) {
        return repo.findByDiscCode(discCode);
    }

    // ───── CREATE ───────────────────────────────────────────────

    @Transactional
    public TraOrder create(TraOrder order) {
        log.info("Tạo đơn hàng mới, customer_id={}", order.getCustomerId());
        order.setOrderId(null);
        return repo.save(order);
    }

    // ───── UPDATE ───────────────────────────────────────────────

    @Transactional
    public TraOrder update(Integer id, TraOrder incoming) {
        log.info("Cập nhật đơn hàng id={}", id);
        TraOrder existing = findById(id);

        if (incoming.getCustomerId()     != null) existing.setCustomerId(incoming.getCustomerId());
        if (incoming.getUserId()         != null) existing.setUserId(incoming.getUserId());
        if (incoming.getOrderDate()      != null) existing.setOrderDate(incoming.getOrderDate());
        if (incoming.getStatus()         != null) existing.setStatus(incoming.getStatus());
        if (incoming.getShippingAddress()!= null) existing.setShippingAddress(incoming.getShippingAddress());
        if (incoming.getShippingFee()    != null) existing.setShippingFee(incoming.getShippingFee());
        if (incoming.getNote()           != null) existing.setNote(incoming.getNote());
        if (incoming.getTotalAmount()    != null) existing.setTotalAmount(incoming.getTotalAmount());
        if (incoming.getDiscCode()       != null) existing.setDiscCode(incoming.getDiscCode());
        if (incoming.getDiscType()       != null) existing.setDiscType(incoming.getDiscType());
        if (incoming.getDiscValue()      != null) existing.setDiscValue(incoming.getDiscValue());
        if (incoming.getDiscMinOrder()   != null) existing.setDiscMinOrder(incoming.getDiscMinOrder());
        if (incoming.getDiscountAmount() != null) existing.setDiscountAmount(incoming.getDiscountAmount());
        if (incoming.getPayMethod()      != null) existing.setPayMethod(incoming.getPayMethod());
        if (incoming.getPayStatus()      != null) existing.setPayStatus(incoming.getPayStatus());
        if (incoming.getPayAmount()      != null) existing.setPayAmount(incoming.getPayAmount());
        if (incoming.getPayTxnId()       != null) existing.setPayTxnId(incoming.getPayTxnId());
        if (incoming.getPayAt()          != null) existing.setPayAt(incoming.getPayAt());

        return repo.save(existing);
    }

    // ───── DELETE ───────────────────────────────────────────────

    @Transactional
    public void delete(Integer id) {
        log.info("Xóa đơn hàng id={}", id);
        findById(id);
        repo.deleteById(id);
    }

    // ───── CHANGE STATUS ────────────────────────────────────────

    @Transactional
    public TraOrder changeStatus(Integer id, String newStatus) {
        log.info("Đổi trạng thái đơn hàng id={} → {}", id, newStatus);
        List<String> allowed = List.of("pending", "confirmed", "shipped", "done", "cancelled");
        if (!allowed.contains(newStatus)) {
            throw new IllegalArgumentException(
                "Status không hợp lệ: " + newStatus + ". Cho phép: " + allowed);
        }
        TraOrder existing = findById(id);
        existing.setStatus(newStatus);
        return repo.save(existing);
    }

    // ───── MARK PAID ────────────────────────────────────────────

    @Transactional
    public TraOrder markPaid(Integer id, String txnId) {
        log.info("Xác nhận thanh toán đơn hàng id={}", id);
        TraOrder existing = findById(id);
        if ("paid".equals(existing.getPayStatus())) {
            throw new IllegalArgumentException("Đơn hàng id=" + id + " đã được thanh toán trước đó");
        }
        existing.setPayStatus("paid");
        existing.setPayTxnId(txnId);
        existing.setPayAt(LocalDateTime.now());
        return repo.save(existing);
    }
}
