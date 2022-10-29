package com.littleetx.database_project_1;

import com.littleetx.database_project_1.file_database.Database;
import com.littleetx.database_project_1.file_database.Table;
import com.littleetx.database_project_1.records.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class FileDataOperator implements IDataOperator {
    private Database database;

    @Override
    public void initialize() {
        database = new Database();
        database.initialize();
    }

    private long packageSize;
    @Override
    public void importData(TableInfo info, long packageSize) {
        this.packageSize = packageSize;
        insertInto("city", info.cities(), city -> new Object[] {
                city.id(), city.name(), city.areaCode()});
        insertInto("company", info.companies(), company -> new Object[] {
                company.id(), company.name()});
        insertInto("container", info.containers(), container -> new Object[] {
                container.code(), container.type()});
        insertInto("courier", info.couriers(), courier -> new Object[] {
                courier.id(), courier.name(), courier.gender(),
                courier.phoneNumber(), courier.birthday(), courier.company().id()});
        insertInto("item", info.items(), item -> new Object[] {
                item.name(), item.type(), item.price()});
        insertInto("ship", info.ships(), ship -> new Object[] {
                ship.id(), ship.name(), ship.company().id()});
        insertInto("delivery", info.deliveries(), delivery -> new Object[] {
                delivery.item().name(), delivery.date(), delivery.courier().id()});
        insertInto("export", info.exports(), export -> new Object[] {
                export.item().name(), export.date(),
                export.container().code(), export.ship().id()});
        insertInto("import", info.imports(), import_ -> new Object[] {
                import_.item().name(), import_.date()});
        insertInto("item_via_city", info.itemsViaCities(), itemViaCity -> new Object[] {
                itemViaCity.item().name(), itemViaCity.retrievalCity().id(),
                itemViaCity.exportCity().id(), itemViaCity.importCity().id(), itemViaCity.deliveryCity().id()});
        insertInto("log", info.logs(), log -> new Object[] {
                log.item().name(), log.logTime()});
        insertInto("retrieval", info.retrieves(), retrieval -> new Object[] {
                retrieval.item().name(), retrieval.date(), retrieval.courier().id()});
        insertInto("tax_info", info.taxInfos(), taxInfo -> new Object[] {
                taxInfo.item().name(), taxInfo.exportRate(), taxInfo.importRate()});
    }

    private <T> void insertInto(String tableName, Collection<T> infos, Function<T, Object[]> getField) {
        //insert into tax_info
        long t = System.currentTimeMillis();
        Table table = database.getTable(tableName);
        Objects.requireNonNull(table);
        List<Object[]> infoList = new ArrayList<>();
        long count = 0;
        for (T info : infos) {
            infoList.add(getField.apply(info));
            if (++count % packageSize == 0) {
                table.insert(infoList);
                infoList.clear();
            }
        }
        if (!infoList.isEmpty()) {
            table.insert(infoList);
        }
        long time = System.currentTimeMillis() - t;
        Logger.log("Insert into " + tableName + " in: " + time + "ms, " +
                "total: "  + infos.size() + " records, speed: " +
                String.format("%.4f", ((double) infos.size()) / time) + " records/ms");
    }

    @Override
    public void delete(List<Item> itemList) {
        List<Object> itemNameList = new ArrayList<>();
        for (Item item : itemList) {
            itemNameList.add(item.name());
        }
        delete("delivery", "item_name", itemNameList);
        delete("export", "item_name", itemNameList);
        delete("import", "item_name", itemNameList);
        delete("item_via_city", "item_name", itemNameList);
        delete("log", "item_name", itemNameList);
        delete("retrieval", "item_name", itemNameList);
        delete("tax_info", "item_name", itemNameList);
        delete("item", "name", itemNameList);
    }

    private void delete(String tableName, String columns, Collection<Object> rows) {
        //insert into tax_info
        long t = System.currentTimeMillis();
        Table table = database.getTable(tableName);
        Objects.requireNonNull(table);
        int count = 0;
        for (Object row : rows) {
            count += table.delete(columns, row);
        }
        Logger.log("Delete from " + tableName + " in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");
    }

    @Override
    public void updateItemType(String oldType, String newType) {
        long t = System.currentTimeMillis();
        Table table = database.getTable("item");
        Objects.requireNonNull(table);

        int count = table.update("type", oldType, "type", newType);
        Logger.log("Update item types in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");
    }


    @Override
    public List<Item> findUnfinishedItems() {
        return null;
    }

}
