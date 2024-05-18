SELECT c.cid, c.first_name, SUM(p.payment) AS "total amount spent"
    FROM customers c
    JOIN purchases p ON c.cid = p.cid
    GROUP BY c.cid, c.first_name
    ORDER BY SUM(p.payment) DESC
    FETCH FIRST 4 ROWS ONLY
/
