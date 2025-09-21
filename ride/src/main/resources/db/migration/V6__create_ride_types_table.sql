CREATE TABLE RIDE_TYPE (
id number(10) generated always as identity primary key,
type varchar2(50),
description varchar2(500),
base_price decimal(10,2),
price_per_km decimal(10,2),
price_per_min decimal(10,2),
create_date timestamp,
update_date timestamp
);

INSERT INTO RIDE_TYPE (type, description, base_price, price_per_km, price_per_min) VALUES ('Mini Tuffy', 'Flexible Riding Options', 10.50, 0.25, 0.60);
INSERT INTO RIDE_TYPE (type, description, base_price, price_per_km, price_per_min) VALUES ('Tuffy', 'Normal Ride', 12.50, 0.55, 0.85);
INSERT INTO RIDE_TYPE (type, description, base_price, price_per_km, price_per_min) VALUES ('Mega Tuffy', 'For larger groups', 14.50, 0.75, 1.60);
