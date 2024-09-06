-- Insert data into Payment_methods
INSERT INTO Payment_methods (name) VALUES 
('Credit Card'),
('Debit Card'),
('PayPal'),
('Bank Transfer');

-- Insert data into Statuses
INSERT INTO Statuses (name) VALUES 
('Pending'),
('Processing'),
('Shipped'),
('Delivered'),
('Cancelled');

-- Insert data into Prod_categories
INSERT INTO Prod_categories (name) VALUES 
('Electronics'),
('Clothing'),
('Home Appliances'),
('Books'),
('Toys');

-- Insert data into Carts
INSERT INTO Carts () VALUES 
(), 
(), 
();

-- Insert data into Users
INSERT INTO Users (password, username, is_admin, reg_date) VALUES 
('password123', 'john_doe', FALSE, '2024-01-01'),
('password456', 'jane_smith', TRUE, '2024-02-15'),
('password789', 'alice_jones', FALSE, '2024-03-20');

-- Insert data into Customers
INSERT INTO Customers (customer_id, email, address, cart_id) VALUES 
(1, 'john.doe@example.com', '123 Elm St', 1),
(2, 'jane.smith@example.com', '456 Oak St', 2),
(3, 'alice.jones@example.com', '789 Pine St', 3);

-- Insert data into Payment
INSERT INTO Payments (payment_met_id, amount, date) VALUES 
(1, 100.00, '2024-01-15'),
(2, 50.00, '2024-02-20'),
(3, 75.00, '2024-03-25'),
(4, 120.00, '2024-04-10');

-- Insert data into Products
INSERT INTO Products (category_id, name, description, unit_price, stock) VALUES 
(1, 'Smartphone', 'Latest model smartphone with advanced features', 699.99, 50),
(2, 'T-Shirt', 'Cotton T-shirt with cool design', 19.99, 150),
(3, 'Vacuum Cleaner', 'Powerful vacuum cleaner for home use', 129.99, 30),
(4, 'Novel', 'Bestselling novel', 14.99, 200),
(5, 'Toy Car', 'Remote-controlled toy car for kids', 39.99, 75);

-- Insert data into Orders
INSERT INTO Orders (customer_id, status_id, payment_id) VALUES 
(1, 1, 1),
(2, 2, 2),
(3, 3, 3);

-- Insert data into Items
INSERT INTO Items (product_id, amount, cart_id) VALUES 
(1, 2, 1),
(2, 3, 1),
(3, 1, 2),
(4, 5, 3),
(5, 2, 2);

-- If necessary, add items to orders
INSERT INTO Items (product_id, amount, order_id) VALUES 
(1, 1, 1),
(2, 2, 1),
(3, 1, 2),
(4, 3, 3),
(5, 2, 3);
