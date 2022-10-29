package com.littleetx.database_project_1.execution;

import java.io.File;
import java.sql.Statement;
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
        files.add(new File("database/ship.csv"));
        files.add(new File("database/tax_info.csv"));


        Statement statement = null;



    }


}
