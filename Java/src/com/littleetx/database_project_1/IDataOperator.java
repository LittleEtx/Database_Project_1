package com.littleetx.database_project_1;

import com.littleetx.database_project_1.records.Item;

import java.util.List;

public interface IDataOperator {
    void initialize();
    void importData(TableInfo info);
    void delete(List<Item> itemList);
    void updateItemType(String oldType, String newType);
    List<Item> findUnfinishedItems();
}
