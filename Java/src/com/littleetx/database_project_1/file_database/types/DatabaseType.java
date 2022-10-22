package com.littleetx.database_project_1.file_database.types;

import java.lang.reflect.InvocationTargetException;

public abstract class DatabaseType {
    public static DatabaseType getInstance(String typeName, Object value) throws
            ClassNotFoundException, InvocationTargetException {
        Class<DatabaseType> c = DatabaseType.class;
        try {
            return  (DatabaseType) Class.forName(c.getName() + "_" + getCapitalize(typeName)).
                    getDeclaredConstructor(Object.class).newInstance(value);
        } catch (InstantiationException
                 | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseType getInstance(String typeName, String value) throws
            ClassNotFoundException, InvocationTargetException {
        Class<DatabaseType> c = DatabaseType.class;
        try {
            return  (DatabaseType) Class.forName(c.getName() + "_" + getCapitalize(typeName)).
                    getDeclaredConstructor(String.class).newInstance(value);
        } catch (InstantiationException
                 | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getCapitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    //Subclass must provide toString method
    @Override
    abstract public String toString();

    abstract public Object getValue();

    public static void main(String[] args) {
        try {
            System.out.println(DatabaseType.getInstance("Integer", "a"));
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        } catch (InvocationTargetException e) {
            System.out.println("Invocation target exception");
        }
    }
}
