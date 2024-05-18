SELECT eid, name
FROM employees e
WHERE NOT EXISTS (
    SELECT *
    FROM purchases
    WHERE eid = e.eid
)
/
