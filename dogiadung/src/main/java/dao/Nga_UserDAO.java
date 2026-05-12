package dao;

import context.Nga_DBContext;
import model.Nga_User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Nga_UserDAO {

    private Connection getConn() throws Exception {
        return new Nga_DBContext().getConnection();
    }

    public void register(String user, String pass, String name, String email) throws Exception {
        String sql = "INSERT INTO nga_users (username, password_hash, full_name, email, role, status) VALUES (?, ?, ?, ?, 'customer', 'active')";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setNString(3, name);
            ps.setString(4, email);
            ps.executeUpdate();
        }
    }

    public Nga_User findByUsername(String username) throws Exception {
        String sql = "SELECT user_id, username, full_name, role, status FROM nga_users WHERE username = ?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Nga_User u = new Nga_User();
                    u.setUser_id(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setFull_name(rs.getNString("full_name"));
                    u.setRole(rs.getString("role"));
                    u.setStatus(rs.getString("status"));
                    return u;
                }
            }
        }
        return null;
    }

    public void updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE nga_users SET status = ? WHERE user_id = ?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public List<Nga_User> getAll(String txtSearch) throws Exception {
        List<Nga_User> list = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash, full_name, role, status FROM nga_users WHERE username LIKE ? OR full_name LIKE ?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String keyword = "%" + (txtSearch != null ? txtSearch : "") + "%";
            ps.setString(1, keyword);
            ps.setNString(2, keyword);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Nga_User u = new Nga_User();
                    u.setUser_id(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword_hash(rs.getString("password_hash"));
                    u.setFull_name(rs.getNString("full_name"));
                    u.setRole(rs.getString("role"));
                    u.setStatus(rs.getString("status"));
                    list.add(u);
                }
            }
        }
        return list;
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM nga_users WHERE user_id = ?";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Nga_User login(String user, String pass) throws Exception {
        String sql = "SELECT user_id, username, password_hash, full_name, role, status FROM nga_users WHERE username = ? AND password_hash = ? AND status = 'active'";
        try (Connection conn = getConn(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Nga_User u = new Nga_User();
                    u.setUser_id(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setPassword_hash(rs.getString("password_hash"));
                    u.setFull_name(rs.getNString("full_name"));
                    u.setRole(rs.getString("role"));
                    u.setStatus(rs.getString("status"));
                    return u;
                }
            }
        }
        return null;
    }
}