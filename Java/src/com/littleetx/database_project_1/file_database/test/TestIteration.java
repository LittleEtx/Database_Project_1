package com.littleetx.database_project_1.file_database.test;

import com.littleetx.database_project_1.file_database.Database;
import com.littleetx.database_project_1.file_database.Table;

import java.util.Objects;

public class TestIteration {
    public static void main(String[] args) {
        Database database = new Database();
        database.initialize();
        Table table = database.getTable("delivery");

        for (Object[] row : Objects.requireNonNull(table).select()) {
            for (Object value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }

    }
}
