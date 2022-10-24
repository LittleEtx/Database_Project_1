package com.littleetx.database_project_1;

import com.littleetx.database_project_1.file_database.Database;
import com.littleetx.database_project_1.file_database.Table;
import com.littleetx.database_project_1.records.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileDataOperator implements IDataOperator {

    @Override
    public void insert(Information info) {
        Database database = new Database();
        database.initialize();

        //import into city
        long t = System.currentTimeMillis();
        int count = 0;
        Table cities = database.getTable("city");
        Objects.requireNonNull(cities);
        List<Object[]> cityList = new ArrayList<>();
        for (City city : info.cities()) {
            cityList.add(new Object[] {city.id(), city.name(), city.areaCode()});
            count++;
        }
        cities.insert(cityList);
        Logger.log("Insert cities in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //import into company
        t = System.currentTimeMillis();
        count = 0;
        Table companies = database.getTable("company");
        Objects.requireNonNull(companies);
        List<Object[]> companyList = new ArrayList<>();
        for (Company company : info.companies()) {
            companyList.add(new Object[] {company.id(), company.name()});
            count++;
        }
        companies.insert(companyList);
        Logger.log("Insert companies in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //import into containers
        t = System.currentTimeMillis();
        count = 0;
        Table containers = database.getTable("container");
        Objects.requireNonNull(containers);
        List<Object[]> containerList = new ArrayList<>();
        for (Container container : info.containers()) {
            containerList.add(new Object[] {container.code(), container.type()});
            count++;
        }
        containers.insert(containerList);
        Logger.log("Insert containers in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert int couriers
        t = System.currentTimeMillis();
        count = 0;
        Table couriers = database.getTable("courier");
        Objects.requireNonNull(couriers);
        List<Object[]> courierList = new ArrayList<>();
        for (Courier courier : info.couriers()) {
            courierList.add(new Object[] {courier.id(), courier.name(), courier.gender(),
                    courier.phoneNumber(), courier.birthday(), courier.company().id()});
            count++;
        }
        couriers.insert(courierList);
        Logger.log("Insert couriers in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into items
        t = System.currentTimeMillis();
        count = 0;
        Table items = database.getTable("item");
        Objects.requireNonNull(items);
        List<Object[]> itemList = new ArrayList<>();
        for (Item item : info.items()) {
            itemList.add(new Object[] {item.id(), item.type(), item.price()});
            count++;
        }
        items.insert(itemList);
        Logger.log("Insert items in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into ships
        t = System.currentTimeMillis();
        count = 0;
        Table ships = database.getTable("ship");
        Objects.requireNonNull(ships);
        List<Object[]> shipList = new ArrayList<>();
        for (Ship ship : info.ships()) {
            shipList.add(new Object[] {ship.id(), ship.name(), ship.company().id()});
            count++;
        }
        ships.insert(shipList);
        Logger.log("Insert ships in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into deliveries
        t = System.currentTimeMillis();
        count = 0;
        Table deliveries = database.getTable("delivery");
        Objects.requireNonNull(deliveries);
        List<Object[]> deliveryList = new ArrayList<>();
        for (Delivery delivery : info.deliveries()) {
            deliveryList.add(new Object[] {delivery.item().id(), delivery.date(), delivery.courier().id()});
            count++;
        }
        deliveries.insert(deliveryList);
        Logger.log("Insert deliveries in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into exports
        t = System.currentTimeMillis();
        count = 0;
        Table exports = database.getTable("export");
        Objects.requireNonNull(exports);
        List<Object[]> exportList = new ArrayList<>();
        for (Export export : info.exports()) {
            exportList.add(new Object[] {export.item().id(), export.date(),
                    export.container().code(), export.ship().id()});
            count++;
        }
        exports.insert(exportList);
        Logger.log("Insert exports in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into imports
        t = System.currentTimeMillis();
        count = 0;
        Table imports = database.getTable("import");
        Objects.requireNonNull(imports);
        List<Object[]> importList = new ArrayList<>();
        for (Import import_ : info.imports()) {
            importList.add(new Object[] {import_.item().id(), import_.date()});
            count++;
        }
        imports.insert(importList);
        Logger.log("Insert imports in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into itemViaCities
        t = System.currentTimeMillis();
        count = 0;
        Table itemsViaCities = database.getTable("item_via_city");
        Objects.requireNonNull(itemsViaCities);
        List<Object[]> itemsViaCitiesList = new ArrayList<>();
        for (ItemViaCity itemViaCity : info.itemsViaCities()) {
            itemsViaCitiesList.add(new Object[] {itemViaCity.item().id(), itemViaCity.retrievalCity().id(),
                    itemViaCity.exportCity().id(), itemViaCity.importCity().id(), itemViaCity.deliveryCity().id()});
            count++;
        }
        itemsViaCities.insert(itemsViaCitiesList);
        Logger.log("Insert itemsViaCities in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into logs
        t = System.currentTimeMillis();
        count = 0;
        Table logs = database.getTable("log");
        Objects.requireNonNull(logs);
        List<Object[]> logsList = new ArrayList<>();
        for (Log log : info.logs()) {
            logsList.add(new Object[] {log.item().id(), log.logTime()});
            count++;
        }
        logs.insert(logsList);
        Logger.log("Insert logs in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into retrieval
        t = System.currentTimeMillis();
        count = 0;
        Table retrievals = database.getTable("retrieval");
        Objects.requireNonNull(retrievals);
        List<Object[]> retrievalsList = new ArrayList<>();
        for (Retrieval retrieval : info.retrievals()) {
            retrievalsList.add(new Object[] {retrieval.item().id(), retrieval.date(), retrieval.courier().id()});
            count++;
        }
        retrievals.insert(retrievalsList);
        Logger.log("Insert retrievals in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");

        //insert into tax_info
        t = System.currentTimeMillis();
        count = 0;
        Table taxInfos = database.getTable("tax_info");
        Objects.requireNonNull(taxInfos);
        List<Object[]> taxInfosList = new ArrayList<>();
        for (TaxInfo taxInfo : info.taxInfos()) {
            taxInfosList.add(new Object[] {taxInfo.item().id(), taxInfo.exportRate(), taxInfo.importRate()});
            count++;
        }
        taxInfos.insert(taxInfosList);
        Logger.log("Insert taxInfos in: " + (System.currentTimeMillis() - t) + "ms, " +
                "total: "  + count + " records");
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
