CREATE TABLE RIDES (
id number(10) generated always as identity primary key,
rider_id number(10),
driver_id number(10),
pickup varchar2(500),
dropoff varchar2(500),
status number(10),
price float,
create_date timestamp,
update_date timestamp
);
