package org.nguyenlinhchi.dogiadung.SERVICE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nguyenlinhchi.dogiadung.ENTITY.TraOrder;
import org.nguyenlinhchi.dogiadung.REPOSITORY.TraOrderRepository;
import org.nguyenlinhchi.dogiadung.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
=======
import java.time.LocalDateTime;
import java.util.List;
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0

@Service
@RequiredArgsConstructor
@Slf4j
public class TraOrderService {

    private final TraOrderRepository repo;

<<<<<<< HEAD
    private static final Set<String> ALLOWED_STATUSES = Set.of("pending", "confirmed", "shipped", "done", "cancelled");
    private static final Set<String> ALLOWED_PAY_STATUSES = Set.of("pending", "paid", "failed", "refunded");
    private static final Set<String> ALLOWED_PAY_METHODS = Set.of("COD", "Banking", "Momo", "VNPay");
    private static final Set<String> ALLOWED_DISC_TYPES = Set.of("percent", "fixed");

    // ───── VALIDATION ─────────────────────────────────────────────

    private void validateOrder(TraOrder order) {
        if (order.getTotalAmount() != null && order.getTotalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total amount không được âm");
        }
        if (order.getShippingFee() != null && order.getShippingFee().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Shipping fee không được âm");
        }
        if (order.getDiscountAmount() != null && order.getDiscountAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Discount amount không được âm");
        }

        if (order.getPayMethod() != null && !ALLOWED_PAY_METHODS.contains(order.getPayMethod())) {
            throw new IllegalArgumentException("Pay method không hợp lệ: " + order.getPayMethod());
        }
        if (order.getDiscType() != null && !ALLOWED_DISC_TYPES.contains(order.getDiscType())) {
            throw new IllegalArgumentException("Discount type không hợp lệ: " + order.getDiscType());
        }
        if (order.getStatus() != null && !ALLOWED_STATUSES.contains(order.getStatus())) {
            throw new IllegalArgumentException("Status không hợp lệ: " + order.getStatus());
        }
    }

    private void calculateDiscountAndTotal(TraOrder order) {
        if (order.getDiscValue() == null || order.getDiscType() == null) {
            order.setDiscountAmount(BigDecimal.ZERO);
            return;
        }

        BigDecimal subtotal = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;

        // Kiểm tra điều kiện tối thiểu
        if (order.getDiscMinOrder() != null &&
                subtotal.compareTo(order.getDiscMinOrder()) < 0) {
            order.setDiscountAmount(BigDecimal.ZERO);
            return;
        }

        BigDecimal discount = BigDecimal.ZERO;

        if ("percent".equals(order.getDiscType())) {
            discount = subtotal.multiply(order.getDiscValue().divide(BigDecimal.valueOf(100)));
        } else if ("fixed".equals(order.getDiscType())) {
            discount = order.getDiscValue();
        }

        // Không cho discount vượt quá subtotal
        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }

        order.setDiscountAmount(discount);
    }

