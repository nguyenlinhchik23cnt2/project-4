package org.nguyenlinhchi.dogiadung.SERVICE;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_order_logs;
import org.nguyenlinhchi.dogiadung.REPOSITORY.chi_order_logsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class chi_order_logsService {

    @Autowired
    private chi_order_logsRepository repository;

    // CREATE
    public chi_order_logs save(chi_order_logs log) {
        return repository.save(log);
    }

    // READ ALL
//    public List<chi_order_logs> getAll() {
//        return repository.findAll();
//    }

    // READ ALL - Sắp xếp theo ID tăng dần
    public List<chi_order_logs> getAll() {
        return repository.findAllByOrderByIdAsc();
    }
    // READ BY ID
    public chi_order_logs getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order log not found with id = " + id));
    }

    // READ BY ORDER_ID
    public List<chi_order_logs> getByOrderId(Integer orderId) {
        return repository.findByOrderIdOrderByUpdatedAtDesc(orderId);
    }

    // UPDATE
    public chi_order_logs update(Integer id, chi_order_logs newLog) {
        return repository.findById(id).map(log -> {
            log.setOrderId(newLog.getOrderId());
            log.setOldStatus(newLog.getOldStatus());
            log.setNewStatus(newLog.getNewStatus());
            log.setChangedBy(newLog.getChangedBy());
            log.setNote(newLog.getNote());
            return repository.save(log);
        }).orElseThrow(() -> new RuntimeException("Order log not found with id = " + id));
    }

    // DELETE
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Order log not found with id = " + id);
        }
        repository.deleteById(id);
    }

}