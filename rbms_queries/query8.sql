SELECT *
FROM prod_discnt pd
WHERE NOT EXISTS (
SELECT discnt_category
FROM products
WHERE discnt_category = pd.discnt_category
)
/
