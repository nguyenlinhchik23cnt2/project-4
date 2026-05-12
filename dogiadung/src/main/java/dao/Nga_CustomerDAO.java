package dao;

import context.Nga_DBContext;
import model.Nga_Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Nga_CustomerDAO {
    Nga_DBContext db = new Nga_DBContext();

    // F09: Them moi khach hang
    public void register(Nga_Customer c) throws Exception {
        String sql = "INSERT INTO nga_customers(full_name, phone, email, address, password) VALUES(?,?,?,?,?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, c.getFullName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setNString(4, c.getAddress());
            ps.setString(5, c.getPassword() != null ? c.getPassword() : "123");
            ps.executeUpdate();
        }
    }

    // F10: Lay toan bo danh sach khach hang
    public List<Nga_Customer> getAll() throws Exception {
        List<Nga_Customer> list = new ArrayList<>();
        String sql = "SELECT customer_id, full_name, phone, email, address FROM nga_customers";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Nga_Customer(
                        rs.getInt("customer_id"),
                        rs.getNString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getNString("address")
                ));
            }
        }
        return list;
    }

    // F12: Tim kiem khach hang theo ten hoac so dien thoai
    public List<Nga_Customer> search(String txt) throws Exception {
        List<Nga_Customer> list = new ArrayList<>();
        String sql = "SELECT customer_id, full_name, phone, email, address FROM nga_customers WHERE full_name LIKE ? OR phone LIKE ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, "%" + txt + "%");
            ps.setString(2, "%" + txt + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Nga_Customer(
                            rs.getInt("customer_id"),
                            rs.getNString("full_name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getNString("address")
                    ));
                }
            }
        }
        return list;
    }

    // F11: Cap nhat thong tin khach hang
    public void update(Nga_Customer c) throws Exception {
        String sql = "UPDATE nga_customers SET full_name=?, phone=?, email=?, address=? WHERE customer_id=?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, c.getFullName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setNString(4, c.getAddress());
            ps.setInt(5, c.getCustomerId());
            ps.executeUpdate();
        }
    }
}