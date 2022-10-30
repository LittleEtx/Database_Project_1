package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.SQLDataOperator;

public class UpdateData {
    private static final String preType = "strawberry";
    private static final String newType = "blueberry";

    public static void main(String[] args) {
        update(new FileDataOperator(), "File");
        update(new SQLDataOperator(), "SQL");
    }

    private static void update(IDataOperator dataOperator, String name) {
        dataOperator.initialize();
        var itemSet = dataOperator.getAllItems();
        int count = 0;
        for (var item : itemSet.values()) {
            if (item.type().equals(preType)) {
                count++;
            }
        }

        System.out.println("Update " + name +", affecting rows: " + count);
        long startTime = System.currentTimeMillis();
        System.out.println("Updating item type from " + preType + " to " + newType + "...");
        dataOperator.updateItemType(preType, newType);
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Updated in " + time + "ms, speed: " +
                String.format("%.4f", ((double) count) / time) + " records/s");

        startTime = System.currentTimeMillis();
        System.out.println("Updating item type from " + newType + " to " + preType + "...");
        dataOperator.updateItemType(newType, preType);
        time = System.currentTimeMillis() - startTime;
        System.out.println("Updated in " + time + "ms, speed: " +
                String.format("%.4f", ((double) count) / time) + " records/s");
        System.out.println();
    }
}
