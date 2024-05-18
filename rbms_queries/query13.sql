SELECT c.cid, c.first_name || ' ' || c.last_name as name
FROM customers c
JOIN purchases p ON c.cid = p.cid
WHERE p.payment = (
    SELECT MAX(payment)
    FROM purchases
)
/
