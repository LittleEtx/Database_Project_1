package com.littleetx.database_project_1;

import com.littleetx.database_project_1.records.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
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

    Map<Ship, LocalDate> findShipServiceYear();
    enum CourierType {
        Retrieval, Delivery
    }
    Map<City, Courier> findBestCourierForCities(CourierType type);
    Map<String, BigDecimal> getMinExportRate(Company company);
}
