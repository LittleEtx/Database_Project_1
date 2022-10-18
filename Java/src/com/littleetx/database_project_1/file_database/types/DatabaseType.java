package com.littleetx.database_project_1.file_database.types;

import java.lang.reflect.InvocationTargetException;

public abstract class DatabaseType {
    public static DatabaseType getInstance(String typeName, Object value) throws ClassNotFoundException {
        Class<DatabaseType> c = DatabaseType.class;
        try {
            return  (DatabaseType) Class.forName(c.getName() + "_" + typeName).
                    getDeclaredConstructor(Object.class).newInstance(value);
        } catch (InvocationTargetException | InstantiationException
                 | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    //Subclass must provide toString method
    @Override
    abstract public String toString();

    abstract public Object getValue();

    public static void main(String[] args) {
        try {
            System.out.println(DatabaseType.getInstance("Integer", 1));
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found");
        }
    }
}
