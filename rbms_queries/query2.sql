SELECT customers.first_name || ' ' || customers.last_name as name
FROM customers, purchases
WHERE customers.phone# LIKE '666%'
  AND customers.cid = purchases.cid
  AND TO_CHAR(purchases.pur_time, 'YYYY-MM') = '2022-10'
/
