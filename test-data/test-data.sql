INSERT INTO customers (id, name) VALUES (1, 'John Doe');
INSERT INTO products (id, name) VALUES (1, 'Laptop');
INSERT INTO orders (id, customer_id, product_id, quantity, order_date, status)
VALUES (1, 1, 1, 2, TO_DATE('2024-07-01', 'YYYY-MM-DD'), 'CONFIRMED');
