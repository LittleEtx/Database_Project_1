package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.file_database.FileOperator;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.records.City;
import com.littleetx.database_project_1.records.Company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetMinExportRate {
    public static void main(String[] args) {
        FileDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();

        System.out.println("Start finding min export rate...");
        long t = System.currentTimeMillis();
        var result = fileOperator.getMinExportRate();
        long time = System.currentTimeMillis() - t;
        System.out.println("Found " + result.size() + " records in " + time + "ms, speed: " +
                String.format("%.4f", ((double) result.size()) / time) + " records/s");

        //insertIntoFile(result);
    }

    private static void insertIntoFile(Map<Company, Map<String, List<City>>> result) {
        FileOperator output = new FileOperator_CSV("results/GetMinExportRate.csv");
        List<String[]> results = new ArrayList<>();
        for (Company company : result.keySet()) {
            var companyTax = result.get(company);
            for (String type : companyTax.keySet()) {
                for (City city : companyTax.get(type)) {
                    results.add(new String[]{company.name(), type, city.name()});
                }
            }
        }
        output.insertRows(results);
    }
}
