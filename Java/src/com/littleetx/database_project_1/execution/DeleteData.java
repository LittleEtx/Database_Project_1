package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.Logger;
import com.littleetx.database_project_1.SQLDataOperator;
import com.littleetx.database_project_1.records.Item;

import java.util.*;

public class DeleteData {
    private static final int sqlDeleteCount = 100000;
    private static final int fileDeleteCount = 25;

    public static void main(String[] args) {
        Logger.setStream(System.out);
        SQLDataOperator sqlOperator = new SQLDataOperator();
        sqlOperator.initialize();
        deleteFrom(sqlOperator, sqlDeleteCount, "SQL");

        IDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();
        deleteFrom(fileOperator, fileDeleteCount, "File");
    }

    private static void deleteFrom(IDataOperator dataOperator, int count, String name) {
        System.out.println("Loading data from " + name + "...");
        var itemSet = dataOperator.getAllItems();

        Collection<Item> itemList = getItemList(itemSet.values(), count);
        System.out.println("Loaded " + itemList.size() + " items.");

        long sqlStart = System.currentTimeMillis();
        dataOperator.delete(itemList);
        System.out.println(name + " deleted " + itemList.size() + " records in " +
                (System.currentTimeMillis() - sqlStart) + "ms.");
    }


    private static Collection<Item> getItemList(Collection<Item> items, int count) {
        List<Item> result = new ArrayList<>();
        Random random = new Random();

        Set<Integer> randomSet = new HashSet<>();
        int i = 0;
        while (i < count) {
            int value = random.nextInt(items.size());
            if (randomSet.add(value)) {
                i++;
            }
        }
        Integer[] randomList = randomSet.toArray(new Integer[0]);
        Arrays.sort(randomList);

        i = 0;
        int j = 0;
        for (Item item : items) {
            if (i >= count) {
                break;
            }
            if (j == randomList[i]) {
                result.add(item);
                i++;
            }
            j++;
        }

        return result;
    }
}
