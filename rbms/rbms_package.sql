set serveroutput on;

CREATE OR REPLACE PACKAGE RBMS_PACKAGE 
AS 
TYPE st_cursor 
IS 
ref CURSOR;

PROCEDURE show_purchases(r_cursor OUT st_cursor );
PROCEDURE show_employees(r_cursor OUT st_cursor );
PROCEDURE show_customers(r_cursor OUT st_cursor );
PROCEDURE show_products(r_cursor OUT st_cursor );
PROCEDURE show_prod_discnt(r_cursor OUT st_cursor );
PROCEDURE show_logs(r_cursor OUT st_cursor );

PROCEDURE monthly_sale_activities(employee_id IN employees.eid%type, r_cursor OUT st_cursor );

PROCEDURE add_employee(e_id IN employees.eid%type, e_name IN employees.name%type, e_telephone# IN employees.telephone#%type, e_email IN employees.email%type, r_cursor OUT st_cursor );

PROCEDURE add_purchase(e_id IN purchases.eid%type, p_id IN purchases.pid%type, c_id IN purchases.cid%type, pur_qty IN purchases.quantity%type, pur_unit_price IN purchases.unit_price%type, r_cursor OUT st_cursor );

END;
/
show errors

CREATE OR REPLACE PACKAGE BODY RBMS_PACKAGE 
AS 
/*      QUERY2      */
-- Procedure to display the purchases table
PROCEDURE show_purchases(
r_cursor OUT st_cursor )
IS 
BEGIN
open r_cursor for 
select * from purchases order by pur# asc;
end show_purchases;

-- Procedure to display the employees table
PROCEDURE show_employees(
r_cursor OUT st_cursor )
IS 
BEGIN
open r_cursor for 
select * from employees order by eid asc;
end show_employees;

-- Procedure to display the customers table
PROCEDURE show_customers(
r_cursor OUT st_cursor )
IS 
BEGIN
open r_cursor for 
select * from customers order by cid asc;
end show_customers;

-- Procedure to display the products table
PROCEDURE show_products(
r_cursor OUT st_cursor )
IS 
BEGIN
open r_cursor for 
select * from products order by pid asc;
end show_products;

-- Procedure to display the prod_discnt table
PROCEDURE show_prod_discnt(
r_cursor OUT st_cursor )
IS 
BEGIN
open r_cursor for 
select * from prod_discnt;
end show_prod_discnt;

-- Procedure to display the logs table
PROCEDURE show_logs(
r_cursor OUT st_cursor )
IS 
BEGIN
open r_cursor for 
select * from logs order by log# asc;
end show_logs;

/*      QUERY3      */
-- Procedure to report the monthly sale activities for any given employee
PROCEDURE monthly_sale_activities(
  employee_id IN employees.eid%type,
  r_cursor OUT st_cursor
)
IS 
  employee_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO employee_count FROM employees WHERE eid = employee_id;		
  IF employee_count = 0 THEN	
    RAISE_APPLICATION_ERROR(-20001, 'Employee ID does not exist');		
  END IF;
  OPEN r_cursor FOR
  SELECT 
    employee_id as EID,
    e.name AS employee_name,
    TO_CHAR(p.pur_time, 'MON') AS month,
    TO_CHAR(p.pur_time, 'YYYY') AS year,
    COUNT(*) AS total_sales,
    SUM(p.quantity) AS total_quantity,
    SUM(p.payment) AS total_amount
  FROM purchases p
  INNER JOIN employees e ON e.eid = p.eid
  WHERE e.eid = employee_id
  GROUP BY employee_id, e.name, TO_CHAR(p.pur_time, 'MON'), TO_CHAR(p.pur_time, 'YYYY')
  ORDER BY year DESC, month DESC;
END monthly_sale_activities;


/*      QUERY4      */
-- Procedure for adding tuples to the Employees table
PROCEDURE add_employee(
  e_id IN employees.eid%type, 
  e_name IN employees.name%type, 
  e_telephone# IN employees.telephone#%type, 
  e_email IN employees.email%type, 
  r_cursor OUT st_cursor 
)
IS
  employee_count NUMBER;
BEGIN
  SELECT COUNT(*) INTO employee_count FROM employees WHERE eid = e_id;		
  IF employee_count > 0 THEN		
    RAISE_APPLICATION_ERROR(-20002, 'Employee ID exists');		
  END IF;
  INSERT INTO employees VALUES (e_id, e_name, e_telephone#, e_email);
    OPEN r_cursor FOR
    SELECT *
    FROM employees
    WHERE eid = e_id;
END add_employee;

/*      QUERY5      */
-- Procedure for adding tuples to the Purchases table
PROCEDURE add_purchase(
  e_id IN purchases.eid%type, 
  p_id IN purchases.pid%type, 
  c_id IN purchases.cid%type, 
  pur_qty IN purchases.quantity%type, 
  pur_unit_price IN purchases.unit_price%type, 
  r_cursor OUT st_cursor 
) 
IS
    v_pur# purchases.pur#%type;
    qoh_threshold products.qoh%type;
    v_orig_price products.orig_price%type;
    v_payment purchases.payment%type;
    v_saving purchases.saving%type;
    v_pur_time purchases.pur_time%type;
    new_qoh products.qoh%type;
    employee_count NUMBER;
    product_count NUMBER;
    customer_count NUMBER;
BEGIN
--Check if eid is present
    SELECT COUNT(*) INTO employee_count FROM employees WHERE eid = e_id;		
    IF employee_count = 0 THEN		
      RAISE_APPLICATION_ERROR(-20003, 'Employee ID does not exist');		
    END IF;
--Check if pid is present
    SELECT COUNT(*) INTO product_count FROM products WHERE pid = p_id;		
    IF product_count = 0 THEN		
      RAISE_APPLICATION_ERROR(-20004, 'Product ID does not exist');		
    END IF;
--Check if cid is present
    SELECT COUNT(*) INTO customer_count FROM customers WHERE cid = c_id;		
    IF customer_count = 0 THEN		
      RAISE_APPLICATION_ERROR(-20005, 'Customer ID does not exist');		
    END IF;

    SELECT qoh, qoh_threshold, orig_price INTO new_qoh, qoh_threshold, v_orig_price
    FROM products WHERE pid = p_id FOR UPDATE;
    IF pur_qty > new_qoh THEN
      RAISE_APPLICATION_ERROR(-20006, 'Insufficient quantity in stock.');
    END IF;
    IF pur_unit_price > v_orig_price THEN
      RAISE_APPLICATION_ERROR(-20007, 'Unit price cannot be greater than original price.');
    END IF;
-- Generate new purchase number and compute payment and savings
    v_pur# := seqpur#.NEXTVAL;
    v_payment := pur_qty * pur_unit_price;
    SELECT orig_price INTO v_orig_price FROM products WHERE pid = p_id;
    v_saving := (v_orig_price - pur_unit_price) * pur_qty;
-- Generate purchase time and insert new tuple into Purchases table
    v_pur_time := SYSDATE;
    INSERT INTO purchases(pur#, eid, pid, cid, pur_time, quantity, unit_price, payment, saving)
    VALUES(v_pur#, e_id, p_id, c_id, v_pur_time, pur_qty, pur_unit_price, v_payment, v_saving);

-- Return a cursor for the newly added purchase tuple
  OPEN r_cursor FOR SELECT * FROM purchases WHERE pur# = v_pur#;
END add_purchase;

end;
/
show errors

-- start sequences.sql
-- start tables.sql
-- start triggers.sql
-- start RBMS_PACKAGE.sql

-- VAR rc refcursor;

-- EXEC procedures.show_purchases(:rc);
-- EXEC procedures.show_employees(:rc);
-- EXEC procedures.show_customers(:rc);
-- EXEC procedures.show_products(:rc);
-- EXEC procedures.show_prod_discnt(:rc);
-- EXEC procedures.show_logs(:rc);

-- EXEC procedures.monthly_sale_activities('e04',:rc);

-- EXEC procedures.add_employee('e06','Ruchi','1234567','ruchi@gmail.com',:rc);

-- EXEC procedures.add_purchase('e06', 'p004', 'c008', 15, 0.98, :rc);

-- print rc;

-- select * from purchases;
-- select * from products;
-- select * from customers;
-- select * from employees;
-- select * from prod_discnt;
-- select * from logs;