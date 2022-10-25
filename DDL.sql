create table item(
    code int primary key ,
    type varchar,
    price int
);
create table company(
    id int primary key ,
    name varchar
);

create table city(
    id int primary key ,
    area_code int ,
    name varchar
);

create table ship(
    id int primary key,
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
   birth_year numeric(4),
    company_id int references company(id)
);

create table tax_info(
    item_id int primary key references item(id),
    export_tax decimal(30,15),
    import_tax decimal(30,15)
);



create table route(
    item_id int primary key references item(id),
    retrieval_city int references city(id),
    export_city_id int references city(id),
    import_city_id int references city(id),
     delivery_city_id int references city(id),
     unique(retrieval_city,export_city_id,import_city_id,delivery_city_id)
);

create table logs(
    item_id int primary key references item(id),
    log_time date
);

create table retrieve(
    item_id int primary key references item(id),
    courier_id int references courier(id),
    start_date date
);

create table delivery(
    item_id int primary key references item(id),
    courier_id int references courier(id),
    finish_date date
);

create table export(
     item_id int primary key references item(id),
    ship_id int references ship(id),
    container_code varchar references container(code),
    export_date date
);

create table import(
    item_id int primary key references item(id),
     import_date date
);