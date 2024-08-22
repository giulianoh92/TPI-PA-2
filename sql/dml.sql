-- Insertar datos en Payment_methods
INSERT INTO Payment_methods (name) VALUES 
('Tarjeta de Crédito'),
('PayPal'),
('Transferencia Bancaria'),
('Efectivo');

-- Insertar datos en Statuses
INSERT INTO Statuses (name) VALUES 
('Pendiente'),
('Enviado'),
('Entregado'),
('Cancelado');

-- Insertar datos en Prod_categories
INSERT INTO Prod_categories (name) VALUES 
('Electrónica'),
('Ropa'),
('Libros'),
('Hogar y Cocina');

-- Insertar datos en Users
INSERT INTO Users (password, username, is_admin, reg_date) VALUES 
(MD5('contraseña123'), 'juan_perez', FALSE, '2023-01-15'),
(MD5('admin123'), 'usuario_admin', TRUE, '2023-01-10'),
(MD5('maria123'), 'maria_lopez', FALSE, '2023-02-20');

-- Insertar datos en Customers
INSERT INTO Customers (customer_id, email, address) VALUES 
(1, 'juan_perez@example.com', 'Calle Falsa 123, Ciudad'),
(3, 'maria_lopez@example.com', 'Av. Siempreviva 456, Ciudad');

-- Insertar datos en Products
INSERT INTO Products (category_id, name, description, unit_price, stock) VALUES 
(1, 'Smartphone', 'Último modelo con funciones avanzadas', 699.99, 50),
(1, 'Portátil', 'Portátil de alto rendimiento para juegos y trabajo', 1199.99, 30),
(2, 'Camiseta', 'Camiseta de algodón con logo', 19.99, 100),
(3, 'Libro', 'Novela más vendida', 14.99, 200),
(4, 'Licuadora', 'Licuadora de alta potencia con múltiples configuraciones', 89.99, 40);
