SELECT customers.first_name, customers.phone#
FROM customers, purchases
WHERE purchases.payment >= 100
  AND customers.cid = purchases.cid
  AND TO_CHAR(purchases.pur_time, 'YYYY-MM') = '2022-10'
/
