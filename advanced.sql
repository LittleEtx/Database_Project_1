
-- container
select container_code,case
    when maxim>maxex
then maxim-minex
    else maxex-minex
end as served_time
    from
(select container_code ,max(import_date) as maxim,min(export_date) as minex,max(export_date) as maxex from
(select  container_code,export_date,i.import_date as import_date
 from (export LEFT JOIN (select distinct import.item_name,import_date from import) i  on export.item_name = i.item_name)) t1
group by container_code) t2;


-- retrieval

with info as(select t2.name as name,city_name,company.name as company_name from
(select name,city_name,company_id from
(select courier_id,city_name from
retrieval left join (select item_name,name as city_name from
(select item_name,retrieval_city from item_via_city) temp
 left join city on temp.retrieval_city=city.id)city_n on retrieval.item_name = city_n.item_name)t1
left join courier on t1.courier_id=courier.id)t2
left join company on company_id=company.id)

select name,city_name,company_name,num from
(select name,city_name,company_name,count(*) as num from info group by name,city_name,company_name)p3
where (city_name,company_name,num) in
(select city_name,company_name,max(num) from
(select name,city_name,company_name,count(*) as num from info group by name,city_name,company_name)p1
group by city_name, company_name);

--delivery courier
with info as(select t2.name as name,city_name,company.name as company_name from
(select name,city_name,company_id from
(select courier_id,city_name from
delivery left join (select item_name,name as city_name from
(select item_name,delivery_city_id from item_via_city) temp
 left join city on temp.delivery_city_id=city.id)city_n on delivery.item_name = city_n.item_name)t1
left join courier on t1.courier_id=courier.id)t2
left join company on company_id=company.id)

select name,city_name,company_name,num from
(select name,city_name,company_name,count(*) as num from info group by name,city_name,company_name)p3
where (city_name,company_name,num) in
(select city_name,company_name,max(num) from
(select name,city_name,company_name,count(*) as num from info group by name,city_name,company_name)p1
group by city_name, company_name);

--rate

with item_info as(
    select t6.item_name,company_name,city_name,type,price from
(select item_name,company_name from retrieval left join
(select courier.id,company.name as company_name from courier left join company on courier.company_id = company.id)t5
on courier_id=t5.id)t6 left join
(select item_name,city_name,type,price from
(select item_name,name as city_name from item_via_city left join city on item_via_city.export_city_id = city.id)t1
left join item on item_name=name)t7 on t6.item_name=t7.item_name)

select company_name,type,city_name from (select export_tax/t2.price as rate,company_name,city_name,type from
(select * from item_info where (price,company_name,city_name,type) in
(select max(price),company_name,city_name,type from item_info group by company_name,city_name, type)) t2
left join tax_info on tax_info.item_name=t2.item_name)t4 where (t4.rate,company_name,t4.type) in-----
(select min(rate),company_name,type from
(select export_tax/t2.price as rate,company_name,city_name,type from
(select * from item_info where (price,company_name,city_name,type) in
(select max(price),company_name,city_name,type from item_info group by company_name,city_name, type)) t2
left join tax_info on tax_info.item_name=t2.item_name)t3 group by type,company_name)

