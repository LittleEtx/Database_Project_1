package com.littleetx.database_project_1.file_database;

import java.io.File;
import java.lang.reflect.Type;

public class Table {
    private final String tableName;
    private String[] columnNames;
    private String[] columnTypes;

    public Table(String name) {
        tableName = name;

        File file = new File(tableName + ".csv");
    }

    public void insert(Object... values) {

    }

    public Object[][] select(String column, String value) {
        return null;
    }

    public void update(String column, Object oldValue, Object newValue) {

    }

    public void delete(String column, Object value) {

    }


}
