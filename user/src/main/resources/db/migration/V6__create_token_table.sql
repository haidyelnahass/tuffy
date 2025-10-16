CREATE TABLE TOKEN (
id number(10) generated always as identity primary key,
value varchar2(50),
user_id varchar2(500),
create_date timestamp,
expiry_date timestamp,
update_date timestamp,
CONSTRAINT FK_TOKEN_USER_ID FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);
