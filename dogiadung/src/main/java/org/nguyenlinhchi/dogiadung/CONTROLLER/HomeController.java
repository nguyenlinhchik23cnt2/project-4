package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // ================= USER =================
    @GetMapping("/dogiadung")
    public String home() {
        return "redirect:/index.html";
    }

    // ================= ADMIN =================
    @GetMapping("/admin")
    public String admin() {
        return "redirect:/admin.html";
    }

    // ================= ORDER ITEM PAGE =================
    @GetMapping("/admin/order-items")
    public String orderItems() {
        return "redirect:/order-items.html";
    }

    // ================= PRODUCT PAGE =================
    @GetMapping("/admin/TraProducts")
    public String products() {
        return "redirect:/TraProduct.html";
    }

    // ================= LOGIN =================
    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }
}