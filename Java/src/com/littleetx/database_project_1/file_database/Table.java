package com.littleetx.database_project_1.file_database;

import com.littleetx.database_project_1.file_database.jsonTypes.ColumnInfo;
import com.littleetx.database_project_1.file_database.jsonTypes.TableInfo;
import com.littleetx.database_project_1.file_database.types.DatabaseType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private final FileOperator fileOperator;
    private final TableInfo tableInfo;

    public Table(String name, String[] columnNames, String[] columnTypes) {
        List<ColumnInfo> columns = new ArrayList<>();
        for (int i = 0; i < columnNames.length; i++) {
            columns.add(new ColumnInfo(columnNames[i], columnTypes[i]));
        }
        tableInfo = new TableInfo(columns, name);
        fileOperator = new FileOperator_CSV(Database.DatabasePath + name + ".csv");
        //TODO : add a check to make sure the columnTypes are valid
    }

    public Table(TableInfo tableInfo) {
        this.tableInfo = tableInfo;
        fileOperator = new FileOperator_CSV(Database.DatabasePath + tableInfo.name() + ".csv");
        //TODO : add a check to make sure the columnTypes are valid
    }

    public TableInfo getTableInfo() {
        return tableInfo;
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
    public void insert(@NotNull List<@Nullable Object @NotNull []> rows) {
        List<String[]> stringRows = new ArrayList<>();
        for (Object[] row : rows) {
            if (row.length != tableInfo.columns().size()) {
                throw new IllegalArgumentException(
                        "The number of values does not match the number of columns");
            }
            String[] stringValues = objToStr(row);
            stringRows.add(stringValues);
        }

        fileOperator.insertRows(stringRows);
    }

    /**
     * shortcut to select rows
     */
    public List<@Nullable Object @NotNull []> select(String column, @Nullable Object value) {
        return select(new String[]{column}, new Object[]{value});
    }

    /**
     * select rows from the table
     * @param columns column names
     * @param values values to be selected
     * @return the selected rows
     */
    public List<@Nullable Object @NotNull []> select(@NotNull String @NotNull [] columns,
                                                     @Nullable Object @NotNull [] values) {
        List<Object[]> result = new ArrayList<>();
        if (columns.length != values.length) {
            throw new IllegalArgumentException(
                    "The number of columns does not match the number of values");
        }

        int[] columnIndices = getColumnIndices(columns);
        String[] stringValues = objToStr(columnIndices, values);

        List<String[]> resultStrings = fileOperator.findRowsSatisfied(columnIndices, stringValues);
        for (String[] strings : resultStrings) {
            Object[] objs = strToObj(strings);
            result.add(objs);
        }
        return result;
    }

    /**
     * select all rows
     */
    public List<@Nullable Object @NotNull []> select() {
        List<Object[]> result = new ArrayList<>();
        try (FileOperatorReader reader = fileOperator.getReader()) {
            for (String[] strings : reader) {
                Object[] objs = strToObj(strings);
                result.add(objs);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void update(@NotNull String findColumns, @Nullable Object oldValue,
                       @NotNull String modifyColumns, @Nullable Object newValue) {
        update(new String[]{findColumns}, new Object[]{oldValue},
                new String[]{modifyColumns}, new Object[]{newValue});
    }

    /**
     * update rows in the table
     * @param findColumns findColumns to be updated
     * @param oldValues old values of the findColumns
     * @param newValues new values of the findColumns
     */

    public void update(@NotNull String @NotNull [] findColumns, @Nullable Object @NotNull [] oldValues,
                       @NotNull String @NotNull [] modifyColumns, @Nullable Object @NotNull [] newValues) {
        if (findColumns.length != oldValues.length || modifyColumns.length != newValues.length) {
            throw new IllegalArgumentException(
                    "The number of findColumns does not match the number of values");
        }

        int[] findColumnIndices = getColumnIndices(findColumns);
        String[] stringOldValues = objToStr(findColumnIndices, oldValues);
        int[] modifyColumnIndices = getColumnIndices(modifyColumns);
        String[] stringNewValues = objToStr(modifyColumnIndices, newValues);

        fileOperator.modifyRowsTo(findColumnIndices, stringOldValues,
                modifyColumnIndices, stringNewValues);
    }

    public void delete(@NotNull String column, @Nullable Object value) {
        delete(new String[]{column}, new Object[]{value});
    }

    /**
     * delete rows in the table
     * @param columns columns to be deleted
     * @param values values of the columns, matched rows will be deleted
     */
    public void delete(@NotNull String @NotNull [] columns, @Nullable Object @NotNull [] values) {
        if (columns.length != values.length) {
            throw new IllegalArgumentException(
                    "The number of columns does not match the number of values");
        }

        int[] columnIndices = getColumnIndices(columns);
        String[] stringValues = objToStr(columnIndices, values);

        fileOperator.deleteRows(columnIndices, stringValues);
    }

    private int findColumnIndex(String column) {
        for (int i = 0; i < tableInfo.columns().size(); i++) {
            if (tableInfo.columns().get(i).name().equals(column)) {
                return i;
            }
        }
        return - 1;
    }

    private int @NotNull [] getColumnIndices(String[] columns) {
        int[] columnIndices = new int[columns.length];
        int index;
        for (int i = 0; i < columns.length; i++) {
            if ((index = findColumnIndex(columns[i])) != -1)
                columnIndices[i] = index;
            else
                throw new IllegalArgumentException("Column " + columns[i] + " does not exist");
        }
        return columnIndices;
    }

    private String[] objToStr(@Nullable Object @NotNull [] row) {
        int[] indices = new int[row.length];
        for (int i = 0; i < indices.length; i++)
            indices[i] = i;
        return objToStr(indices, row);
    }

    @NotNull
    private String[] objToStr(int[] columnIndices, @Nullable Object @NotNull [] row) {
        String[] stringValues = new String[row.length];
        for (int i = 0; i < row.length; i++) {
            try {
                if (row[i] == null)
                    stringValues[i] = null;
                else
                    stringValues[i] = DatabaseType.
                            getInstance(tableInfo.columns()
                                    .get(columnIndices[i]).type(), row[i]).toString();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(
                        "value " + row[i] + " is not of type " + tableInfo.columns()
                                .get(columnIndices[i]).type());
            }
        }
        return stringValues;
    }

    private Object[] strToObj(@Nullable String @NotNull [] strings) {
        int[] indices = new int[strings.length];
        for (int i = 0; i < indices.length; i++)
            indices[i] = i;

        return strToObj(indices, strings);
    }

    @NotNull
    private Object[] strToObj(int[] columnIndices, @Nullable String @NotNull [] strings) {
        Object[] objs = new Object[strings.length];
        for (int i = 0; i < strings.length; i++) {
            try {
                if (strings[i] == null)
                    objs[i] = null;
                else
                    objs[i] = DatabaseType.getInstance(tableInfo.columns()
                            .get(columnIndices[i]).type(), strings[i]).getValue();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("Data " + strings[i] + " cannot meet Type "
                        + tableInfo.columns().get(columnIndices[i]).type(), e);
            }
        }
        return objs;
    }
    public static void main(String[] args) {

        Table t = new Table("testTable", new String[]{"str", "int", "number"},
                new String[]{"Varchar", "Integer", "Numeric"});
        System.out.println("Insert check");
        t.insert("abc", 30, 20.7);
        t.insert("def", 40, 30.7);
        t.insert("ghi", 50, null);
        printTable(t.select());

        System.out.println("Select check");
        printTable(t.select(new String[]{"str", "int"}, new Object[]{"abc", 30}));

        System.out.println("Update check");
        t.update("number", null, "number", "40.7");
        t.update("int", 40, "str", "is forty!");
        t.update("str", "is forty!", "int", null);
        printTable(t.select());

        System.out.println("Delete check");
        t.delete("int", null);
        printTable(t.select());
    }

    private static void printTable(List<Object[]> t) {
        for (Object[] values : t) {
            for (Object value : values) {
                if (value != null)
                    System.out.print(value);
                else
                    System.out.print("NULL");
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
