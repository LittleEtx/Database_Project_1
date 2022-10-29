package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.Logger;
import com.littleetx.database_project_1.SQLDataOperator;

public class SelectData {

    public static void main(String[] args) {
        Logger.setStream(System.out);

        search(new SQLDataOperator(), "SQL");
        search(new FileDataOperator(), "File");
    }

    private static void search(IDataOperator dataOperator, String name) {
        dataOperator.initialize();
        System.out.println("Select unfinished items from " + name + "...");
        long startTime = System.currentTimeMillis();
        System.out.println("select type: delete if find");
        var list1 = dataOperator.findUnfinishedItems(IDataOperator.FindType.DeleteIfFind);
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Selected " + list1.size() + " items in " + time + "ms, speed: " +
                String.format("%.4f", ((double) list1.size()) / time) + " records/s");

        startTime = System.currentTimeMillis();
        System.out.println("select type: query and delete");
        var list2 = dataOperator.findUnfinishedItems(IDataOperator.FindType.QueryAndDelete);
        time = System.currentTimeMillis() - startTime;
        System.out.println("Selected " + list2.size() + " items in " + time + "ms, speed: " +
                String.format("%.4f", ((double) list2.size()) / time) + " records/s");
        if (list1.size() != list2.size()) {
            throw new RuntimeException ("Error: list1 size: " + list1.size() +
                    ", list2 size: " + list2.size());
        }
    }
}
