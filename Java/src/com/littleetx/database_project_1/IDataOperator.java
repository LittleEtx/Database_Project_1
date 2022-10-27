package com.littleetx.database_project_1;

import com.littleetx.database_project_1.records.Information;
import com.littleetx.database_project_1.records.Item;

public interface IDataOperator {
    void initialize();
    void insert(Information info);
    void delete(Item item);
    void fillInAllNullValues();
    Item[] findUnfinishedItems();
}
