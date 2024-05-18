SELECT c.cid, c.first_name || ' ' || c.last_name AS name
    FROM customers c
    WHERE c.visits_made > ALL (
    	 SELECT visits_made
    	 FROM customers
    	 WHERE phone# LIKE '777%'
    )
    order by c.cid
/
