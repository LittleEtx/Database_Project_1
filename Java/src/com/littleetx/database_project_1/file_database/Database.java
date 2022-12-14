package com.littleetx.database_project_1.file_database;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littleetx.database_project_1.file_database.jsonTypes.ColumnInfo;
import com.littleetx.database_project_1.file_database.jsonTypes.DatabaseInfo;
import com.littleetx.database_project_1.file_database.jsonTypes.TableInfo;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Database {
    public static final String DatabasePath = "database/";
    private static final String DDLPath = DatabasePath + "database.json";
    private Map<String, Table> tables;

    public void initialize() {
        File file = new File(DDLPath);
        DatabaseInfo info;
        if (!file.exists()) {
            //create new database
            File dir = new File(DatabasePath);
            if (!dir.exists() && !dir.mkdir()) {
                 throw new RuntimeException("Cannot create database directory");
            }
            try {
                if (!file.createNewFile())
                    throw new RuntimeException("Cannot create database file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            info = new DatabaseInfo(new ArrayList<>());
        } else {
            //read database from file
            JsonFactory jf = new JsonFactory();
            try (JsonParser jp = jf.createParser(file)) {
                ObjectMapper mapper = new ObjectMapper();
                info = mapper.readValue(jp, DatabaseInfo.class);
            } catch (IOException e) {
                throw new RuntimeException("Fail to read database.json", e);
            }
        }

        tables = new HashMap<>();
        for (TableInfo tableInfo : info.tables()) {
            Table table = new Table(tableInfo);
            tables.putIfAbsent(table.getTableInfo().name(), table);
        }

        DatabaseMsg.print("Database initialized");
    }

    public void createTable(TableInfo tableInfo) {
        createTable(new Table(tableInfo));
    }

    public void createTable(Table table) {
        if (tables.get(table.getTableInfo().name()) != null) {
            throw new IllegalArgumentException("Table "  + table.getTableInfo().name() + " already exists");
        }

        tables.put(table.getTableInfo().name(), table);
        File file = new File(DatabasePath + table.getTableInfo().name() + ".csv");
        try {
            if (!file.createNewFile()) {
                throw new RuntimeException("Cannot create table file");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        write();
        DatabaseMsg.print("Table " + table.getTableInfo().name() + " created");
    }

    public void dropTable(String tableName) {
        Table table = tables.get(tableName);
        if (table != null) {
            tables.remove(table.getTableInfo().name());
        } else {
            throw new IllegalArgumentException("Table " + tableName + " does not exist");
        }
        write();
        File file = new File(DatabasePath + tableName + ".csv");
        if (!file.delete()) {
            throw new RuntimeException("Cannot delete table file " + tableName + ".csv");
        }

        DatabaseMsg.print("Table " + tableName + " dropped");
    }

    //TODO : link Table to Database
    public Collection<Table> getTables() {
        return tables.values();
    }

    public @Nullable Table getTable(String tableName) {
        return tables.get(tableName);
    }

    private void write() {
        List<TableInfo> tableInfos = new ArrayList<>();
        for (Table table : tables.values()) {
            tableInfos.add(table.getTableInfo());
        }
        DatabaseInfo info = new DatabaseInfo(tableInfos);

        try {
            (new ObjectMapper()).writerFor(DatabaseInfo.class)
                    .writeValue(new File(DDLPath), info);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file", e);
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
        db.initialize();
        ColumnInfo c1 = new ColumnInfo("c1", "Integer");
        ColumnInfo c2 = new ColumnInfo("c2", "Varchar");

        db.createTable(new TableInfo(List.of(new ColumnInfo[]{c1, c2}), "test"));
        db.dropTable("test");
    }

}
