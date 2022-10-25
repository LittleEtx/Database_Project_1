package com.littleetx.database_project_1.file_database.types;

import java.time.LocalDateTime;

public class DatabaseType_DateTime extends DatabaseType {
    private final LocalDateTime date;

    DatabaseType_DateTime(String value) {
        this.date = LocalDateTime.parse(value);
    }

    DatabaseType_DateTime(Object value) {
        this.date = (LocalDateTime) value;
    }


    @Override
    public String toString() {
        return date.toString();
    }

    @Override
    public Object getValue() {
        return (LocalDateTime) date;
    }
}
