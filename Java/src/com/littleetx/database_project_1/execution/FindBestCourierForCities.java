package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.file_database.FileOperator;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.records.City;
import com.littleetx.database_project_1.records.Company;
import com.littleetx.database_project_1.records.Courier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindBestCourierForCities {
    public static void main(String[] args) {
        IDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();

        var result1 = getBestCouriers(fileOperator, IDataOperator.CourierType.Retrieval);
        //writeIntoFiles(result1, "Retrieval");

        var result2 = getBestCouriers(fileOperator, IDataOperator.CourierType.Delivery);
        //writeIntoFiles(result2, "Delivery");
    }

    @NotNull
    private static Map<Company, Map<City, List<Courier>>> getBestCouriers(
            IDataOperator fileOperator, IDataOperator.CourierType type) {
        System.out.println("Start finding the best " + type + " couriers...");
        long t = System.currentTimeMillis();
        var result = fileOperator
                .findBestCourierForCities(type);
        long time = System.currentTimeMillis() - t;
        System.out.println("Found " + result.size() + " records in " + time + "ms, speed: " +
                String.format("%.4f", ((double) result.size()) / time) + " records/s");
        return result;
    }

    private static void writeIntoFiles(Map<Company, Map<City, List<Courier>>> result, String type) {
        FileOperator file = new FileOperator_CSV("results/FindBest" + type +"CourierForCities.csv");
        List<String[]> results = new ArrayList<>();
        for (Company company : result.keySet()) {
            var cityMap = result.get(company);
            for (City city : cityMap.keySet()) {
                for (Courier courier : cityMap.get(city)) {
                    results.add(new String[]{company.name(), city.name(), courier.name()});
                }
            }
        }
        file.insertRows(results);
    }
}
