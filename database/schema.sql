CREATE DATABASE IF NOT EXISTS clothify_store;
USE clothify_store;

-- ========== USERS ==========
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password_hash, full_name, role)
SELECT 'admin', 'admin123', 'System Admin', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users (username, password_hash, full_name, role)
SELECT 'staff', 'staff123', 'Staff User', 'STAFF'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'staff');

-- ========== SUPPLIERS ==========
CREATE TABLE IF NOT EXISTS suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    contact_person VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO suppliers (name, contact_person, phone, email, address)
SELECT 'Fashion Hub Ltd', 'John Silva', '0771234567', 'john@fashionhub.lk', 'No. 45, Galle Road, Colombo 03'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Fashion Hub Ltd');

INSERT INTO suppliers (name, contact_person, phone, email, address)
SELECT 'Textile World', 'Nimal Perera', '0779876543', 'nimal@textileworld.lk', 'No. 12, Kandy Road, Peradeniya'
WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'Textile World');

-- ========== EMPLOYEES ==========
CREATE TABLE IF NOT EXISTS employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    role ENUM('ADMIN','STAFF') NOT NULL DEFAULT 'STAFF',
    hire_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO employees (full_name, phone, email, role, hire_date)
SELECT 'System Admin', '0770000000', 'admin@clothify.lk', 'ADMIN', '2025-01-01'
WHERE NOT EXISTS (SELECT 1 FROM employees WHERE full_name = 'System Admin');

INSERT INTO employees (full_name, phone, email, role, hire_date)
SELECT 'Staff User', '0771111111', 'staff@clothify.lk', 'STAFF', '2025-06-01'
WHERE NOT EXISTS (SELECT 1 FROM employees WHERE full_name = 'Staff User');

-- ========== PRODUCTS ==========
CREATE TABLE IF NOT EXISTS products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(120) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    quantity INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P001', 'Slim Fit T-Shirt', 2500.00, 45
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P001');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P002', 'Classic Denim Jeans', 4200.00, 30
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P002');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P003', 'Summer Floral Dress', 3800.00, 25
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P003');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P004', 'Winter Jacket', 8500.00, 8
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P004');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P005', 'Formal Shirt', 3200.00, 40
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P005');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P006', 'Cotton Polo Shirt', 1800.00, 60
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P006');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P007', 'Cargo Pants', 3500.00, 20
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P007');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P008', 'Hoodie Sweatshirt', 4500.00, 15
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P008');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P009', 'Linen Shorts', 2200.00, 35
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P009');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P010', 'Leather Belt', 1500.00, 50
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P010');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P011', 'Silk Scarf', 2800.00, 5
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P011');

INSERT INTO products (product_code, name, unit_price, quantity)
SELECT 'P012', 'Sports Tracksuit', 5500.00, 12
WHERE NOT EXISTS (SELECT 1 FROM products WHERE product_code = 'P012');

-- ========== ORDERS ==========
CREATE TABLE IF NOT EXISTS orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cashier_user_id INT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    status ENUM('COMPLETED','CANCELLED') NOT NULL DEFAULT 'COMPLETED',
    FOREIGN KEY (cashier_user_id) REFERENCES users(user_id)
);

-- ========== ORDER DETAILS ==========
CREATE TABLE IF NOT EXISTS order_details (
    order_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(120) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    line_total DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);
