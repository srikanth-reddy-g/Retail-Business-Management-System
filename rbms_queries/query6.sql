COLUMN PUR_DATE FORMAT A30
SELECT
    pur#,
    (
        SELECT name
        FROM products
        WHERE pid = purchases.pid
    ) AS "product name",
    TO_CHAR(pur_time, 'Month DD, YYYY Day') AS pur_date,
    payment,
    saving
FROM
    purchases
/
