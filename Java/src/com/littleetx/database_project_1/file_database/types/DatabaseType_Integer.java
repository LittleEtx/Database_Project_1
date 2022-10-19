package com.littleetx.database_project_1.file_database.types;

public class DatabaseType_Integer extends DatabaseType {
    private final Integer value;
    DatabaseType_Integer(String value) {
        this.value = Integer.parseInt(value);
    }

    DatabaseType_Integer(Object value) {
        if (value instanceof String) {
            this.value = Integer.parseInt((String) value);
        } else {
            this.value = (Integer) value;
        }
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DatabaseType_Integer that = (DatabaseType_Integer) o;

        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
