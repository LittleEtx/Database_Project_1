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
            allItems.keySet().removeIf(s -> !table.select("item_name", s).isEmpty());
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
    public Map<Container, LocalDate> findContainerServiceYear() {
        Map<String, Container> containers = getContainers();

        HashMap<Container, LocalDate> containerServiceYear = new HashMap<>();
        Table exportTable = database.getTable("export");
        //read all export records for once
        Objects.requireNonNull(exportTable);
        for (Object[] row : exportTable.select()) {
            Container container = containers.get((String) row[2]);
            LocalDate date = (LocalDate) row[1];
            if (containerServiceYear.containsKey(container)) {
                if (containerServiceYear.get(container).compareTo(date) > 0) {
                    containerServiceYear.put(container, date);
                }
            } else {
                containerServiceYear.put(container, date);
            }
        }
        return containerServiceYear;
    }

    @NotNull
    public Map<String, Container> getContainers() {
        return getAllInfos("container",
                row -> (String) row[0],
                row -> new Container((String) row[0], (String) row[1])
        );
    }

    @Override
    public Map<Company, Map<City, List<Courier>>> findBestCourierForCities(CourierType type) {
        Map<String, Item> items = getAllItems();
        Map<Integer, Company> companies = getCompanies();
        Map<Integer, Courier> couriers = getCouriers(companies);
        Map<Integer, City> cities = getCities();

        Map<String, City> itemCityMap = new HashMap<>();
        Table table;
        if (type == CourierType.Delivery) {
            table = database.getTable("delivery");
            Objects.requireNonNull(database.getTable("item_via_city"))
                    .select().forEach(row -> {
                if (row[4] != null) {
                    String itemName = (String) row[0];
                    City city = cities.get((int) row[4]);
                    itemCityMap.put(itemName, city);
                }
            });

        } else { //retrieval
            table = database.getTable("retrieval");
            Objects.requireNonNull(database.getTable("item_via_city"))
                    .select().forEach(row -> {
                if (row[1] != null) {
                    String itemName = (String) row[0];
                    City city = cities.get((int) row[1]);
                    itemCityMap.put(itemName, city);
                }
            });
        }

        //get all the courier records
        Map<Company, Map<City, Map<Courier, Integer>>> companyCityCourierCount = new HashMap<>();
        for (Company company : companies.values()) {
            companyCityCourierCount.put(company, new HashMap<>());
        }

        for (Object[] row : Objects.requireNonNull(table).select()) {
            Item item = items.get((String) row[0]);
            Courier courier = couriers.get((int) row[2]);
            City city = itemCityMap.get(item.name());
            var cityCourierMap = companyCityCourierCount.get(courier.company());
            if (!cityCourierMap.containsKey(city)) {
                cityCourierMap.put(city, new HashMap<>());
            }
            var courierCount = cityCourierMap.get(city);
            if (courierCount.containsKey(courier)) {
                courierCount.put(courier, courierCount.get(courier) + 1);
            } else {
                courierCount.put(courier, 1);
            }
        }

        Map<Company, Map<City, List<Courier>>> result = new HashMap<>();
        for (Company company : companies.values()) {
            result.put(company, new HashMap<>());
        }

        for (Company company : result.keySet()) {
            var cityMap = companyCityCourierCount.get(company);
            for (City city : cityMap.keySet()) {
                List<Courier> bestCouriers = new ArrayList<>();
                int maxCount = 0;
                var courierCount = cityMap.get(city);
                for (Courier courier : courierCount.keySet()) {
                    int count = courierCount.get(courier);
                    if (count > maxCount) {
                        maxCount = count;
                        bestCouriers.clear();
                        bestCouriers.add(courier);
                    } else if (count == maxCount) {
                        bestCouriers.add(courier);
                    }
                }
                Logger.log("Best couriers for " + company.name() + " in " + city.name() + ": "
                        + bestCouriers + ", having transport " + maxCount + " times");
                result.get(company).put(city, bestCouriers);
            }
        }
        return result;
    }

    private Map<Integer, Courier> getCouriers(Map<Integer, Company> companies) {
        return getAllInfos("courier",
                row -> (int) row[0],
                row -> new Courier((int) row[0], (String) row[1], (String) row[2],
                        (String) row[3], (LocalDate) row[4], companies.get((int) row[5]))
        );
    }

    private Map<Integer, City> getCities() {
        return getAllInfos("city",
                row -> (int) row[0],
                row -> new City((int) row[0], (String) row[1], (String) row[2])
        );
    }

    @Override
    public Map<Company, Map<String, List<City>>> getMinExportRate() {
        var items = getAllItems();
        var companies = getCompanies();
        var cites = getCities();
        var couriers = getCouriers(companies);

        Map<String, Company> itemCompanyMap = getItemCompanyMap(couriers);

        var taxInfo = getAllInfos("tax_info",
                row -> (String) row[0],
                row -> new TaxInfo((items.get((String)row[0])),
                        (BigDecimal) row[1], (BigDecimal) row[2])
        );

        Map<Company, Map<String, Map<City, BigDecimal>>> companyMap = new HashMap<>();
        for (Company company : companies.values()) {
            companyMap.put(company, new HashMap<>());
        }

        for (Object[] row : Objects.requireNonNull(database
                .getTable("item_via_city")).select()) {
            String itemName = (String) row[0];
            Item item = items.get(itemName);
            City city = cites.get((int) row[1]);
            var company = companyMap.get(itemCompanyMap.get(itemName));
            BigDecimal exportRate = taxInfo.get(item.name()).exportRate();
            if (company.containsKey(item.type())) {
                company.get(item.type()).put(city, exportRate);
            } else {
                Map<City, BigDecimal> cityExportRate = new HashMap<>();
                cityExportRate.put(city, exportRate);
                company.put(item.type(), cityExportRate);
            }

        }

        Map<Company, Map<String, List<City>>> result = new HashMap<>();
        //find the best cities for all companies
        for (Company company : companies.values()) {
            Map<String, List<City>> itemCityMap = new HashMap<>();
            var companyTaxInfo = companyMap.get(company);
            //find the best cities for each type
            for (String type : companyTaxInfo.keySet()) {
                Map<City, BigDecimal> cityExportRate = companyTaxInfo.get(type);
                List<City> bestCities = new ArrayList<>();
                for (City city : cityExportRate.keySet()) {
                    if (bestCities.isEmpty()) {
                        bestCities.add(city);
                    } else {
                        BigDecimal rate = cityExportRate.get(city);
                        BigDecimal bestRate = cityExportRate.get(bestCities.get(0));
                        if (rate.compareTo(bestRate) < 0) {
                            bestCities.clear();
                            bestCities.add(city);
                        } else if (rate.compareTo(bestRate) == 0) {
                            bestCities.add(city);
                        }
                    }
                }
                itemCityMap.put(type, bestCities);
            }
            result.put(company, itemCityMap);
        }


        return result;
    }

    @NotNull
    private Map<String, Company> getItemCompanyMap(Map<Integer, Courier> couriers) {
        Map<String, Company> itemCompanyMap = new HashMap<>();
        Objects.requireNonNull(database.getTable("retrieval")).select().forEach(row -> {
            Company company = couriers.get((Integer) row[2]).company();
            itemCompanyMap.put((String) row[0], company);
        });
        Logger.log("get item company map: " + itemCompanyMap.size());
        return itemCompanyMap;
    }

    public <I, T> HashMap<I, T> getAllInfos(String tableName, Function<Object[], I> key,
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
    public HashMap<Integer, Company> getCompanies() {
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
