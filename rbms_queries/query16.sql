SELECT c.cid, c.first_name, COUNT(DISTINCT p.pid) AS num_products
FROM customers c
JOIN purchases p ON c.cid = p.cid
GROUP BY c.cid, c.first_name
ORDER BY c.cid
/
