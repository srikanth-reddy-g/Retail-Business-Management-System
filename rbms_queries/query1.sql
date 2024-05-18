SELECT pid, name
FROM products
WHERE discnt_category IN (
    SELECT discnt_category
    FROM prod_discnt
    WHERE discnt_rate BETWEEN 0.1 AND 0.2
)
AND (qoh - 5) >= qoh_threshold
/
