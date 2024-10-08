USE tpi_db;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON tpi_db.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;

-- Drop tables if they exist
DROP TABLE IF EXISTS Items;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Products;
DROP TABLE IF EXISTS Payments;
DROP TABLE IF EXISTS Customers;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Carts;
DROP TABLE IF EXISTS Prod_categories;
DROP TABLE IF EXISTS Statuses;
DROP TABLE IF EXISTS Payment_methods;

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

CREATE TABLE Payments (
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
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    image_path VARCHAR(255),
    FOREIGN KEY (category_id) REFERENCES Prod_categories(category_id) ON DELETE SET NULL
);

CREATE TABLE Orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    status_id INT,
    payment_id INT,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE SET NULL,
    FOREIGN KEY (status_id) REFERENCES Statuses(status_id) ON DELETE SET NULL,
    FOREIGN KEY (payment_id) REFERENCES Payments(payment_id) ON DELETE SET NULL
);

CREATE TABLE Items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT,
    amount INT NOT NULL,
    cart_id INT NULL,
    order_id INT NULL,
    FOREIGN KEY (product_id) REFERENCES Products(product_id),
    FOREIGN KEY (cart_id) REFERENCES Carts(cart_id) ON DELETE SET NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE SET NULL
);

-- Create triggers to enforce exclusivity
DELIMITER //

CREATE TRIGGER check_item_exclusive_before_insert
BEFORE INSERT ON Items
FOR EACH ROW
BEGIN
    IF (NEW.cart_id IS NOT NULL AND NEW.order_id IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot have both cart_id and order_id set at the same time.';
    END IF;
END //

CREATE TRIGGER check_item_exclusive_before_update
BEFORE UPDATE ON Items
FOR EACH ROW
BEGIN
    IF (NEW.cart_id IS NOT NULL AND NEW.order_id IS NOT NULL) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Cannot have both cart_id and order_id set at the same time.';
    END IF;
END //

-- Trigger to update Payments.amount when an item is inserted
CREATE TRIGGER update_payment_amount_after_insert
AFTER INSERT ON Items
FOR EACH ROW
BEGIN
    DECLARE total_amount DECIMAL(10, 2);
    
    -- Calculate the total amount for the order
    SELECT SUM(i.amount * p.unit_price)
    INTO total_amount
    FROM Items i
    JOIN Products p ON i.product_id = p.product_id
    WHERE i.order_id = NEW.order_id;
    
    -- Update the Payments table with the calculated amount
    UPDATE Payments
    SET amount = total_amount
    WHERE payment_id = (SELECT payment_id FROM Orders WHERE order_id = NEW.order_id);
END //

-- Trigger to update Payments.amount when an item is updated
CREATE TRIGGER update_payment_amount_after_update
AFTER UPDATE ON Items
FOR EACH ROW
BEGIN
    DECLARE total_amount DECIMAL(10, 2);
    
    -- Calculate the total amount for the order
    SELECT SUM(i.amount * p.unit_price)
    INTO total_amount
    FROM Items i
    JOIN Products p ON i.product_id = p.product_id
    WHERE i.order_id = NEW.order_id;
    
    -- Update the Payments table with the calculated amount
    UPDATE Payments
    SET amount = total_amount
    WHERE payment_id = (SELECT payment_id FROM Orders WHERE order_id = NEW.order_id);
END //

-- Trigger to update Payments.amount when an item is deleted
CREATE TRIGGER update_payment_amount_after_delete
AFTER DELETE ON Items
FOR EACH ROW
BEGIN
    DECLARE total_amount DECIMAL(10, 2);
    
    -- Calculate the total amount for the order
    SELECT SUM(i.amount * p.unit_price)
    INTO total_amount
    FROM Items i
    JOIN Products p ON i.product_id = p.product_id
    WHERE i.order_id = OLD.order_id;
    
    -- Update the Payments table with the calculated amount
    UPDATE Payments
    SET amount = total_amount
    WHERE payment_id = (SELECT payment_id FROM Orders WHERE order_id = OLD.order_id);
END //

DELIMITER ;