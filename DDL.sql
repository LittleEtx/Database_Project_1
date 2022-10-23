create table company(
    id int primary key ,
    name varchar
);

create table city(
    id int primary key ,
    name varchar
);

create table ship(
    id int primary key ,
    name varchar,
    company_id int references company(id)
);

create table container(
    code varchar primary key,
    type varchar
);

create table courier(
    id int primary key ,
    name varchar,
    gender varchar,
    phone_number varchar,
    age int,
    company_id int references company(id)
);

create table tax_info(
    id int primary key ,
    export_tax decimal(30,15),
    import_tax decimal(30,15)
);

create table item(
    id int primary key ,
    name varchar,
    price int,
    type varchar
);

create table route(
    id int primary key ,
    retrieval_city int references city(id),
    export_city_id int references city(id),
    import_city_id int references city(id),
     delivery_city_id int references city(id)
);

create table logs(
    id int primary key ,
    log_time date,
    item_id int references item(id),
    tax_info_id int references tax_info(id),
    route_id int references route(id)
);

create table retrieve(
    log_id int primary key references logs(id),
    courier_id int references courier(id),
    start_date date
);

create table delivery(
    log_id int primary key references logs(id),
    courier_id int references courier(id),
    finish_date date
);

create table export(
     log_id int primary key references logs(id),
    ship_id int references ship(id),
    container_code varchar references container(code),
    export_date date
);

create table import(
    log_id int primary key references logs(id),
     import_date date
);