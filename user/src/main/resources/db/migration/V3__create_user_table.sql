CREATE TABLE USERS (
id number(10) generated always as identity primary key,
password varchar2(500),
email varchar2(100),
name varchar2(100),
login_attempts number(10),
create_date timestamp,
update_date timestamp,
customer_status number(10),
previous_customer_status number(10),
user_type number(10),
phone varchar2(10),
CONSTRAINT CUSTOMER_STATUS_VALUE  FOREIGN KEY (CUSTOMER_STATUS) REFERENCES CUSTOMER_STATUS (ID),
CONSTRAINT PREVIOUS_CUSTOMER_STATUS_VALUE  FOREIGN KEY (PREVIOUS_CUSTOMER_STATUS) REFERENCES CUSTOMER_STATUS (ID),
CONSTRAINT FK_USERTYPE FOREIGN KEY (USER_TYPE) REFERENCES USER_TYPE (ID)
);
