drop SEQUENCE seqlog#;
drop SEQUENCE seqpur#;

/*      QUERY1      */
-- create a sequence to automatically generate unique values for log#
CREATE SEQUENCE seqlog#
START WITH 1001
INCREMENT BY 1;
-- create a sequence to automatically generate unique values for pur#
CREATE SEQUENCE seqpur#
START WITH 10001
INCREMENT BY 1;