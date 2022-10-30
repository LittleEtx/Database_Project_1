package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.file_database.FileOperator;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.records.Container;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindContainerServiceYear {
    public static void main(String[] args) {
        IDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();
        System.out.println("Start finding service year of containers...");
        long t = System.currentTimeMillis();
        var result = fileOperator.findContainerServiceYear();
        long time = System.currentTimeMillis() - t;
        System.out.println("Found " + result.size() + " records in " + time + "ms, speed: " +
                String.format("%.4f", ((double) result.size()) / time) + " records/s");

        //insertIntoFile(result);
    }

    private static void insertIntoFile(Map<Container, LocalDate> result) {
        FileOperator output = new FileOperator_CSV("results/FindContainerServiceYear.csv");
        List<String[]> results = new ArrayList<>();
        for (Container container : result.keySet()) {
            results.add(new String[]{container.code(), result.get(container).toString()});
        }
        output.insertRows(results);
    }
}
