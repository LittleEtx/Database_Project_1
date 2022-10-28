package com.littleetx.database_project_1;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littleetx.database_project_1.records.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.List;

public class SQLDataOperator implements IDataOperator {
    private static final String LoginInfoPath = "database_login.json";
    private Connection con;

    @Override
    public void initialize() {
        File file = new File(LoginInfoPath);
        LoginInfo info ;
        JsonFactory jf = new JsonFactory();
        try (JsonParser jp = jf.createParser(file)) {
            ObjectMapper mapper = new ObjectMapper();
            info = mapper.readValue(jp, LoginInfo.class);
        } catch (IOException e) {
            throw new RuntimeException("Can not read database_login.json", e);
        }

        try {
            String url = "jdbc:postgresql://" + info.host() + ":" + info.port() + "/" + info.databaseName();
            con = DriverManager.getConnection(url, info.username(), info.password());

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (SQLException e) {
                System.err.println("Database disconnection failed");
                System.err.println(e.getMessage());
                System.exit(1);
            }
        }
    }

    @Override
    public void importData(TableInfo info){
        String imItem = "insert into item (name,type,price) " +
                "values (?,?,?)";
        String imCity = "insert into city (id,area_code,name) " +
                "values (?,?,?)";
        String imCompany="insert into company (id,name) " +
                "values (?,?)";
        String imContainer="insert into container (code,type) " +
                "values (?,?)";
        String imShip="insert into ship (id,name,company_id) " +
                "values (?,?,?)";
        String imcourier="insert into courier (id,name,gender,phone_number,birth_year,company_id) " +
                "values (?,?,?,?,?,?)";
        String imTax_info="insert into tax_info (item_name,export_tax,import_tax) " +
                "values (?,?,?)";
        String imRoute="insert into route (item_id, retrieval_city,export_city_id,import_city_id,delivery_city_id) " +
                "values (?,?,?,?,?)";
        String imLogs="insert into logs (item_name,log_time) " +
                "values (?,?)";
        String imRetrieve="insert into retrieve (item_name,courier_id,start_date) " +
                "values (?,?,?)";
        String imDelivery="insert into delivery (item_name,courier_id,finish_date) " +
                "values (?,?,?)";
        String imExport="insert into export (item_name, ship_id,container_code,export_date) " +
                "values (?,?,?,?)";
        String imImport="insert into import (item_name, import_date) " +
                "values (?,?)";
        try{
            long start,end;
            start = System.currentTimeMillis();
            PreparedStatement stmt = con.prepareStatement(imItem);
            int count=0;
            for ( Item temp: info.items()) {//item
                stmt.setString(1,temp.name());
                stmt.setString(2,temp.type());
                stmt.setInt(3,temp.price());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
        }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
           stmt = con.prepareStatement(imCity);
            count=0;
            for ( City temp: info.cities()) {//city
                stmt.setInt(1,temp.id());
                stmt.setString(2,temp.areaCode());
                stmt.setString(3,temp.name());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imCompany);
            count=0;
            for ( Company temp: info.companies()) {//company (id,name)
                stmt.setInt(1,temp.id());
                stmt.setString(2,temp.name());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imContainer);
            count=0;
            for ( Container temp: info.containers()) {// container (code,type)
                stmt.setString(1,temp.code());
                stmt.setString(2,temp.type());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imShip);
            count=0;
            for ( Ship temp: info.ships()) {// ship (id,name,company_id)
                stmt.setInt(1,temp.id());
                stmt.setString(2,temp.name());
                stmt.setInt(3,temp.company().id());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imcourier);
            count=0;
            for ( Courier temp: info.couriers()) {//courier (id,name,gender,phone_number,birth_year,company_id)
                stmt.setInt(1,temp.id());
                stmt.setString(2,temp.name());
                stmt.setString(3,temp.gender());
                stmt.setString(4,temp.phoneNumber());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imTax_info);
            count=0;
            for ( TaxInfo temp: info.taxInfos()) {//tax_info (item_name,export_tax,import_tax)
                stmt.setString(1,temp.item().name());
                stmt.setBigDecimal(2,temp.exportRate());
                stmt.setBigDecimal(3,temp.importRate());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imRoute);
            count=0;
            for (ItemViaCity temp: info.itemsViaCities()) {//route (item_id, retrieval_city,export_city_id,import_city_id,delivery_city_id)
                stmt.setString(1,temp.item().name());
                stmt.setInt(2,temp.retrievalCity().id());
                stmt.setInt(3,temp.exportCity().id());
                stmt.setInt(4,temp.importCity().id());
                stmt.setInt(5,temp.deliveryCity().id());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imLogs);
            count=0;
            for ( Log temp: info.logs()) {//logs (item_name,log_time)
                stmt.setString(1,temp.item().name());
                stmt.setTimestamp(2,temp.logTime());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imRetrieve);
            count=0;
            for ( Retrieval temp: info.retrievals()) {//retrieve (item_name,courier_id,start_date)
                stmt.setString(1,temp.item().name());
                stmt.setInt(2,temp.courier().id());
                stmt.setDate(3,temp.date());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imDelivery);
            count=0;
            for ( Delivery temp: info.deliveries()) {//delivery (item_name,courier_id,finish_date)
                stmt.setString(1,temp.item().name());
                stmt.setInt(2,temp.courier().id());
                stmt.setDate(3,temp.date());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imExport);
            count=0;
            for ( Export temp: info.exports()) {//export (item_name, ship_id,container_code,export_date)
                stmt.setString(1,temp.item().name());
                stmt.setInt(2,temp.ship().id());
                stmt.setString(3,temp.container().code());
                stmt.setDate(4,temp.date());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

            start = System.currentTimeMillis();
            stmt = con.prepareStatement(imImport);
            count=0;
            for ( Import temp: info.imports()) {//import (item_name, import_date)
                stmt.setString(1,temp.item().name());
                stmt.setDate(2,temp.date());
                count++;
                stmt.addBatch();
                if (count%1000==0){
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            if (count%1000!=0){
                stmt.executeBatch();
            }
            stmt.clearBatch();
            end = System.currentTimeMillis();
            System.out.println(count + " records successfully loaded");
            System.out.println("Loading speed : " + (count * 1000)/(end - start) + " records/s");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
//    public <T>void insertAll(Collection<T> table,PreparedStatement stmt){
//        for (T row : table) {
//            stmt.set
//        }
//    }


    private int insertCity(City city) {
        String findCity = "select id FROM city where name = ?";
        ResultSet rs;
        try {
            PreparedStatement findCityStatement = con.prepareStatement(findCity);
            findCityStatement.setString(1, city.name());
            rs = findCityStatement.executeQuery();
            if (!rs.next()) {
                String insertCity = "insert into city (id, name) values (?, ?)";
                PreparedStatement insertCityStatement = con.prepareStatement(insertCity);

                int id = getIdFrom("city");
                insertCityStatement.setInt(1, id);
                insertCityStatement.setString(2, city.name());
                insertCityStatement.executeUpdate();
                return id;
            }
            else {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int getIdFrom(String table) throws SQLException {
        String maxId = "select max(id) from " + table;
        PreparedStatement maxCityIdStatement = con.prepareStatement(maxId);
        ResultSet maxIdResult = maxCityIdStatement.executeQuery();
        int id;
        if (!maxIdResult.next()) {
            id = 0;
        } else {
            id = maxIdResult.getInt(1) + 1;
        }
        return id;
    }

    @Override
    public void delete(List<Item> itemList) {

    }

    @Override
    public void updateItemType(String oldType, String newType) {

    }

    @Override
    public List<Item> findUnfinishedItems() {
        return null;
    }
}
