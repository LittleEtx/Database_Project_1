package com.littleetx.database_project_1;

import com.littleetx.database_project_1.file_database.Database;
import com.littleetx.database_project_1.file_database.Table;
import com.littleetx.database_project_1.records.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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
    public void delete(Collection<Item> itemList) {
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
        long time = System.currentTimeMillis() - t;
        Logger.log("Delete from " + tableName + " in: " + time + "ms, " +
                "total: "  + count + " records, speed: " + String.format("%.4f", ((double) count) / time) + " records/ms");
    }

    @Override
    public void updateItemType(String oldType, String newType) {
        long t = System.currentTimeMillis();
        Table table = database.getTable("item");
        Objects.requireNonNull(table);
        long time = System.currentTimeMillis() - t;
        int count = table.update("type", oldType, "type", newType);
        Logger.log("Update item types in: " + time + "ms, " +
                "total: "  + count + " records, speed: " + String.format("%.4f", ((double) count) / time) + " records/ms");
    }


    @Override
    public Collection<Item> findUnfinishedItems(FindType type) {
        long t = System.currentTimeMillis();
        Map<String, Item> allItems = getAllItems();

        Table table = database.getTable("delivery");
        Objects.requireNonNull(table);
        if (type == FindType.DeleteIfFind) {
            for (String s : allItems.keySet()) {
                if (!table.select("item_name", s).isEmpty()) {
                    allItems.remove(s);
                }
            }
        } else {
            for (Object[] row : table.select()) {
                String itemName = (String) row[0];
                allItems.remove(itemName);
            }
        }

        long time = System.currentTimeMillis() - t;
        Logger.log("Find unfinished items in: " + time + "ms, " +
                "total: "  + allItems.size() + " records, speed: " + String.format("%.4f", ((double) allItems.size()) / time) + " records/ms");

        return allItems.values();
    }

    @Override
    public Map<Ship, LocalDate> findShipServiceYear() {
        HashMap<Integer, Company> companies = getCompanies();

        Map<Integer, Ship> ships = getAllInfos("ship",
                row -> (int) row[0],
                row -> new Ship((int) row[0], (String) row[1], companies.get((int) row[2])
        ));

        HashMap<Ship, LocalDate> shipServiceYear = new HashMap<>();
        Table exportTable = database.getTable("export");
        //read all export records for once
        Objects.requireNonNull(exportTable);
        for (Object[] row : exportTable.select()) {
            Ship ship = ships.get((int) row[3]);
            LocalDate date = (LocalDate) row[1];
            if (shipServiceYear.containsKey(ship)) {
                if (shipServiceYear.get(ship).compareTo(date) > 0) {
                    shipServiceYear.put(ship, date);
                }
            } else {
                shipServiceYear.put(ship, date);
            }
        }
        return shipServiceYear;
    }

    public Map<City, Courier> findBestCourierForCities(CourierType type) {
        Map<Integer, Company> companies = getCompanies();
        Map<Integer, Courier> couriers = getAllInfos("courier",
                row -> (int) row[0],
                row -> new Courier((int) row[0], (String) row[1], (String) row[2],
                        (String) row[3], (LocalDate) row[4], companies.get((int) row[5])
        ));

        //TODO: use city and courier to find the best courier for each city
        Table cites;

        class CityCourier {
            public final City city;
            public final Courier courier;
            public int count;

            public CityCourier(City city, Courier courier, int count) {
                this.city = city;
                this.courier = courier;
                this.count = count;
            }
        }

        Map<Courier, CityCourier> courierTransportItemCount = new HashMap<>();
        for (Courier courier : couriers.values()) {
            courierTransportItemCount.put(courier, new CityCourier(null, courier, 0));
        }


        Table table;
        if (type == CourierType.Delivery) {
            table = database.getTable("delivery");
        } else {
            table = database.getTable("retrieval");
        }
        for (Object[] row : Objects.requireNonNull(table).select()) {
            Courier courier = couriers.get((int) row[2]);
            //courierTransportItemCount.put(courier, courierTransportItemCount.get(courier) + 1);
        }
        return null;
    }

    @Override
    public Map<String, BigDecimal> getMinExportRate(Company company) {
        return null;
    }

    private <I, T> HashMap<I, T> getAllInfos(String tableName, Function<Object[], I> key,
                                             Function<Object[], T> constructor) {
        HashMap<I, T> map = new HashMap<>();
        Table table = database.getTable(tableName);
        Objects.requireNonNull(table);
        for (Object[] row : table.select()) {
            map.put(key.apply(row), constructor.apply(row));
        }
        return map;
    }

    @NotNull
    private HashMap<Integer, Company> getCompanies() {
        return getAllInfos("company",
                row -> (int) row[0],
                row -> new Company((Integer) row[0], (String) row[1]));
    }

    @NotNull
    @Override
    public Map<String, Item> getAllItems() {
        return getAllInfos("item",
                row -> (String) row[0],
                row -> new Item((String) row[0], (String) row[1], (int) row[2]));
    }
}
