SELECT eid, name
FROM employees
WHERE eid NOT IN (
    SELECT eid
    FROM purchases
)
/
