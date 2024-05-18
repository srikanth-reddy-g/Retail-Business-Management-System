SELECT eid, name
FROM employees e
WHERE NOT EXISTS (
    SELECT *
    FROM products p
    WHERE discnt_category=3 AND NOT EXISTS (
        SELECT *
        FROM purchases pr
        WHERE e.eid=pr.eid AND pr.pid = p.pid
    )
)
/
