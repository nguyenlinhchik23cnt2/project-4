package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.nguyenlinhchi.dogiadung.ENTITY.NgaUser;
import org.nguyenlinhchi.dogiadung.SERVICE.NgaUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class NgaUserController {


    private final NgaUserService ngaUserService;
    public NgaUserController(NgaUserService ngaUserService) {
        this.ngaUserService = ngaUserService;
    }

    // ... Giữ nguyên toàn bộ các hàm @PostMapping, @GetMapping, @PutMapping, @DeleteMapping ở bước trước ...
}