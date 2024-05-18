SELECT TO_CHAR(pur_time, 'YYYY/MM') AS Month,
       SUM(payment) AS "Total Sale"
FROM purchases
GROUP BY TO_CHAR(pur_time, 'YYYY/MM')
ORDER BY TO_CHAR(pur_time, 'YYYY/MM') ASC
/
