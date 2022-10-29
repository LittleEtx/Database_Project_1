package com.littleetx.database_project_1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littleetx.database_project_1.records.Item;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

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
        try {
            String imItem = "insert into item (name, type, price) " +
                    "values (?, ?, ?)";
            PreparedStatement itemStm = con.prepareStatement(imItem);
            insertData(itemStm, info.items(), (stm, item) -> {
                try {
                    stm.setString(1, item.name());
                    stm.setString(2, item.type());
                    stm.setInt(3, item.price());
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imCity = "insert into city (id, area_code, name) " +
                    "values (?, ?, ?)";
            PreparedStatement cityStm = con.prepareStatement(imCity);
            insertData(cityStm, info.cities(), (stm, city) -> {
                try {
                    stm.setInt(1,city.id());
                    stm.setString(2,city.areaCode());
                    stm.setString(3,city.name());;
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imCompany="insert into company (id, name) " +
                    "values (?, ?)";
            PreparedStatement companyStm = con.prepareStatement(imCompany);
            insertData(companyStm, info.companies(), (stm, company) -> {
                try {
                    stm.setInt(1,company.id());
                    stm.setString(2,company.name());
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imContainer="insert into container (code, type) " +
                    "values (?, ?)";
            PreparedStatement containerStm = con.prepareStatement(imContainer);
            insertData(containerStm, info.containers(), (stm, container) -> {
                try {
                    stm.setString(1,container.code());
                    stm.setString(2,container.type());
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imShip="insert into ship (id, name, company_id) " +
                    "values (?, ?, ?)";
            PreparedStatement shipStm = con.prepareStatement(imShip);
            insertData(shipStm, info.ships(), (stm, ship) -> {
                try {
                    stm.setInt(1,ship.id());
                    stm.setString(2,ship.name());
                    stm.setInt(3,ship.company().id());
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imCourier="insert into courier (id, name ,gender, phone_number, birthday, company_id) " +
                    "values (?, ?, ?, ?, ?, ?)";
            PreparedStatement courierStm = con.prepareStatement(imCourier);
            insertData(courierStm, info.couriers(), (stm, courier) -> {
                try {
                    stm.setInt(1,courier.id());
                    stm.setString(2,courier.name());
                    stm.setString(3,courier.gender());
                    stm.setString(4,courier.phoneNumber());
                    stm.setDate(5,Date.valueOf(courier.birthday()));
                    stm.setInt(6,courier.company().id());
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imTax_info="insert into tax_info (item_name,export_tax,import_tax) " +
                    "values (?, ?, ?)";
            PreparedStatement tax_infoStm = con.prepareStatement(imTax_info);
            insertData(tax_infoStm, info.taxInfos(), (stm, tax_info) -> {
                try {
                    stm.setString(1,tax_info.item().name());
                    stm.setBigDecimal(2,tax_info.exportRate());
                    stm.setBigDecimal(3,tax_info.importRate());
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imRoute="insert into item_via_city (item_name, retrieval_city," +
                    "export_city_id, import_city_id, delivery_city_id) " +
                    "values (?, ?, ?, ?, ?)";
            PreparedStatement routeStm = con.prepareStatement(imRoute);
            insertData(routeStm, info.itemsViaCities(), (stm, route) -> {
                try {
                    stm.setString(1, route.item().name());
                    stm.setInt(2, route.retrievalCity().id());
                    stm.setInt(3, route.exportCity().id());
                    stm.setInt(4, route.importCity().id());
                    stm.setInt(5, route.deliveryCity().id());
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imLogs="insert into logs (item_name, log_time) " +
                    "values (?, ?)";
            PreparedStatement logsStm = con.prepareStatement(imLogs);
            insertData(logsStm, info.logs(), (stm, logs) -> {
                try {
                    stm.setString(1, logs.item().name());
                    stm.setTimestamp(2, Timestamp.valueOf(logs.logTime()));
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imRetrieve="insert into retrieve (item_name, courier_id, start_date) " +
                    "values (?, ?, ?)";
            PreparedStatement retrieveStm = con.prepareStatement(imRetrieve);
            insertData(retrieveStm, info.retrieves(), (stm, retrieve) -> {
                try {
                    stm.setString(1, retrieve.item().name());
                    stm.setInt(2, retrieve.courier().id());
                    stm.setDate(3, Date.valueOf(retrieve.date()));
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imDelivery="insert into delivery (item_name, courier_id, finish_date) " +
                    "values (?, ?, ?)";
            PreparedStatement deliveryStm = con.prepareStatement(imDelivery);
            insertData(deliveryStm, info.deliveries(), (stm, delivery) -> {
                try {
                    stm.setString(1, delivery.item().name());
                    stm.setInt(2, delivery.courier().id());
                    stm.setDate(3, Date.valueOf(delivery.date()));
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imExport="insert into export (item_name, ship_id, container_code, export_date) " +
                    "values (?, ?, ?, ?)";
            PreparedStatement exportStm = con.prepareStatement(imExport);
            insertData(exportStm, info.exports(), (stm, export) -> {
                try {
                    stm.setString(1, export.item().name());
                    stm.setInt(2, export.ship().id());
                    stm.setString(3, export.container().code());
                    stm.setDate(4, Date.valueOf(export.date()));
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

            String imImport="insert into import (item_name, import_date) " +
                    "values (?, ?)";
            PreparedStatement importStm = con.prepareStatement(imImport);
            insertData(importStm, info.imports(), (stm, import1) -> {
                try {
                    stm.setString(1, import1.item().name());
                    stm.setDate(2, Date.valueOf(import1.date()));
                } catch (SQLException e) {
                    throw new RuntimeException("Wrong parameter!", e);
                }
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void insertData(PreparedStatement statement, Collection<T> data,
                                BiConsumer<PreparedStatement, T> setPara) {
        long start;
        start = System.currentTimeMillis();
        int count = 0;
        try{
            for (T t: data) {
                setPara.accept(statement, t);
                count++;
                statement.addBatch();
                if (count % 1000 == 0){
                    statement.executeBatch();
                    statement.clearBatch();
                }
            }
            if (count % 1000 != 0){
                statement.executeBatch();
            }
            statement.clearBatch();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        long time = System.currentTimeMillis() - start;
        String tableName =  data.iterator().next().getClass().getSimpleName();
        Logger.log(count + " records successfully loaded into " + tableName + " in " + time + " ms" +
                ", loading speed : " + count/time + " records/ms");
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
