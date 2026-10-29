# Inventory Management System — JDBC + MySQL (Java 24)

Implements Question 1 from the MSE-2 sample PDF.

## Setup
1. Install JDK 24 and MySQL Server.
2. Open MySQL Workbench or MySQL command line.
3. Execute `src/main/resources/schema.sql`.
4. In `InventoryManagementApp.java`, replace `YOUR_MYSQL_PASSWORD` with your actual MySQL password.
5. Open the folder containing `pom.xml` in IntelliJ IDEA.
6. Set Project SDK and Maven importer JDK to 24.
7. Reload Maven and wait for MySQL Connector/J to download.
8. Run `InventoryManagementApp.java`.

## Menu
1. Insert three sample products (duplicate IDs are skipped).
2. Search by category, quantity < 20, sorted by price descending.
3. Add stock.
4. Display product and update price only if new price > 0.
5. Calculate SUM(quantity * price).
6. Delete only if quantity is zero.
0. Exit.

## Troubleshooting
- Access denied: verify MySQL username/password.
- Unknown database: execute `schema.sql`.
- Communications link failure: ensure MySQL Server is running and listening on port 3306.
- No suitable driver: reload Maven and verify Connector/J dependency.
