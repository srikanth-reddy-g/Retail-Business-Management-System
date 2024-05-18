SELECT pid, name
FROM products p
WHERE orig_price > 180
AND NOT EXISTS (
  SELECT *
  FROM employees e
  WHERE (name LIKE 'A%' OR name LIKE 'D%')
  AND NOT EXISTS (
    SELECT *
    FROM purchases pr
    WHERE p.pid = pr.pid
    AND e.eid = pr.eid
  )
)
/
