USE tpi_db;

-- Insert sample data into Payment_methods
INSERT INTO Payment_methods (name) VALUES 
('Tarjeta de Crédito'),
('PayPal'),
('Transferencia Bancaria'),
('Efectivo');

-- Insert sample data into Statuses
INSERT INTO Statuses (name) VALUES 
('Pendiente'),
('Enviado'),
('Entregado'),
('Cancelado');

-- Insert sample data into Prod_categories
INSERT INTO Prod_categories (name) VALUES 
('Electrónica'),
('Ropa'),
('Libros'),
('Hogar y Cocina');

-- Insert sample data into Users
INSERT INTO Users (password, username, is_admin, reg_date) VALUES 
(MD5('contraseña123'), 'juan_perez', FALSE, '2023-01-15'),
(MD5('admin123'), 'usuario_admin', TRUE, '2023-01-10'),
(MD5('maria123'), 'maria_lopez', FALSE, '2023-02-20');

-- Insert sample data into Carts
INSERT INTO Carts () VALUES ();

-- Insert sample data into Customers
INSERT INTO Customers (customer_id, email, address, cart_id) VALUES 
(1, 'juan_perez@example.com', 'Calle Falsa 123, Ciudad', 1),
(3, 'maria_lopez@example.com', 'Av. Siempreviva 456, Ciudad', NULL);

-- Insert sample data into Products
INSERT INTO Products (category_id, name, description, unit_price, stock) VALUES 
(1, 'Smartphone', 'Último modelo con funciones avanzadas', 699.99, 50),
(1, 'Portátil', 'Portátil de alto rendimiento para juegos y trabajo', 1199.99, 30),
(2, 'Camiseta', 'Camiseta de algodón con logo', 19.99, 100),
(3, 'Libro', 'Novela más vendida', 14.99, 200),
(4, 'Licuadora', 'Licuadora de alta potencia con múltiples configuraciones', 89.99, 40);

-- Insert sample data into Payment
INSERT INTO Payment (payment_met_id, amount, date) VALUES 
(1, 1000.00, '2023-03-01');

-- Insert sample data into Orders
INSERT INTO Orders (cart_id, status_id, payment_id) VALUES 
(1, 1, 1);

-- Insert sample data into Items
INSERT INTO Items (product_id, amount, cart_id) VALUES 
(1, 1, 1),
(2, 3, 1),
(3, 2, 1),
(4, 1, 1);