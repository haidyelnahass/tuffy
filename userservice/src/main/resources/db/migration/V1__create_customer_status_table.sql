CREATE TABLE CUSTOMER_STATUS (
id number(10) generated always as identity primary key,
value varchar2(50),
description varchar2(500),
create_date timestamp,
update_date timestamp
);

INSERT INTO CUSTOMER_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('PENDING', 'User created a new account', sysdate, sysdate);
INSERT INTO CUSTOMER_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('ACTIVE', 'User is in active status, can perform transactions', sysdate, sysdate);
INSERT INTO CUSTOMER_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('BLOCKED', 'User entered wrong passcode too many times!', sysdate, sysdate);
INSERT INTO CUSTOMER_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('INACTIVE', 'User deactivated his account', sysdate, sysdate);
