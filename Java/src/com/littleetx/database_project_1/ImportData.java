package com.littleetx.database_project_1;

import com.littleetx.database_project_1.file_database.FileOperator;
import com.littleetx.database_project_1.file_database.FileOperatorReader;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.records.City;
import com.littleetx.database_project_1.records.Item;
import com.littleetx.database_project_1.records.Log;
import com.littleetx.database_project_1.records.Route;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportData {

    private static final String DataFile = "data.csv";

    public static List<Log> importData() {
        FileOperator fileOperator = new FileOperator_CSV(DataFile);
        List<Log> list = new ArrayList<>();
        try {
            Iterator<String[]> reader = fileOperator.getReader().iterator();
            reader.next();
            while (reader.hasNext()) {
                String[] info = reader.next();
                Item item = new Item(info[0], info[1], Integer.parseInt(info[2]));
                //City c1 = new City(info[3], )


                //Route route = new Route(info[3], info[4], info[5], info[6]);




            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file " + DataFile, e);
        }

        return list;
    }

}
