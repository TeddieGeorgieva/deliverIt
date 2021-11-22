INSERT INTO deliverit.countries (name)
values ('Bulgaria'), ('Romania');

INSERT INTO deliverit.countries (name)
values ('Greece'), ('Turkey'),
       ('Austria'), ('Germany'),
       ('France'), ('Switzerland');

INSERT INTO deliverit.cities (name, country_id)
values ('Varna', 1), ('Sofia', 1),
       ('Bucharest', 2), ('Timisoara', 2),
       ('Kozani', 3), ('Athens', 3),
       ('Istanbul', 4), ('Izmir', 4),
       ('Vienna', 5), ('Linz', 5),
       ('Berlin', 6), ('Nuremberg', 6),
       ('Paris', 7), ('Lyon', 7),
       ('Bern', 8), ('Zurich', 8);

INSERT INTO deliverit.addresses (street_name, city_id)
VALUES ('bul. Slivnitsa', 1), ('bul. Vasil Levski', 1),
       ('bul. Slivnitsa', 2), ('bul. Makedonia', 2),
       ('Odoarei', 3), ('Progresului', 3);

INSERT INTO deliverit.categories (name)
values ('Sport'), ('Luggage'),
       ('Electronics'), ('Clothing'),
       ('Home and Garden'), ('Pottery and Glass');

INSERT INTO deliverit.warehouses (address_id)
values (1), (2), (3), (4), (5), (6);


ALTER TABLE shipments
    ALTER status SET DEFAULT 'PREPARING';

INSERT INTO deliverit.shipments (shipment_id, origin_warehouse_id, destination_warehouse_id, departure_date, arrival_date)
VALUES (1, 1, 2, '2021-10-27', '2021-11-01');

INSERT INTO deliverit.shipments (shipment_id, origin_warehouse_id, destination_warehouse_id, departure_date, arrival_date)
VALUES (2, 2, 2, '2021-10-27', '2021-11-01');

INSERT INTO deliverit.shipments (shipment_id, origin_warehouse_id, destination_warehouse_id, departure_date, arrival_date)
VALUES (3, 1, 2, '2021-10-27', '2021-11-01');


INSERT INTO deliverit.warehouses (warehouse_id, address_id)
VALUES (1, 1);

INSERT INTO deliverit.addresses (street_name, city_id)
VALUES ('blvd. Dondukov 22', 1);
INSERT INTO deliverit.addresses (street_name, city_id)
VALUES ('blvd. Vasil Levski 17', 2);
INSERT INTO deliverit.addresses (street_name, city_id)
VALUES ('blvd. Hristo Botev 4', 3);


INSERT INTO deliverit.users (username, first_name, last_name, email, address_id)
VALUES ('gabi', 'Gabriela', 'Stoyanova', 'gabriela@deliverit.com', 1);
INSERT INTO deliverit.users (user_id, first_name, last_name, email, address_id, username)
VALUES (2, 'Teodora', 'Georgieva', 'test@test.com', 7, 'tg');
INSERT INTO deliverit.users (first_name, last_name, email, address_id, username)
VALUES ('Petar', 'Petrov', 'pesho@deliverit.com', 3, 'pesho');
INSERT INTO deliverit.users (username, password, email, first_name, last_name, address_id)
VALUES ('ivan', 'ivan', 'ivan@mail.com', 'Ivan', 'Ivanov', 5);

INSERT INTO deliverit.parcels (parcel_id, user_id, warehouse_id, weight, category_id, delivery_type)
VALUES (1, 1, 1, 12, 1,'TO_ADDRESS');
INSERT INTO deliverit.parcels (parcel_id, user_id, warehouse_id, weight, category_id, delivery_type)
VALUES (2, 1, 1, 9, 1, 'TO_ADDRESS');
INSERT INTO deliverit.parcels (parcel_id, user_id, warehouse_id, weight, category_id, delivery_type)
VALUES (3, 2, 1, 4, 1, 'TO_WAREHOUSE');
INSERT INTO deliverit.parcels (parcel_id, user_id, warehouse_id, weight, category_id, delivery_type)
VALUES (4, 1, 1, 2, 1, 'TO_WAREHOUSE');
INSERT INTO deliverit.parcels (parcel_id, user_id, warehouse_id, weight, category_id, delivery_type)
VALUES (5, 1, 1, 12, 1, 'TO_ADDRESS');


INSERT INTO deliverit.shipments_parcels (shipment_id, parcel_id)
VALUES (1, 2);
INSERT INTO deliverit.shipments_parcels (shipment_id, parcel_id)
VALUES (1, 3);
INSERT INTO deliverit.shipments_parcels (shipment_id, parcel_id)
VALUES (2, 3);
INSERT INTO deliverit.shipments_parcels (shipment_id, parcel_id)
VALUES (3, 1);


INSERT INTO deliverit.roles (role_name)
VALUES ('customer'), ('employee');


INSERT INTO deliverit.users_roles (user_id, role_id)
VALUES (1, 1), (2, 1), (1, 2),(3, 2);


INSERT INTO deliverit.shipments (origin_warehouse_id, destination_warehouse_id, departure_date, arrival_date, status)
VALUES (1, 2, '2021-11-03', '2021-11-15', 'On the way.');
UPDATE shipments SET status='ON_THE_WAY' WHERE shipment_id=5;

INSERT INTO deliverit.users_roles (user_id, role_id)
VALUES (4, 1)