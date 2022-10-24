package com.littleetx.database_project_1.file_database.test;

import com.littleetx.database_project_1.file_database.Database;
import com.littleetx.database_project_1.file_database.Table;
import com.littleetx.database_project_1.file_database.jsonTypes.ColumnInfo;
import com.littleetx.database_project_1.file_database.jsonTypes.TableInfo;

public class DatabaseJsonChecker {
    public static void main(String[] args) {
        Database database = new Database();
        database.initialize();

        for (Table table : database.getTables()) {
            TableInfo tableInfo = table.getTableInfo();
            System.out.println("=======================");
            System.out.println(tableInfo.name());
            for (ColumnInfo columnInfo : tableInfo.columns()) {
                System.out.println(columnInfo.name() + " " + columnInfo.type());
            }
        }
    }
}
