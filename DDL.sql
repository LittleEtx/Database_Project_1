create table item(
    name varchar primary key ,
    type varchar,
    price int
);
create table company(
    id int primary key ,
    name varchar unique
);

create table city(
    id int primary key ,
    area_code varchar unique,
    name varchar unique
);

create table ship(
    id int primary key,
    name varchar unique ,
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
    phone_number varchar unique,
    birthday date,
    company_id int references company(id)
);

create table tax_info(
    item_name varchar primary key references item(name),
    export_tax decimal(30,15),
    import_tax decimal(30,15)
);


create table item_via_city(
    item_name varchar primary key references item(name),
    retrieval_city int references city(id),
    export_city_id int references city(id),
    import_city_id int references city(id),
     delivery_city_id int references city(id)
);

create table log(
    item_name varchar primary key references item(name),
    log_time timestamp
);

create table retrieve(
    item_name varchar primary key references item(name),
    courier_id int references courier(id),
    start_date date
);

create table delivery(
    item_name varchar primary key references item(name),
    courier_id int references courier(id),
    finish_date date
);

create table export(
    item_name varchar primary key references item(name),
    ship_id int references ship(id),
    container_code varchar references container(code),
    export_date date
);

create table import(
    item_name varchar primary key references item(name),
    import_date date
);