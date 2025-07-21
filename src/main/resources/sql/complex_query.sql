SELECT o.order_id,
       c.name AS customer_name,
       p.product_code,
       oi.quantity,
       p.price
  FROM orders o
  JOIN customers c ON o.customer_id = c.customer_id
  JOIN order_items oi ON o.order_id = oi.order_id
  JOIN products p ON oi.product_id = p.product_id
