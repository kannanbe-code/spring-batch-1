-- Create schema tables
CREATE TABLE customers (
    customer_id NUMBER PRIMARY KEY,
    name VARCHAR2(100)
);

CREATE TABLE orders (
    order_id NUMBER PRIMARY KEY,
    customer_id NUMBER,
    order_date DATE
);

CREATE TABLE products (
    product_id NUMBER PRIMARY KEY,
    product_code VARCHAR2(20),
    price NUMBER
);

CREATE TABLE order_items (
    item_id NUMBER PRIMARY KEY,
    order_id NUMBER,
    product_id NUMBER,
    quantity NUMBER
);

-- Insert test data
INSERT INTO customers VALUES (1, 'Alice');
INSERT INTO customers VALUES (2, 'Bob');

INSERT INTO orders VALUES (101, 1, SYSDATE);
INSERT INTO orders VALUES (102, 2, SYSDATE);

INSERT INTO products VALUES (1001, 'P001', 99.99);
INSERT INTO products VALUES (1002, 'P002', 149.99);

INSERT INTO order_items VALUES (201, 101, 1001, 2);
INSERT INTO order_items VALUES (202, 101, 1002, 1);
INSERT INTO order_items VALUES (203, 102, 1001, 3);

COMMIT;
