package com.littleetx.database_project_1.file_database.types;

public class DatabaseType_Varchar extends DatabaseType{
    private final String value;

    DatabaseType_Varchar(Object value) {
        this.value = (String) value;
    }

    DatabaseType_Varchar(String string) {
        this.value = string;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
