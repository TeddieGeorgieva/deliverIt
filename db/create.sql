create table categories
(
    category_id int auto_increment,
    name        varchar(20) not null,
    constraint categories_category_id_uindex
        unique (category_id),
    constraint categories_name_uindex
        unique (name)
);

alter table categories
    add primary key (category_id);

create table countries
(
    country_id int auto_increment,
    name       int not null,
    constraint countries_country_id_uindex
        unique (country_id),
    constraint countries_name_uindex
        unique (name)
);

alter table countries
    add primary key (country_id);

create table cities
(
    city_id    int auto_increment,
    name       int not null,
    country_id int not null,
    constraint cities_city_id_uindex
        unique (city_id),
    constraint cities_name_uindex
        unique (name),
    constraint cities_countries_fk
        foreign key (country_id) references countries (country_id)
);

alter table cities
    add primary key (city_id);

create table addresses
(
    address_id  int auto_increment,
    street_name varchar(50) null,
    city_id     int         not null,
    constraint addresses_address_id_uindex
        unique (address_id),
    constraint addresses_cities_fk
        foreign key (city_id) references cities (city_id)
);

alter table addresses
    add primary key (address_id);

create table delivery_types
(
    delivery_type_id int auto_increment,
    type_name        varchar(20) not null,
    constraint delivery_types_delivery_type_id_uindex
        unique (delivery_type_id),
    constraint delivery_types_type_name_uindex
        unique (type_name)
);

alter table delivery_types
    add primary key (delivery_type_id);

create table emails
(
    email_id int auto_increment,
    email    int not null,
    constraint emails_email_id_uindex
        unique (email_id),
    constraint emails_email_uindex
        unique (email)
);

alter table emails
    add primary key (email_id);

create table customers
(
    customer_id int auto_increment,
    first_name  varchar(20) not null,
    last_name   varchar(20) not null,
    email_id    int         null,
    address_id  int         null,
    constraint customers_id_uindex
        unique (customer_id),
    constraint customers_addresses_fk
        foreign key (address_id) references addresses (address_id),
    constraint customers_emails_fk
        foreign key (email_id) references emails (email_id)
);

alter table Users
    add primary key (user_id);

create table employees
(
    employee_id int auto_increment,
    first_name  varchar(20) not null,
    last_name   varchar(20) not null,
    email_id    int         not null,
    address_id  int         not null,
    constraint employees_id_uindex
        unique (employee_id),
    constraint employees_address_fk
        foreign key (address_id) references addresses (address_id),
    constraint employees_emails_fk
        foreign key (email_id) references emails (email_id)
);

alter table employees
    add primary key (employee_id);

create table statuses
(
    status_id int auto_increment,
    name      int not null,
    constraint statuses_name_uindex
        unique (name),
    constraint statuses_status_id_uindex
        unique (status_id)
);

alter table statuses
    add primary key (status_id);

create table warehouses
(
    warehouse_id int auto_increment,
    address_id   int null,
    constraint warehouses_warehouse_id_uindex
        unique (warehouse_id),
    constraint warehouses_addresses_fk
        foreign key (address_id) references addresses (address_id)
);

alter table warehouses
    add primary key (warehouse_id);

create table parcels
(
    parcel_id        int auto_increment,
    customer_id      int null,
    warehouse_id     int null,
    weight           int not null,
    category_id      int null,
    delivery_type_id int null,
    constraint parcels_parcel_id_uindex
        unique (parcel_id),
    constraint parcels_categories_fk
        foreign key (category_id) references categories (category_id),
    constraint parcels_customers_fk
        foreign key (customer_id) references Users (user_id),
    constraint parcels_delivery_types_fk
        foreign key (delivery_type_id) references delivery_types (delivery_type_id),
    constraint parcels_warehouses_fk
        foreign key (warehouse_id) references warehouses (warehouse_id)
);

alter table parcels
    add primary key (parcel_id);

create table shipments
(
    shipment_id              int auto_increment,
    origin_warehouse_id      int  not null,
    destination_warehouse_id int  null,
    departure_date           date null,
    arrival_date             date null,
    status_id                int  null,
    constraint shipments_shipment_id_uindex
        unique (shipment_id),
    constraint shipments_dest_warehouses_fk
        foreign key (destination_warehouse_id) references warehouses (warehouse_id),
    constraint shipments_statuses_fk
        foreign key (status_id) references statuses (status_id),
    constraint shipments_warehouses_fk
        foreign key (origin_warehouse_id) references warehouses (warehouse_id)
);

alter table shipments
    add primary key (shipment_id);

create table shipments_parcels
(
    shipment_id int not null,
    parcel_id   int null,
    constraint shipments_parcels_parcels_fk
        foreign key (parcel_id) references parcels (parcel_id),
    constraint shipments_parcels_shipments_fk
        foreign key (shipment_id) references shipments (shipment_id)
);


