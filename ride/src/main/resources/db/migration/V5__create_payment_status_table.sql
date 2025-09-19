CREATE TABLE PAYMENT_STATUS (
id number(10) generated always as identity primary key,
value varchar2(50),
description varchar2(500),
create_date timestamp,
update_date timestamp
);

INSERT INTO PAYMENT_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('PAYMENT_SUCCESSFUL', 'Payment successful', sysdate, sysdate);
INSERT INTO PAYMENT_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('PAYMENT_FAILED', 'Payment failed', sysdate, sysdate);

ALTER TABLE RIDES ADD CONSTRAINT fk_payment_status FOREIGN KEY (PAYMENT_STATUS) REFERENCES PAYMENT_STATUS(ID);
