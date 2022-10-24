package com.littleetx.database_project_1.file_database;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.littleetx.database_project_1.file_database.jsonTypes.ColumnInfo;
import com.littleetx.database_project_1.file_database.jsonTypes.DatabaseInfo;
import com.littleetx.database_project_1.file_database.jsonTypes.TableInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Database implements Iterable<Table> {
    public static final String DatabasePath = "database/";
    private static final String DDLPath = DatabasePath + "database.json";
    private List<Table> tables;

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

        tables = new ArrayList<>();
        for (TableInfo tableInfo : info.tables()) {
            Table table = new Table(tableInfo);
            tables.add(table);
        }

        DatabaseMsg.print("Database initialized");
    }

    public void createTable(TableInfo tableInfo) {
        createTable(new Table(tableInfo));
    }

    public void createTable(Table table) {
        for (Table t : tables) {
            if (t.getTableInfo().name().equals(table.getTableInfo().name())) {
                throw new IllegalArgumentException("Table "  + table.getTableInfo().name() + " already exists");
            }
        }

        tables.add(table);
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
        boolean hasTable = false;
        for (Table table : tables) {
            if (table.getTableInfo().name().equals(tableName)) {
                tables.remove(table);
                hasTable = true;
                break;
            }
        }
        if (!hasTable) {
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
    @NotNull
    @Override
    public Iterator<Table> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < tables.size();
            }

            @Override
            public Table next() {
                return tables.get(index++);
            }
        };
    }

    private void write() {
        List<TableInfo> tableInfos = new ArrayList<>();
        for (Table table : tables) {
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
