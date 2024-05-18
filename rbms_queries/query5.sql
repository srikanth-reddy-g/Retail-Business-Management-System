SELECT pur#
FROM purchases
WHERE cid IN (
    SELECT cid
    FROM customers
    WHERE first_name LIKE 'K%'
)
AND pid IN (
    SELECT pid
    FROM products
    WHERE orig_price < 15
)
AND eid IN (
    SELECT eid
    FROM employees
    WHERE telephone# LIKE '888%'
)
/
