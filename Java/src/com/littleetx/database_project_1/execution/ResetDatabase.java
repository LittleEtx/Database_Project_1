package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.SQLDataOperator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResetDatabase {
    public static void main(String[] args) {
        List<File> files = new ArrayList<>();
        files.add(new File("database/city.csv"));
        files.add(new File("database/company.csv"));
        files.add(new File("database/container.csv"));
        files.add(new File("database/courier.csv"));
        files.add(new File("database/delivery.csv"));
        files.add(new File("database/export.csv"));
        files.add(new File("database/import.csv"));
        files.add(new File("database/item.csv"));
        files.add(new File("database/item_via_city.csv"));
        files.add(new File("database/log.csv"));
        files.add(new File("database/retrieval.csv"));
        files.add(new File("database/ship.csv"));
        files.add(new File("database/tax_info.csv"));

        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }

        SQLDataOperator sqlOperator = new SQLDataOperator();
        sqlOperator.initialize();
        sqlOperator.resetTables();
        sqlOperator.disconnect();

        System.out.println("Database reset.");
    }
}
