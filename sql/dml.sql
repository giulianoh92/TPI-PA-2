-- Insert sample data into Roles table
INSERT INTO Roles (name) VALUES ('Admin');
INSERT INTO Roles (name) VALUES ('Customer');
INSERT INTO Roles (name) VALUES ('Manager');

-- Insert sample data into Payment_methods table
INSERT INTO Payment_methods (name) VALUES ('Credit Card');
INSERT INTO Payment_methods (name) VALUES ('PayPal');
INSERT INTO Payment_methods (name) VALUES ('Bank Transfer');

-- Insert sample data into Statuses table
INSERT INTO Statuses (name) VALUES ('Pending');
INSERT INTO Statuses (name) VALUES ('Completed');
INSERT INTO Statuses (name) VALUES ('Cancelled');

-- Insert sample data into Prod_categories table
INSERT INTO Prod_categories (name) VALUES ('Electronics');
INSERT INTO Prod_categories (name) VALUES ('Clothing');
INSERT INTO Prod_categories (name) VALUES ('Home & Garden');

-- Insert sample data into Users table
INSERT INTO Users (password, username, email, role_id, reg_date) VALUES ('password123', 'admin_user', 'admin@example.com', 1, CURDATE());
INSERT INTO Users (password, username, email, role_id, reg_date) VALUES ('password123', 'customer_user', 'customer@example.com', 2, CURDATE());
INSERT INTO Users (password, username, email, role_id, reg_date) VALUES ('password123', 'manager_user', 'manager@example.com', 3, CURDATE());

-- Insert sample data into Customers table
INSERT INTO Customers (user_id, address) VALUES (2, '123 Main St, Anytown, USA');
INSERT INTO Customers (user_id, address) VALUES (3, '456 Elm St, Othertown, USA');

-- Insert sample data into Carts table
INSERT INTO Carts (creation_date, customer_id) VALUES (CURDATE(), 1);
INSERT INTO Carts (creation_date, customer_id) VALUES (CURDATE(), 2);

-- Insert sample data into Payment table
INSERT INTO Payment (payment_met_id) VALUES (1);
INSERT INTO Payment (payment_met_id) VALUES (2);

-- Insert sample data into Products table
INSERT INTO Products (category_id, name, description, unit_price, stock) VALUES (1, 'Smartphone', 'Latest model smartphone with high-resolution camera', 699.99, 50);
INSERT INTO Products (category_id, name, description, unit_price, stock) VALUES (2, 'T-Shirt', 'Comfortable cotton t-shirt available in various colors', 19.99, 200);
INSERT INTO Products (category_id, name, description, unit_price, stock) VALUES (3, 'Garden Hose', 'Durable garden hose with adjustable nozzle', 29.99, 75);

-- Insert sample data into Orders table
INSERT INTO Orders (cart_id, status_id, payment_id) VALUES (1, 1, 1);
INSERT INTO Orders (cart_id, status_id, payment_id) VALUES (2, 2, 2);

-- Insert sample data into Items table
INSERT INTO Items (product_id, amount, order_id) VALUES (1, 1, 1);
INSERT INTO Items (product_id, amount, order_id) VALUES (2, 3, 1);
INSERT INTO Items (product_id, amount, order_id) VALUES (3, 2, 2);
