SELECT pd.pid, pd.name
FROM Products pd
WHERE pd.pid IN (
    SELECT DISTINCT pr.pid
    FROM purchases pr
)
ORDER BY (
    SELECT MAX(pr.pur_time)
    FROM purchases pr
    WHERE pr.pid = pd.pid
)
FETCH FIRST 1 ROW ONLY
/
