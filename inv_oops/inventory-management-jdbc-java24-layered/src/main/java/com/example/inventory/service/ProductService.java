package com.example.inventory.service;

import com.example.inventory.dao.ProductDAO;
import com.example.inventory.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public boolean insertProduct(Product product) throws SQLException {
        if (product.getProductId() <= 0) {
            throw new IllegalArgumentException("Product ID must be positive.");
        }
        if (product.getProductName() == null || product.getProductName().isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
        if (product.getCategory() == null || product.getCategory().isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        if (product.getQuantity() < 0 || product.getPrice() < 0) {
            throw new IllegalArgumentException("Quantity and price cannot be negative.");
        }
        return productDAO.insertProduct(product);
    }

    public List<Product> findLowStockByCategory(String category) throws SQLException {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }
        return productDAO.findLowStockByCategory(category.trim());
    }

    public boolean addStock(int productId, int amount) throws SQLException {
        if (productId <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        if (amount <= 0) throw new IllegalArgumentException("Quantity to add must be greater than zero.");
        return productDAO.addStock(productId, amount);
    }

    public Product findById(int productId) throws SQLException {
        if (productId <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        return productDAO.findById(productId);
    }

    public boolean updatePrice(int productId, double newPrice) throws SQLException {
        if (productId <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        if (!Double.isFinite(newPrice) || newPrice <= 0) {
            throw new IllegalArgumentException("New price must be greater than zero.");
        }
        if (productDAO.findById(productId) == null) return false;
        return productDAO.updatePrice(productId, newPrice);
    }

    public double getTotalInventoryValue() throws SQLException {
        return productDAO.getTotalInventoryValue();
    }

    public boolean deleteIfQuantityZero(int productId) throws SQLException {
        if (productId <= 0) throw new IllegalArgumentException("Product ID must be positive.");
        return productDAO.deleteIfQuantityZero(productId);
    }
}