=======
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
    // ───── READ ─────────────────────────────────────────────────

    public List<TraOrder> findAll() {
        return repo.findAll();
    }

    public TraOrder findById(Integer id) {
        return repo.findById(id)
<<<<<<< HEAD
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với id=" + id));
=======
                   .orElseThrow(() -> new ResourceNotFoundException(
                       "Không tìm thấy đơn hàng với id=" + id));
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
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
<<<<<<< HEAD
        log.info("Tạo đơn hàng mới cho customer_id={}", order.getCustomerId());

        order.setOrderId(null);
        validateOrder(order);
        calculateDiscountAndTotal(order);

        // Tính pay_amount nếu chưa có
        if (order.getPayAmount() == null) {
            BigDecimal total = order.getTotalAmount() != null ? order.getTotalAmount() : BigDecimal.ZERO;
            BigDecimal discount = order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO;
            BigDecimal shipping = order.getShippingFee() != null ? order.getShippingFee() : BigDecimal.ZERO;
            order.setPayAmount(total.subtract(discount).add(shipping));
        }

=======
        log.info("Tạo đơn hàng mới, customer_id={}", order.getCustomerId());
        order.setOrderId(null);
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
        return repo.save(order);
    }

    // ───── UPDATE ───────────────────────────────────────────────

    @Transactional
    public TraOrder update(Integer id, TraOrder incoming) {
        log.info("Cập nhật đơn hàng id={}", id);
        TraOrder existing = findById(id);

<<<<<<< HEAD
        // Cập nhật các trường được gửi lên
        if (incoming.getCustomerId() != null) existing.setCustomerId(incoming.getCustomerId());
        if (incoming.getUserId() != null) existing.setUserId(incoming.getUserId());
        if (incoming.getOrderDate() != null) existing.setOrderDate(incoming.getOrderDate());
        if (incoming.getStatus() != null) existing.setStatus(incoming.getStatus());
        if (incoming.getShippingAddress() != null) existing.setShippingAddress(incoming.getShippingAddress());
        if (incoming.getShippingFee() != null) existing.setShippingFee(incoming.getShippingFee());
        if (incoming.getNote() != null) existing.setNote(incoming.getNote());
        if (incoming.getTotalAmount() != null) existing.setTotalAmount(incoming.getTotalAmount());
        if (incoming.getDiscCode() != null) existing.setDiscCode(incoming.getDiscCode());
        if (incoming.getDiscType() != null) existing.setDiscType(incoming.getDiscType());
        if (incoming.getDiscValue() != null) existing.setDiscValue(incoming.getDiscValue());
        if (incoming.getDiscMinOrder() != null) existing.setDiscMinOrder(incoming.getDiscMinOrder());
        if (incoming.getDiscountAmount() != null) existing.setDiscountAmount(incoming.getDiscountAmount());
        if (incoming.getPayMethod() != null) existing.setPayMethod(incoming.getPayMethod());
        if (incoming.getPayStatus() != null) existing.setPayStatus(incoming.getPayStatus());
        if (incoming.getPayAmount() != null) existing.setPayAmount(incoming.getPayAmount());
        if (incoming.getPayTxnId() != null) existing.setPayTxnId(incoming.getPayTxnId());
        if (incoming.getPayAt() != null) existing.setPayAt(incoming.getPayAt());

        validateOrder(existing);
        calculateDiscountAndTotal(existing);
=======
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
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0

        return repo.save(existing);
    }

    // ───── DELETE ───────────────────────────────────────────────

    @Transactional
    public void delete(Integer id) {
        log.info("Xóa đơn hàng id={}", id);
<<<<<<< HEAD
        findById(id); // check tồn tại
        repo.deleteById(id);
    }

    // ───── BUSINESS METHODS ─────────────────────────────────────
=======
        findById(id);
        repo.deleteById(id);
    }

    // ───── CHANGE STATUS ────────────────────────────────────────
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0

    @Transactional
    public TraOrder changeStatus(Integer id, String newStatus) {
        log.info("Đổi trạng thái đơn hàng id={} → {}", id, newStatus);
<<<<<<< HEAD

        if (!ALLOWED_STATUSES.contains(newStatus)) {
            throw new IllegalArgumentException("Status không hợp lệ: " + newStatus);
        }

        TraOrder order = findById(id);
        order.setStatus(newStatus);
        return repo.save(order);
    }

    @Transactional
    public TraOrder markPaid(Integer id, String txnId) {
        log.info("Xác nhận thanh toán đơn hàng id={}", id);
        TraOrder order = findById(id);

        if ("paid".equals(order.getPayStatus())) {
            throw new IllegalArgumentException("Đơn hàng này đã được thanh toán trước đó");
        }

        order.setPayStatus("paid");
        order.setPayTxnId(txnId);
        order.setPayAt(LocalDateTime.now());
        return repo.save(order);
    }

    // Bonus: Tính lại tổng tiền + discount khi cần
    @Transactional
    public TraOrder recalculateOrder(Integer id) {
        TraOrder order = findById(id);
        calculateDiscountAndTotal(order);
        return repo.save(order);
    }
}
=======
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
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
