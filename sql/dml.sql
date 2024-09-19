-- Insert data into Payment_methods
INSERT INTO Payment_methods (name) VALUES 
('Credit Card'),
('Debit Card'),
('PayPal'),
('Bank Transfer'),
('Apple Pay'),
('Google Pay'),
('Cryptocurrency'),
('Cash');

-- Insert data into Statuses
INSERT INTO Statuses (name) VALUES 
('Pending'),
('Processing'),
('Shipped'),
('Delivered'),
('Cancelled'),
('Returned'),
('Refunded'),
('Out of Stock'),
('Failed Payment');

-- Insert data into Prod_categories
INSERT INTO Prod_categories (name) VALUES 
('Electronics'),
('Clothing'),
('Home Appliances'),
('Books'),
('Toys'),
('Beauty & Health'),
('Sports & Outdoors'),
('Automotive'),
('Furniture'),
('Groceries');

-- Insert data into Carts
INSERT INTO Carts () VALUES 
(), 
(), 
(), 
(), 
(), 
(), 
(), 
(), 
();

-- Insert data into Users with hashed passwords
INSERT INTO Users (password, username, is_admin, reg_date) VALUES 
('$2b$12$DKcc2uGCDWxBXnZpx1NjsO1mD4Ap19bHShaupNHmFY3z0K0oRtrKi', 'john_doe', FALSE, '2024-01-01'),
('$2b$12$lE9k8ydrb8GGpjodqrVo8emeQxx5hZeM6mHkFN99E/bbn8vPamkoW', 'jane_smith', TRUE, '2024-02-15'),
('$2b$12$xclFQYJke65JrmxrLI.HkuvnT8nxLtKG1lmXmg0BH2yq3K32PrpXy', 'alice_jones', FALSE, '2024-03-20'),
('$2b$12$5n2BE40ZaCRuoGHl3hwHOeAuREHe7jL4VUKcQPUWHNlaXnkaPRO3S', 'michael_brown', FALSE, '2024-04-05'),
('$2b$12$69Qfvzffx80FmxfSLch9ceVLrYUlogLhuQgAosHNOU3H.AsrXOCjS', 'laura_wilson', TRUE, '2024-05-10'),
('$2b$12$VTdsDUA86VmZqjL/JYEkU.3pgjqgGICA5tO1PAYgoralG4dVUYSdi', 'peter_clark', FALSE, '2024-06-15'),
('$2b$12$4HK7xxi6MBvsZohwJ3CBuuEvVYjex4wb44Yjb/vLIOHP8RHKyDMxa', 'susan_taylor', FALSE, '2024-07-01'),
('$2b$12$1L9SH.yZH/30o9icAR9.6OapkFZm40MGt7ENpaBZpcGUlBRcVnUN2', 'david_white', TRUE, '2024-08-01');

-- Insert data into Customers
INSERT INTO Customers (customer_id, email, address, cart_id) VALUES 
(1, 'john.doe@example.com', '123 Elm St', 1),
(2, 'jane.smith@example.com', '456 Oak St', 2),
(3, 'alice.jones@example.com', '789 Pine St', 3),
(4, 'michael.brown@example.com', '321 Cedar St', 4),
(5, 'laura.wilson@example.com', '654 Birch St', 5),
(6, 'peter.clark@example.com', '987 Maple St', 6),
(7, 'susan.taylor@example.com', '1010 Walnut St', 7),
(8, 'david.white@example.com', '1112 Cherry St', 8);

-- Insert data into Payments
INSERT INTO Payments (payment_met_id, amount, date) VALUES 
(1, 0, '2024-01-15'),
(2, 0, '2024-02-20'),
(3, 0, '2024-03-25'),
(4, 0, '2024-04-10'),
(5, 0, '2024-05-05'),
(6, 0, '2024-06-10'),
(7, 0, '2024-07-20'),
(8, 0, '2024-08-15'),
(1, 0, '2024-09-05');

-- Insert data into Products
INSERT INTO Products (category_id, name, description, unit_price, stock) VALUES 
(1, 'Smartphone', 'Latest model smartphone with advanced features', 699.99, 50),
(2, 'T-Shirt', 'Cotton T-shirt with cool design', 19.99, 150),
(3, 'Vacuum Cleaner', 'Powerful vacuum cleaner for home use', 129.99, 30),
(4, 'Novel', 'Bestselling novel', 14.99, 200),
(5, 'Toy Car', 'Remote-controlled toy car for kids', 39.99, 75),
(6, 'Shampoo', 'Moisturizing shampoo for daily use', 8.99, 100),
(7, 'Basketball', 'Durable outdoor basketball', 25.99, 50),
(8, 'Car Seat Cover', 'Leather car seat cover, universal fit', 49.99, 80),
(9, 'Sofa', 'Comfortable 3-seater sofa', 399.99, 20),
(10, 'Organic Apples', 'Fresh organic apples, pack of 6', 4.99, 200),
(1, 'Laptop', 'High-performance laptop with 16GB RAM', 1199.99, 40),
(3, 'Microwave Oven', 'Compact microwave with quick-cook settings', 89.99, 60),
(2, 'Jacket', 'Waterproof jacket for outdoor activities', 59.99, 100),
(7, 'Tennis Racket', 'Professional-grade tennis racket', 199.99, 25),
(9, 'Dining Table', 'Wooden dining table with seating for six', 499.99, 15);

UPDATE Products SET image_path = 'images/product1' WHERE product_id = 1;

-- Insert data into Orders
INSERT INTO Orders (customer_id, status_id, payment_id) VALUES 
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5),
(6, 6, 6),
(7, 7, 7),
(8, 8, 8),
(1, 1, 9);

-- Insert data into Items (Cart Items)
INSERT INTO Items (product_id, amount, cart_id) VALUES 
(1, 2, 1),
(2, 3, 1),
(3, 1, 2),
(4, 5, 3),
(5, 2, 2),
(6, 3, 4),
(7, 1, 4),
(8, 2, 5),
(9, 1, 6),
(10, 5, 7),
(11, 1, 8),
(12, 2, 8);

-- Insert data into Items (Order Items)
INSERT INTO Items (product_id, amount, order_id) VALUES 
(1, 1, 1),
(2, 2, 1),
(3, 1, 2),
(4, 3, 3),
(5, 2, 3),
(6, 1, 4),
(7, 1, 4),
(8, 1, 5),
(9, 1, 6),
(10, 4, 7),
(11, 1, 8),
(12, 2, 9),
(1, 1, 9),
(3, 2, 9),
(4, 1, 9);
