package com.example.inventory;

import com.example.inventory.controller.ProductController;
import com.example.inventory.dao.ProductDAO;
import com.example.inventory.service.ProductService;
import com.example.inventory.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class InventoryManagementApp {
    public static void main(String[] args) {
        try (Connection ignored = DBConnection.getConnection();
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected to retail_db successfully.");
            ProductDAO dao = new ProductDAO();
            ProductService service = new ProductService(dao);
            ProductController controller = new ProductController(service, scanner);
            controller.runMenu();
        } catch (SQLException e) {
            System.out.println("Could not connect to MySQL.");
            System.out.println("Check that MySQL is running, retail_db exists, and credentials in "
                    + "src/main/resources/application.properties are correct.");
            System.out.println("Details: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Application could not start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
