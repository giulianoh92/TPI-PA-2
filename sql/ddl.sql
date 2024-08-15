-- Drop the database if it exists, then create it
DROP DATABASE IF EXISTS tpi_db;
CREATE DATABASE tpi_db;
USE tpi_db;

-- Drop the User if it exists, then create it and grant permissions
DROP USER IF EXISTS 'admin'@'localhost';
CREATE USER 'admin'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON tpi_db.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;

-- Create Tables
CREATE TABLE Roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

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

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role_id INT,
    reg_date DATE,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id) ON DELETE SET NULL
);

CREATE TABLE Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    address VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

CREATE TABLE Carts (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    creation_date DATE NOT NULL,
    customer_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE SET NULL
);

CREATE TABLE Payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    payment_met_id INT,
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
    order_id INT,
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id)
);

