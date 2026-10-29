package com.example.inventory.dao;

import com.example.inventory.model.Product;
import com.example.inventory.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean insertProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Product "
                + "(product_id, product_name, category, quantity, price) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, product.getProductId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getCategory());
            ps.setInt(4, product.getQuantity());
            ps.setDouble(5, product.getPrice());
            ps.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false; // Duplicate primary key
        }
    }

    public List<Product> findLowStockByCategory(String category) throws SQLException {
        String sql = "SELECT product_id, product_name, category, quantity, price "
                + "FROM Product WHERE category = ? AND quantity < 20 "
                + "ORDER BY price DESC";
        List<Product> products = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }
        return products;
    }

    public boolean addStock(int productId, int amount) throws SQLException {
        String sql = "UPDATE Product SET quantity = quantity + ? WHERE product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public Product findById(int productId) throws SQLException {
        String sql = "SELECT product_id, product_name, category, quantity, price "
                + "FROM Product WHERE product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapProduct(rs) : null;
            }
        }
    }

    public boolean updatePrice(int productId, double newPrice) throws SQLException {
        String sql = "UPDATE Product SET price = ? WHERE product_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        }
    }

    public double getTotalInventoryValue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity * price), 0) AS total_value FROM Product";
        try (Connection con = DBConnection.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            rs.next();
            return rs.getDouble("total_value");
        }
    }

    public boolean deleteIfQuantityZero(int productId) throws SQLException {
        String sql = "DELETE FROM Product WHERE product_id = ? AND quantity = 0";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        }
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("product_id"),
                rs.getString("product_name"),
                rs.getString("category"),
                rs.getInt("quantity"),
                rs.getDouble("price"));
    }
}
