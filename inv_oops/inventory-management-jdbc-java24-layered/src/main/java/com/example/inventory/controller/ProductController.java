package com.example.inventory.controller;

import com.example.inventory.model.Product;
import com.example.inventory.service.ProductService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductController {
    private final ProductService productService;
    private final Scanner scanner;

    public ProductController(ProductService productService, Scanner scanner) {
        this.productService = productService;
        this.scanner = scanner;
    }

    public void runMenu() {
        while (true) {
            printMenu();
            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> insertSampleProducts();
                    case 2 -> retrieveLowStock();
                    case 3 -> addStock();
                    case 4 -> updatePrice();
                    case 5 -> showInventoryValue();
                    case 6 -> deleteProduct();
                    case 0 -> {
                        System.out.println("Exiting Inventory Management System.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please select 0–6.");
                }
            } catch (SQLException e) {
                System.out.println("Database operation failed: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n===== INVENTORY MANAGEMENT SYSTEM =====");
        System.out.println("1. Insert three sample products");
        System.out.println("2. Retrieve products by category (quantity < 20)");
        System.out.println("3. Add stock to a product");
        System.out.println("4. Search product and update price");
        System.out.println("5. Display total inventory value");
        System.out.println("6. Delete product only if quantity is zero");
        System.out.println("0. Exit");
    }

    private void insertSampleProducts() throws SQLException {
        Product[] products = {
            new Product(101, "Keyboard", "Electronics", 15, 799.00),
            new Product(102, "Notebook", "Stationery", 50, 60.00),
            new Product(103, "Mouse", "Electronics", 10, 499.00)
        };

        for (Product product : products) {
            if (productService.insertProduct(product)) {
                System.out.println("Inserted: " + product.getProductName());
            } else {
                System.out.println("Product ID " + product.getProductId()
                        + " already exists; skipped duplicate.");
            }
        }
    }

    private void retrieveLowStock() throws SQLException {
        System.out.print("Enter category (e.g. Electronics): ");
        String category = scanner.nextLine().trim();
        List<Product> products = productService.findLowStockByCategory(category);

        if (products.isEmpty()) {
            System.out.println("No matching low-stock products found.");
            return;
        }
        products.forEach(System.out::println);
    }

    private void addStock() throws SQLException {
        int id = readInt("Enter product ID: ");
        int amount = readInt("Enter quantity to add: ");
        if (productService.addStock(id, amount)) {
            System.out.println("Stock updated successfully.");
        } else {
            System.out.println("Product does not exist.");
        }
    }

    private void updatePrice() throws SQLException {
        int id = readInt("Enter product ID: ");
        Product product = productService.findById(id);
        if (product == null) {
            System.out.println("Product does not exist.");
            return;
        }

        System.out.println("Current product details: " + product);
        double price = readDouble("Enter new price (> 0): ");
        if (productService.updatePrice(id, price)) {
            System.out.println("Updated product details: " + productService.findById(id));
        } else {
            System.out.println("Product could not be updated.");
        }
    }

    private void showInventoryValue() throws SQLException {
        System.out.printf("Total inventory value: %.2f%n",
                productService.getTotalInventoryValue());
    }

    private void deleteProduct() throws SQLException {
        int id = readInt("Enter product ID to delete: ");
        if (productService.deleteIfQuantityZero(id)) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println("Product cannot be deleted: it may not exist or quantity is not zero.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
