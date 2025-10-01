CREATE TABLE USER_TYPE (
id number(10) generated always as identity primary key,
value varchar2(50),
description varchar2(500),
create_date timestamp,
update_date timestamp
);

INSERT INTO USER_TYPE (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('RIDER', 'User is a rider', sysdate, sysdate);
INSERT INTO USER_TYPE (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('DRIVER', 'User is a driver', sysdate, sysdate);
