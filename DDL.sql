create table item(
    name varchar primary key ,
    type varchar,
    price int
);
create table company(
    id int primary key ,
    name varchar unique not null
);

create table city(
    id int primary key ,
    area_code varchar unique,
    name varchar unique not null
);

create table ship(
    id int primary key,
    name varchar unique not null ,
    company_id int references company(id)
);

create table container(
    code varchar primary key,
    type varchar
);

create table courier(
    id int primary key ,
    name varchar not null ,
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
    log_time timestamp not null
);

create table retrieval(
    item_name varchar primary key references item(name),
    courier_id int references courier(id) not null ,
    start_date date not null
);

create table delivery(
    item_name varchar primary key references item(name),
    courier_id int references courier(id) not null ,
    finish_date date not null
);

create table export(
    item_name varchar primary key references item(name),
    ship_id int references ship(id),
    container_code varchar references container(code),
    export_date date not null
);

create table import(
    item_name varchar primary key references item(name),
    import_date date not null
);