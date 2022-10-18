package com.littleetx.database_project_1.file_database;

import com.littleetx.database_project_1.file_database.types.DatabaseType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final String tableName;
    private final FileOperator fileOperator;
    private String[] columnNames;
    private String[] columnTypes;

    public Table(String name, String[] columnNames, String[] columnTypes) {
        tableName = name;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        fileOperator = new FileOperator_CSV(tableName + ".csv");
    }

    /**
     * a shortcut to add one row
     * @param values values of the row
     */
    public void insert(Object... values) {
        ArrayList<Object[]> rows = new ArrayList<>();
        rows.add(values);
        insert(rows);
    }

    /**
     * add multiple rows to the table
     * @param rows rows to be added
     */
    public void insert(List<Object[]> rows) {
        List<String[]> stringRows = new ArrayList<>();
        for (Object[] row : rows) {
            if (row.length != columnNames.length) {
                throw new IllegalArgumentException(
                        "The number of values does not match the number of columns");
            }
            String[] stringValues = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                try {
                    stringValues[i] = DatabaseType.getInstance(columnTypes[i], row[i]).toString();
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(
                            "value at " + i + " is not of type " + columnTypes[i]);
                }
            }
            stringRows.add(stringValues);
        }

        fileOperator.insertRows(stringRows);
    }


    public List<Object[]> select(String[] columns, DatabaseType[] values) {
        //TODO
        return null;
    }

    public void update(String[] columns, DatabaseType[] oldValues, DatabaseType[] newValues) {

        //TODO
    }

    public void delete(String[] columns, DatabaseType[] values) {
        //TODO
    }

    //testing
    public static void main(String[] args) {

        Table t = new Table("testTable", new String[]{"str", "int", "number"},
                new String[]{"String", "Integer", "Numeric"});
        t.insert("abc", 30, 20.7);
    }
}
