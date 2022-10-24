package com.littleetx.database_project_1.file_database.types;

import java.time.LocalDate;

public class DatabaseType_Date extends DatabaseType{
    private final LocalDate date;

    DatabaseType_Date(String value) {
        this.date = LocalDate.parse(value);
    }

    DatabaseType_Date(Object value) {
        this.date = (LocalDate) value;
    }

    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public Object getValue() {
        return date;
    }
}
