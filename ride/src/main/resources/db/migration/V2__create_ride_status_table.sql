CREATE TABLE RIDE_STATUS (
id number(10) generated always as identity primary key,
value varchar2(50),
description varchar2(500),
create_date timestamp,
update_date timestamp
);

INSERT INTO RIDE_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('REQUESTED', 'User Requested a ride', sysdate, sysdate);
INSERT INTO RIDE_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('ASSIGNED', 'A driver has been assigned to the ride', sysdate, sysdate);
INSERT INTO RIDE_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('STARTED', 'Ride started', sysdate, sysdate);
INSERT INTO RIDE_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('COMPLETED', 'Ride completed', sysdate, sysdate);
INSERT INTO RIDE_STATUS (VALUE, DESCRIPTION, CREATE_DATE, UPDATE_DATE) VALUES ('CANCELLED', 'Ride cancelled', sysdate, sysdate);


ALTER TABLE RIDES ADD CONSTRAINT fk_status FOREIGN KEY (STATUS) REFERENCES RIDE_STATUS(ID);
