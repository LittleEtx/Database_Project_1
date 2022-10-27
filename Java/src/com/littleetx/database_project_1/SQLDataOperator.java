package com.littleetx.database_project_1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littleetx.database_project_1.records.City;
import com.littleetx.database_project_1.records.Information;
import com.littleetx.database_project_1.records.Item;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLDataOperator implements IDataOperator {
    private static final String LoginInfoPath = "database_login.json";
    private Connection con;

    @Override
    public void initialize() {
        File file = new File(LoginInfoPath);
        LoginInfo info;
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
    public void insert(Information info) {
        //insert into cities

    }

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
    public void delete(Item item) {

    }

    @Override
    public void fillInAllNullValues() {

    }

    @Override
    public Item[] findUnfinishedItems() {
        return new Item[0];
    }
}
