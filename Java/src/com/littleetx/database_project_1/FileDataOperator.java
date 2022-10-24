package com.littleetx.database_project_1;

import com.littleetx.database_project_1.records.Company;
import com.littleetx.database_project_1.records.Item;
import com.littleetx.database_project_1.records.Log;
import com.littleetx.database_project_1.records.Ship;

public class FileDataOperator implements IDataOperator {

    @Override
    public void insert(Log log) {
        










    }

    @Override
    public void delete(Item item) {

    }

    @Override
    public void fillInAllNullValues() {

    }

    @Override
    public Item[] findUnfinishedItems() {
        return new Item[0];
    }

    public static void main(String[] args) {
        Item item = new Item("apple_11", "apple", 30);
        System.out.println(item);
        Company company = new Company("SUSTC");
        Ship ship = new Ship("deep blue", company);
        System.out.println(ship);
    }

}
