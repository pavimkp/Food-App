USE food_ordering_db;

-- Row counts
SELECT 'restaurants' AS tbl, COUNT(*) AS row_count FROM restaurants
UNION ALL SELECT 'food_items', COUNT(*) FROM food_items
UNION ALL SELECT 'orders', COUNT(*) FROM orders
UNION ALL SELECT 'order_items', COUNT(*) FROM order_items;

-- Menu with restaurant names
SELECT r.name AS restaurant, f.id, f.name AS food, f.price, f.available
FROM food_items f JOIN restaurants r ON r.id = f.restaurant_id
ORDER BY r.id, f.id;

-- Orders with their lines
SELECT o.id AS order_id, o.customer_name, o.total_amount, o.status,
       f.name AS food, oi.quantity, oi.unit_price, oi.subtotal
FROM orders o
JOIN order_items oi ON oi.order_id = o.id
JOIN food_items f   ON f.id = oi.food_item_id
ORDER BY o.id, oi.id;

-- Does each order header total equal the sum of its lines? (diff should be 0.00)
SELECT o.id, o.total_amount, SUM(oi.subtotal) AS lines_total, o.total_amount - SUM(oi.subtotal) AS diff
FROM orders o JOIN order_items oi ON oi.order_id = o.id
GROUP BY o.id, o.total_amount;
