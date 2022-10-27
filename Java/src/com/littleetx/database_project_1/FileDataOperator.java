package com.littleetx.database_project_1;

import com.littleetx.database_project_1.file_database.Database;
import com.littleetx.database_project_1.file_database.Table;
import com.littleetx.database_project_1.records.Information;
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

    @Override
    public void insert(Information info) {

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
        insertInto("retrieval", info.retrievals(), retrieval -> new Object[] {
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
        for (T info : infos) {
            infoList.add(getField.apply(info));
        }
        table.insert(infoList);
        Logger.log("Insert into " + tableName + " in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + infoList.size() + " records");
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

}
