SELECT pr.pid, pr.name, MAX(pr.orig_price * pd.discnt_rate) AS "discount amount"
FROM products pr
JOIN prod_discnt pd ON pd.discnt_category = pr.discnt_category
GROUP BY pr.pid, pr.name
ORDER BY MAX(pr.orig_price * discnt_rate) DESC
FETCH FIRST 1 ROW ONLY
/
