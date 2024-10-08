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
('Shipped'),
('Delivered'),
('Cancelled'),
('Returned');

-- Insert data into Prod_categories
INSERT INTO Prod_categories (name) VALUES 
('Electronics'),
('Clothing'),
('Books'),
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
('$2b$12$DKcc2uGCDWxBXnZpx1NjsO1mD4Ap19bHShaupNHmFY3z0K0oRtrKi', 'John Doe', FALSE, '2024-01-01'),
('$2b$12$lE9k8ydrb8GGpjodqrVo8emeQxx5hZeM6mHkFN99E/bbn8vPamkoW', 'Jane Smith', TRUE, '2024-02-15'),
('$2b$12$xclFQYJke65JrmxrLI.HkuvnT8nxLtKG1lmXmg0BH2yq3K32PrpXy', 'Alice Jones', FALSE, '2024-03-20'),
('$2b$12$5n2BE40ZaCRuoGHl3hwHOeAuREHe7jL4VUKcQPUWHNlaXnkaPRO3S', 'Michael Brown', FALSE, '2024-04-05'),
('$2b$12$69Qfvzffx80FmxfSLch9ceVLrYUlogLhuQgAosHNOU3H.AsrXOCjS', 'Laura Wilson', TRUE, '2024-05-10'),
('$2b$12$VTdsDUA86VmZqjL/JYEkU.3pgjqgGICA5tO1PAYgoralG4dVUYSdi', 'Peter Clark', FALSE, '2024-06-15'),
('$2b$12$4HK7xxi6MBvsZohwJ3CBuuEvVYjex4wb44Yjb/vLIOHP8RHKyDMxa', 'Susan Taylor', FALSE, '2024-07-01'),
('$2b$12$1L9SH.yZH/30o9icAR9.6OapkFZm40MGt7ENpaBZpcGUlBRcVnUN2', 'David White', TRUE, '2024-08-01');

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
(1, 0, '2024-09-05'),
(2, 0, '2024-09-10'),
(3, 0, '2024-09-15'),
(4, 0, '2024-09-20'),
(5, 0, '2024-09-25'),
(6, 0, '2024-09-30'),
(7, 0, '2024-10-05'),
(8, 0, '2024-10-10'),
(1, 0, '2024-10-15'),
(2, 0, '2024-10-20'),
(3, 0, '2024-10-25'),
(4, 0, '2024-10-30'),
(5, 0, '2024-11-05'),
(4, 0, '2024-11-07');

-- Insert data into Products with image paths
INSERT INTO Products (category_id, name, description, unit_price, stock, image_path) VALUES
(1, 'IPhone 15 Pro Max', '6.7p 512GB 8GB RAM', 699.99, 50, 'IPhone_15_Pro_Max.jpg'),
(1, 'Samsung Galaxy S23', '6.6p 256GB 8GB RAM', 599.99, 45, 'Samsung_Galaxy_S23.jpg'),
(1, 'MacBook Air M2', '13.3p 256GB SSD 8GB RAM', 999.99, 30, 'MacBook_Air_M2.jpg'),
(1, 'Sony WH-1000XM5', 'Wireless Noise Cancelling Headphones', 349.99, 20, 'Sony_WH-1000XM5.jpg'),
(1, 'Apple Watch Series 9', '41mm GPS', 399.99, 40, 'Apple_Watch_Series_9.jpg'),

(3, 'The Catcher in the Rye', 'Paperback by J.D. Salinger', 9.99, 60, 'The_Catcher_in_the_Rye.jpg'),
(3, '1984', 'Paperback by George Orwell', 8.99, 70, '1984.jpg'),
(3, 'Sapiens', 'Hardcover by Yuval Noah Harari', 18.99, 50, 'Sapiens.jpg'),
(3, 'Educated', 'Paperback by Tara Westover', 11.99, 55, 'Educated.jpg'),
(3, 'Becoming', 'Hardcover by Michelle Obama', 22.99, 40, 'Becoming.jpg'),

