SELECT first_name || ' ' || last_name AS name
FROM customers
WHERE visits_made > 1
  AND cid NOT IN (
    SELECT cid
    FROM purchases
    WHERE cid = customers.cid
      AND pur_time > SYSDATE - 100
  )
/
