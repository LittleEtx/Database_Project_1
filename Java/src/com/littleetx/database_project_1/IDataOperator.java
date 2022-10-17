package com.littleetx.database_project_1;

import com.littleetx.database_project_1.records.Item;
import com.littleetx.database_project_1.records.Log;

public interface IDataOperator {
    void insert(Log log);
    void delete(Item item);
    void fillInAllNullValues();
    Item[] findUnfinishedItems();
}