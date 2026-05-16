package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaCustomer;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaCustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin(origins = "*")   // Cách đơn giản nhất, tránh lỗi compile
public class NgaCustomerController {

    private final NgaCustomerService service;

    public NgaCustomerController(NgaCustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NgaCustomer> create(@RequestBody NgaCustomer customer) {
        NgaCustomer saved = service.create(customer);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NgaCustomer>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NgaCustomer> getById(@PathVariable Integer id) {
        NgaCustomer customer = service.getById(id);
        return customer != null ? ResponseEntity.ok(customer)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<NgaCustomer> update(@PathVariable Integer id,
                                              @RequestBody NgaCustomer customer) {
        NgaCustomer updated = service.update(id, customer);
        return updated != null ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}