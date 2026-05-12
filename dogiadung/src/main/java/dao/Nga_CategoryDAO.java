package dao;

import context.Nga_DBContext;
import model.Nga_Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Nga_CategoryDAO {
    Nga_DBContext db = new Nga_DBContext();

    // F16: Them danh muc
    public void add(Nga_Category c) throws Exception {
        String sql = "INSERT INTO nga_categories(name, description, parent_id, sort_order, is_visible) VALUES(?,?,?,?,?)";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, c.getName());
            ps.setNString(2, c.getDescription());
            ps.setInt(3, c.getParentId());
            ps.setInt(4, c.getSortOrder());
            ps.setBoolean(5, c.isVisible());
            ps.executeUpdate();
        }
    }

    // F18, F19, F20: Danh sach + Cha con + Sap xep
    public List<Nga_Category> getAll() throws Exception {
        List<Nga_Category> list = new ArrayList<>();
        String sql = "SELECT category_id, name, description, parent_id, sort_order, is_visible FROM nga_categories ORDER BY parent_id, sort_order";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Nga_Category(
                        rs.getInt("category_id"),
                        rs.getNString("name"),
                        rs.getNString("description"),
                        rs.getInt("parent_id"),
                        rs.getInt("sort_order"),
                        rs.getBoolean("is_visible")
                ));
            }
        }
        return list;
    }

    // F18: An/Hien
    public void toggleVisible(int id, boolean status) throws Exception {
        String sql = "UPDATE nga_categories SET is_visible = ? WHERE category_id = ?";
        try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}