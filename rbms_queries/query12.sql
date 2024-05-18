SELECT c.cid
FROM customers c
WHERE to_char(c.last_visit_date, 'yyyy-mm') = '2022-08'
AND NOT EXISTS (
  SELECT *
  FROM products p
  WHERE (orig_price BETWEEN 15 AND 20)
  AND NOT EXISTS (
    SELECT *
    FROM purchases pr
    WHERE c.cid = pr.cid
    AND p.pid = pr.pid
  )
)
/
