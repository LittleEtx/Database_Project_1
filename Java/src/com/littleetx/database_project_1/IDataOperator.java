package com.littleetx.database_project_1;

import com.littleetx.database_project_1.records.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IDataOperator {
    void initialize();
    Map<String, Item> getAllItems();
    void importData(TableInfo info, long packageSize);
    void delete(Collection<Item> itemList);
    void updateItemType(String oldType, String newType);

    enum FindType {
        QueryAndDelete, DeleteIfFind
    }
    Collection<Item> findUnfinishedItems(FindType type);

    Map<Container, LocalDate> findContainerServiceYear();
    enum CourierType {
        Retrieval, Delivery
    }
    Map<Company, Map<City, List<Courier>>> findBestCourierForCities(CourierType type);
    Map<Company, Map<String, List<City>>> getMinExportRate();
}