(2, 'Levi\'s 501 Jeans', 'Classic Fit, Blue', 49.99, 100, 'Levi\'s_501_Jeans.jpg'),
(2, 'Nike Air Force 1', 'Men\'s Sneakers, White', 89.99, 75, 'Nike_Air_Force_1.jpg'),
(2, 'Adidas Ultraboost', 'Men\'s Running Shoes, Black', 129.99, 60, 'Adidas_Ultraboost.jpg'),
(2, 'Patagonia Down Jacket', 'Women\'s Puffer Jacket, Black', 199.99, 30, 'Patagonia_Down_Jacket.jpg'),
(2, 'The North Face Backpack', 'Borealis, 28L, Black', 99.99, 50, 'The_North_Face_Backpack.jpg'),

(4, 'Organic Almonds', '16oz Pack', 12.99, 80, 'Organic_Almonds.jpg'),
(4, 'Quinoa', '16oz Organic White Quinoa', 9.99, 90, 'Quinoa.jpg'),
(4, 'Green Tea', '100 Bags Organic', 7.99, 70, 'Green_Tea.jpg'),
(4, 'Dark Chocolate', '70% Cocoa, 200g', 5.99, 120, 'Dark_Chocolate.jpg'),
(4, 'Honey', 'Raw Organic 500g', 14.99, 50, 'Honey.jpg'),

(1, 'Lenovo IdeaPad', '15.6p 512GB SSD 16GB RAM', 499.99, 40, 'Lenovo_IdeaPad.jpg'),
(1, 'Dell XPS 13', '13.4p 512GB SSD 16GB RAM', 1199.99, 20, 'Dell_XPS_13.jpg'),
(1, 'GoPro HERO12', 'Action Camera, 5K', 399.99, 35, 'GoPro_HERO12.jpg'),
(1, 'JBL Flip 6', 'Portable Bluetooth Speaker', 129.99, 80, 'JBL_Flip_6.jpg'),
(1, 'Fitbit Charge 5', 'Fitness Tracker, Black', 149.99, 55, 'Fitbit_Charge_5.jpg'),

(3, 'The Subtle Art of Not Giving a F\'ck', 'Paperback by Mark Manson', 12.99, 65, 'The_Subtle_Art_of_Not_Giving_a_F\'ck.jpg'),
(3, 'The Power of Habit', 'Paperback by Charles Duhigg', 10.99, 70, 'The_Power_of_Habit.jpg'),
(3, 'Atomic Habits', 'Hardcover by James Clear', 16.99, 55, 'Atomic_Habits.jpg'),
(3, 'How to Win Friends and Influence People', 'Paperback by Dale Carnegie', 9.99, 75, 'How_to_Win_Friends_and_Influence_People.jpg'),
(3, 'Thinking, Fast and Slow', 'Paperback by Daniel Kahneman', 14.99, 60, 'Thinking,_Fast_and_Slow.jpg');

-- Insert data into Orders
INSERT INTO Orders (customer_id, status_id, payment_id) VALUES 
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 4),
(5, 5, 5),
(6, 1, 6),
(7, 2, 7),
(8, 1, 8),
(1, 3, 9),
(2, 1, 10),
(3, 2, 11),
(4, 3, 12),
(5, 4, 13),
(6, 5, 14),
(7, 1, 15),
(8, 2, 16),
(1, 3, 17),
(2, 1, 18),
(3, 2, 19),
(4, 3, 20);

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
(11, 1, 1),
(12, 2, 2),
(13, 1, 3),
(14, 2, 4),
(15, 3, 5),
(16, 1, 6),
(17, 2, 7),
(18, 1, 1),
(19, 3, 2),
(20, 2, 3);

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
(4, 1, 9),
(13, 1, 10),
(14, 2, 10),
(15, 1, 11),
(16, 3, 12),
(17, 2, 13),
(18, 1, 14),
(19, 3, 14),
(20, 2, 14),
(21, 1, 15),
(22, 2, 16),
(23, 1, 17),
(24, 3, 18),
(25, 2, 19),
(26, 1, 20);