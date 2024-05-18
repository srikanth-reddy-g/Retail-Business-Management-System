SELECT pid, name
FROM products
WHERE pid IN (
    SELECT pid
    FROM purchases
    WHERE purchases.unit_price < 10
    AND (TO_CHAR(purchases.pur_time, 'YYYY-MM') = '2022-08' OR TO_CHAR(purchases.pur_time, 'YYYY-MM') = '2022-09')
)
/
