SELECT p.pid, pr.name, SUM(p.quantity) AS "total quantity sold"
FROM purchases p
JOIN products pr ON p.pid = pr.pid
GROUP BY p.pid, pr.name
HAVING SUM(p.quantity) = (
    SELECT MAX(SUM(quantity))
    FROM purchases
    GROUP BY pid
)
ORDER BY p.pid
/
