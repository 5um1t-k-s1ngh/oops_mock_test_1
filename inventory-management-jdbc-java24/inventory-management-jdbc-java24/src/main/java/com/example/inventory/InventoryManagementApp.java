package com.example.inventory;

import java.sql.*;
import java.util.Scanner;

public class InventoryManagementApp {
    private static final String URL = "jdbc:mysql://localhost:3306/retail_db";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Connected to retail_db successfully.");

            while (true) {
                System.out.println("\\n===== INVENTORY MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert three sample products");
                System.out.println("2. Retrieve low-stock products by category");
                System.out.println("3. Add stock to a product");
                System.out.println("4. Search product and update price");
                System.out.println("5. Display total inventory value");
                System.out.println("6. Delete product only if quantity is zero");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");

                int choice = readInt();
                try {
                    switch (choice) {
                        case 1 -> insertSampleProducts(con);
                        case 2 -> retrieveLowStockByCategory(con);
                        case 3 -> addStock(con);
                        case 4 -> updatePrice(con);
                        case 5 -> showInventoryValue(con);
                        case 6 -> deleteOnlyIfOutOfStock(con);
                        case 0 -> {
                            System.out.println("Exiting.");
                            return;
                        }
                        default -> System.out.println("Invalid choice.");
                    }
                } catch (SQLException e) {
                    System.out.println("Database operation failed: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to MySQL. Check server, database, and credentials.");
            System.out.println("Details: " + e.getMessage());
        }
    }

    // (a) Insert at least three products using PreparedStatement.
    private static void insertSampleProducts(Connection con) throws SQLException {
        String sql = "INSERT INTO Product (product_id, product_name, category, quantity, price) VALUES (?, ?, ?, ?, ?)";
        Object[][] products = {
            {101, "Keyboard", "Electronics", 15, 799.00},
            {102, "Notebook", "Stationery", 50, 60.00},
            {103, "Mouse", "Electronics", 10, 499.00}
        };

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Object[] p : products) {
                ps.setInt(1, (Integer) p[0]);
                ps.setString(2, (String) p[1]);
                ps.setString(3, (String) p[2]);
                ps.setInt(4, (Integer) p[3]);
                ps.setDouble(5, (Double) p[4]);
                try {
                    ps.executeUpdate();
                    System.out.println("Inserted product ID " + p[0]);
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Product ID " + p[0] + " already exists; skipped duplicate.");
                }
            }
        }
    }

    // (b) Category match, quantity < 20, sorted by price descending.
    private static void retrieveLowStockByCategory(Connection con) throws SQLException {
        System.out.print("Enter category: ");
        String category = sc.nextLine().trim();
        String sql = "SELECT product_id, product_name, category, quantity, price " +
                     "FROM Product WHERE category = ? AND quantity < 20 ORDER BY price DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    printProduct(rs);
                }
                if (!found) System.out.println("No matching low-stock products found.");
            }
        }
    }

    // (c) Add quantity to current stock.
    private static void addStock(Connection con) throws SQLException {
        System.out.print("Enter product ID: ");
        int id = readInt();
        System.out.print("Enter quantity to add: ");
        int amount = readInt();

        if (amount <= 0) {
            System.out.println("Quantity to add must be greater than zero.");
            return;
        }

        String sql = "UPDATE Product SET quantity = quantity + ? WHERE product_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, amount);
            ps.setInt(2, id);
            if (ps.executeUpdate() > 0) System.out.println("Stock updated successfully.");
            else System.out.println("Product does not exist.");
        }
    }

    // (d) Retrieve product first, then update only when new price > 0.
    private static void updatePrice(Connection con) throws SQLException {
        System.out.print("Enter product ID: ");
        int id = readInt();
        String selectSql = "SELECT product_id, product_name, category, quantity, price FROM Product WHERE product_id = ?";

        try (PreparedStatement ps = con.prepareStatement(selectSql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Product does not exist.");
                    return;
                }
                System.out.println("Current product details:");
                printProduct(rs);
            }
        }

        System.out.print("Enter new price (> 0): ");
        double newPrice = readDouble();
        if (newPrice <= 0) {
            System.out.println("Price must be greater than zero. No update made.");
            return;
        }

        String updateSql = "UPDATE Product SET price = ? WHERE product_id = ?";
        try (PreparedStatement ps = con.prepareStatement(updateSql)) {
            ps.setDouble(1, newPrice);
            ps.setInt(2, id);
            if (ps.executeUpdate() > 0) {
                System.out.println("Price updated. Updated product details:");
                try (PreparedStatement ps2 = con.prepareStatement(selectSql)) {
                    ps2.setInt(1, id);
                    try (ResultSet rs = ps2.executeQuery()) {
                        if (rs.next()) printProduct(rs);
                    }
                }
            } else {
                System.out.println("Product was not updated.");
            }
        }
    }

    // (e) Aggregate query.
    private static void showInventoryValue(Connection con) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity * price), 0) AS total_value FROM Product";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) System.out.printf("Total inventory value: %.2f%n", rs.getDouble("total_value"));
        }
    }

    // (e) Delete only if quantity is zero.
    private static void deleteOnlyIfOutOfStock(Connection con) throws SQLException {
        System.out.print("Enter product ID to delete: ");
        int id = readInt();
        String sql = "DELETE FROM Product WHERE product_id = ? AND quantity = 0";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) System.out.println("Product deleted successfully.");
            else System.out.println("Cannot delete: product not found or quantity is not zero.");
        }
    }

    private static void printProduct(ResultSet rs) throws SQLException {
        System.out.printf("ID: %d | Name: %s | Category: %s | Quantity: %d | Price: %.2f%n",
                rs.getInt("product_id"), rs.getString("product_name"),
                rs.getString("category"), rs.getInt("quantity"), rs.getDouble("price"));
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid integer: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
