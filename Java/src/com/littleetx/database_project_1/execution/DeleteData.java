package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.Logger;
import com.littleetx.database_project_1.SQLDataOperator;
import com.littleetx.database_project_1.file_database.FileOperator;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.records.Item;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.littleetx.database_project_1.DataIndexMapping.*;

public class DeleteData {
    private static final String DataFile = "data.csv";

    public static void main(String[] args) {
        Logger.setStream(System.out);

        IDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();
        IDataOperator sqlOperator = new SQLDataOperator();
        sqlOperator.initialize();

        System.out.println("Loading data...");
        List<Item> itemList = readRandomItems(0.00005);
        System.out.println("Loaded " + itemList.size() + " items.");

        long sqlStart = System.currentTimeMillis();
        sqlOperator.delete(itemList);
        System.out.println("SQL deleted " + itemList.size() + " items in " +
                (System.currentTimeMillis() - sqlStart) + "ms.");

        long fileStart = System.currentTimeMillis();
        fileOperator.delete(itemList);
        System.out.println("File database deleted " + itemList.size() + " items in " +
                (System.currentTimeMillis() - fileStart) + "ms.");
    }

    private static List<Item> readRandomItems(double ration) {
        List<Item> result = new ArrayList<>();
        FileOperator fileOperator = new FileOperator_CSV(DataFile);
        Random random = new Random();
        try {
            Iterator<String[]> reader = fileOperator.getReader().iterator();
            reader.next();
            while (reader.hasNext()) {
                String[] infos = reader.next();
                if (random.nextDouble() < ration) {
                    result.add(new Item(infos[ItemName], infos[ItemType],
                            Integer.parseInt(infos[ItemPrice])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
