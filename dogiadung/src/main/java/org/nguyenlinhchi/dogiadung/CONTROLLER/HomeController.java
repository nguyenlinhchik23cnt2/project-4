package org.nguyenlinhchi.dogiadung.CONTROLLER;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/dogiadung", "/home"})
    public String home() {
        return "redirect:/index.html";
    }

    /** Trang sản phẩm */
    @GetMapping("/products")
    public String products() {
        return "redirect:/products.html";
    }
    /** Trang liên hệ */
    @GetMapping("/contact")
    public String contact() {
        return "redirect:/contact.html";
    }
    /** Trang đăng nhập */
    @GetMapping("/login")
    public String login() {
        return "redirect:/login.html";
    }


    /** Trang quản trị chính */
    @GetMapping("/admin")
    public String admin() {
        return "redirect:/admin.html";
    }

    /** Trang quản lý đơn hàng (alias) */
    @GetMapping("/admin/orders")
    public String adminOrders() {
        return "redirect:/admin.html";
    }

    /** Trang quản lý sản phẩm (alias) */
    @GetMapping("/admin/products")
    public String adminProducts() {
        return "redirect:/admin.html";
    }

    /** Trang quản lý nhập kho (alias) */
    @GetMapping("/admin/stock")
    public String adminStock() {
        return "redirect:/admin.html";
    }

    /** Trang quản lý danh mục (alias) */
    @GetMapping("/admin/categories")
    public String adminCategories() {
        return "redirect:/admin.html";
    }

    /** Trang quản lý người dùng (alias) */
    @GetMapping("/admin/users")
    public String adminUsers() {
        return "redirect:/admin.html";
    }
}