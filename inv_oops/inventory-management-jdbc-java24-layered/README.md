# Inventory Management System — JDBC + MySQL (Java 24)

MSE-2 Sample Question 1 (10 marks). This is a plain Java console application using JDBC and MySQL, organized into Model, DAO, Service, Controller, and DBConnection classes.

## Requirements
- JDK 24
- MySQL Server running locally on port 3306
- IntelliJ IDEA with Maven support

## 1. Create the database and table
Open MySQL Workbench and run the SQL in `src/main/resources/schema.sql`, or open the file and execute it in your MySQL client.

## 2. Configure credentials
`src/main/resources/application.properties` is configured as requested:
- URL: `jdbc:mysql://localhost:3306/retail_db`
- Username: `root`
- Password: `admin`

Your MySQL account must already have these credentials. If it does not, change the properties to match your local MySQL setup. The project does not change your MySQL password.

## 3. Open and run
1. Extract this ZIP.
2. In IntelliJ IDEA, open the extracted folder containing `pom.xml`.
3. Set Project SDK to JDK 24.
4. Set Maven importer JDK to JDK 24 (Settings → Build Tools → Maven).
5. Reload Maven and wait for MySQL Connector/J to download.
6. Run `InventoryManagementApp.java`.

## Menu operations
1. Insert three sample products (IDs 101–103; duplicate IDs are skipped).
2. Retrieve products by category where quantity < 20, sorted by price descending.
3. Add stock to an existing product.
4. Find a product, validate a positive new price, update it, and display the updated details.
5. Calculate total inventory value with `SUM(quantity * price)`.
6. Delete a product only if its quantity is zero.
0. Exit.

## Architecture
- `model/Product.java`: product data and getters/setters.
- `dao/ProductDAO.java`: JDBC and SQL statements.
- `service/ProductService.java`: business validation.
- `controller/ProductController.java`: console menu operations.
- `util/DBConnection.java`: loads database settings and opens JDBC connections.
- `InventoryManagementApp.java`: application entry point.

## Troubleshooting
- `Access denied for user`: make sure the MySQL account uses the configured username and password.
- `Unknown database 'retail_db'`: execute `schema.sql`.
- `Communications link failure`: start MySQL Server and verify port 3306.
- `No suitable driver`: reload Maven and check that `mysql-connector-j` downloaded.
- `release version 24 not supported`: set both Project SDK and Maven importer JDK to JDK 24.
