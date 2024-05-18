-- to drop the triggers

drop trigger employee_insert_log_trigger;
drop trigger update_qoh_trigger;
drop trigger update_customer_trigger;
drop trigger update_last_visit_trigger;
drop trigger update_visits_made_trigger;
drop trigger purchase_insert_log_trigger;
drop trigger qoh_update_log_trigger;

/*      QUERY4      */
CREATE OR REPLACE TRIGGER employee_insert_log_trigger
AFTER INSERT ON employees
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, operation, op_time, table_name, tuple_pkey)
  VALUES (seqlog#.NEXTVAL, USER, 'insert', SYSDATE, 'employees', :new.eid);
END;
/
show errors

/*      QUERY5      */
CREATE OR REPLACE TRIGGER update_qoh_trigger
AFTER INSERT ON purchases
FOR EACH ROW
DECLARE
  v_qoh_threshold products.qoh_threshold%type;
  v_qoh products.qoh%type;
BEGIN
DBMS_OUTPUT.DISABLE();
DBMS_OUTPUT.ENABLE(20000);
    UPDATE products
    SET qoh = qoh - :new.quantity
    WHERE pid = :new.pid;

    SELECT qoh, qoh_threshold INTO v_qoh, v_qoh_threshold FROM products WHERE pid = :NEW.pid;
    IF (v_qoh < v_qoh_threshold) THEN
      DBMS_OUTPUT.PUT_LINE('The current qoh of the product is below the required threshold and new supply is required.');
      UPDATE products SET qoh = v_qoh_threshold + 20 WHERE pid = :NEW.pid;
      DBMS_OUTPUT.PUT_LINE('The new value of the qoh of the product is ' || (v_qoh_threshold + 20) || ' after new supply.');
    END IF;
END;
/
show errors

CREATE OR REPLACE TRIGGER update_customer_trigger
AFTER INSERT ON purchases
FOR EACH ROW
DECLARE
  v_last_visit_date customers.last_visit_date%type;
BEGIN
    UPDATE customers SET visits_made = visits_made + 1 WHERE cid = :new.cid;
    SELECT last_visit_date INTO v_last_visit_date FROM customers WHERE cid = :NEW.cid;
    IF v_last_visit_date IS NULL OR :new.pur_time > v_last_visit_date THEN
      UPDATE customers SET last_visit_date = :new.pur_time WHERE cid = :new.cid;  
    END IF;
END;
/
show errors

/*      QUERY6      */
CREATE OR REPLACE TRIGGER update_last_visit_trigger
AFTER UPDATE OF last_visit_date ON customers
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, operation, op_time, table_name, tuple_pkey)
  VALUES (seqlog#.NEXTVAL, USER, 'update', SYSDATE, 'customers', :NEW.cid);
END;
/
show errors

CREATE OR REPLACE TRIGGER update_visits_made_trigger
AFTER UPDATE OF visits_made ON customers
FOR EACH ROW
DECLARE
  v_last_visit_date customers.last_visit_date%type;
BEGIN
  -- IF :OLD.last_visit_date < :NEW.last_visit_date OR :OLD.last_visit_date IS NULL THEN    
    INSERT INTO logs (log#, user_name, operation, op_time, table_name, tuple_pkey)
    VALUES (seqlog#.NEXTVAL, USER, 'update', SYSDATE, 'customers', :NEW.cid);
  -- END IF;
END;
/
show errors

CREATE OR REPLACE TRIGGER purchase_insert_log_trigger
AFTER INSERT ON purchases
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, operation, op_time, table_name, tuple_pkey)
  VALUES (seqlog#.NEXTVAL, USER, 'insert', SYSDATE, 'purchases', :NEW.pur#);
END;
/
show errors

CREATE OR REPLACE TRIGGER qoh_update_log_trigger
AFTER UPDATE OF qoh ON products
FOR EACH ROW
BEGIN
  INSERT INTO logs (log#, user_name, operation, op_time, table_name, tuple_pkey)
  VALUES (seqlog#.NEXTVAL, USER, 'update', SYSDATE, 'products', :NEW.pid);
END;
/
show errors