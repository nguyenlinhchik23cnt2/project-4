package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.chi_order_logs;
import org.nguyenlinhchi.dogiadung.SERVICE.chi_order_logsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-logs")
<<<<<<< HEAD
@CrossOrigin(origins = "*")     // ← THÊM DÒNG NÀY
=======
>>>>>>> ecc3aead0d9dbe4ca659da22d1b158672f4734f0
public class chi_order_logsController {

    @Autowired
    private chi_order_logsService service;

    // CREATE
    @PostMapping
    public ResponseEntity<chi_order_logs> create(@RequestBody chi_order_logs log) {
        chi_order_logs saved = service.save(log);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<chi_order_logs>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<chi_order_logs> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // READ BY ORDER_ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<chi_order_logs>> getByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(service.getByOrderId(orderId));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<chi_order_logs> update(@PathVariable Integer id,
                                                 @RequestBody chi_order_logs log) {
        chi_order_logs updated = service.update(id, log);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}