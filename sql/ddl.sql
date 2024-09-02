DROP DATABASE IF EXISTS tpi_db;
CREATE DATABASE tpi_db;
USE tpi_db;

-- Drop the User if it exists, then create it and grant permissions
DROP USER IF EXISTS 'admin'@'localhost';
CREATE USER 'admin'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON tpi_db.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;

-- Create Tables

CREATE TABLE Payment_methods (
    payment_met_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Statuses (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Prod_categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    is_admin BOOLEAN,
    reg_date DATE
);

CREATE TABLE Customers (
    customer_id INT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    cart_id INT,
    FOREIGN KEY (cart_id) REFERENCES Carts(cart_id) ON DELETE SET NULL,
    FOREIGN KEY (customer_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    payment_met_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (payment_met_id) REFERENCES Payment_methods(payment_met_id) ON DELETE SET NULL
);

CREATE TABLE Products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    unit_price DECIMAL(10, 2) NOT NULL,
    stock INT UNSIGNED NOT NULL,
    FOREIGN KEY (category_id) REFERENCES Prod_categories(category_id) ON DELETE SET NULL
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT,
    status_id INT,
    payment_id INT,
    FOREIGN KEY (cart_id) REFERENCES Carts(cart_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES Statuses(status_id) ON DELETE SET NULL,
    FOREIGN KEY (payment_id) REFERENCES Payment(payment_id) ON DELETE SET NULL
);

CREATE TABLE Items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    amount INT NOT NULL,
    cart_id INT,
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (cart_id) REFERENCES Carts(cart_id)
);