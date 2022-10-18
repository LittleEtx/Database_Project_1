package com.littleetx.database_project_1.file_database.types;

import java.math.BigDecimal;

public class DatabaseType_Numeric extends DatabaseType{
    private final BigDecimal value;

    DatabaseType_Numeric(String value) {
        this.value = new BigDecimal(value);
    }

    DatabaseType_Numeric(Object value) {
        if (value instanceof Float)
            this.value = BigDecimal.valueOf((float) value);
        else if(value instanceof Double)
            this.value = BigDecimal.valueOf((double) value);
        else
            this.value = (BigDecimal) value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public Object getValue() {
        return value;
    }
}
