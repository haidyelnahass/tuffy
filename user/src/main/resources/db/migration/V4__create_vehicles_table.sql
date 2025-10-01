CREATE TABLE VEHICLE (
id number(10) generated always as identity primary key,
driver_id number(10),
plate_number varchar2(500),
model varchar2(100),
capacity varchar2(100),
create_date timestamp,
update_date timestamp,
CONSTRAINT FK_DRIVER_ID FOREIGN KEY (DRIVER_ID) REFERENCES USERS (ID)
);
