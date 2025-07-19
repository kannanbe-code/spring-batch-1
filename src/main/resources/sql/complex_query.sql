SELECT c.name AS customerName,
       p.name AS productName,
       o.quantity,
       TO_CHAR(o.order_date, 'YYYY-MM-DD') AS orderDate
FROM customers c
JOIN orders o ON c.id = o.customer_id
JOIN products p ON o.product_id = p.id
WHERE o.status = 'CONFIRMED';
