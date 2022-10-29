package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.*;
import com.littleetx.database_project_1.file_database.FileOperator;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.records.*;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.littleetx.database_project_1.DataIndexMapping.*;

public class ImportData {

    private static final String DataFile = "data.csv";

    public static TableInfo importData() {
        Map<String, City> cities = new HashMap<>();
        int cityId = 1;
        Map<String, Company> companies = new HashMap<>();
        int companyId = 1;
        Map<String, Container> containers = new HashMap<>();
        Map<String, Courier> couriers = new HashMap<>();
        int courierId = 1;
        List<Delivery> deliveries = new ArrayList<>();
        List<Export> exports = new ArrayList<>();
        List<Import> imports = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        List<ItemViaCity> itemsViaCities = new ArrayList<>();
        List<Log> logs = new ArrayList<>();
        List<Retrieve> retrieves = new ArrayList<>();
        Map<String, Ship> ships = new HashMap<>();
        int shipId = 1;
        List<TaxInfo> taxInfos = new ArrayList<>();

        FileOperator fileOperator = new FileOperator_CSV(DataFile);
        try {
            Iterator<String[]> reader = fileOperator.getReader().iterator();
            reader.next();
            while (reader.hasNext()) {
                String[] info = reader.next();
                Item item = new Item(info[ItemName],
                        info[ItemType], Integer.parseInt(info[ItemPrice]));
                items.add(item);

                City retrievalCity = cities.get(info[RetrievalCity]);
                if (retrievalCity == null) {
                    retrievalCity = new City(cityId++, info[RetrievalCity],
                            info[RetrievalCourierPhoneNumber].substring(0,4));
                    cities.put(info[RetrievalCity], retrievalCity);
                }

                City deliveryCity = cities.get(info[DeliveryCity]);
                if (deliveryCity == null) {
                    deliveryCity = new City(cityId++, info[DeliveryCity],
                            info[DeliveryCourierPhoneNumber] != null ?
                                    info[DeliveryCourierPhoneNumber].substring(0,4) : null);
                    cities.put(info[DeliveryCity], deliveryCity);
                }

                City exportCity = cities.get(info[ItemExportCity]);
                if (exportCity == null) {
                    exportCity = new City(cityId++, info[ItemExportCity], null);
                    cities.put(info[ItemExportCity], exportCity);
                }
                City importCity = cities.get(info[ItemExportCity]);
                if (importCity == null) {
                    importCity = new City(cityId++, info[ItemImportCity], null);
                    cities.put(info[ItemImportCity], importCity);
                }

                ItemViaCity itemViaCity = new ItemViaCity(item,
                        retrievalCity, exportCity, importCity, deliveryCity);
                itemsViaCities.add(itemViaCity);
                TaxInfo taxInfo = new TaxInfo(item, new BigDecimal(info[ItemExportTax]),
                        new BigDecimal(info[ItemImportTax]));
                taxInfos.add(taxInfo);

                Company company = companies.get(info[CompanyName]);
                if (company == null) {
                    company = new Company(companyId++, info[CompanyName]);
                    companies.put(info[CompanyName], company);
                }

                //retrieval
                String retrievalPhone = info[RetrievalCourierPhoneNumber].substring(5);
                Courier courier = couriers.get(retrievalPhone);
                if (courier == null) {
                    courier= new Courier(courierId++, info[RetrievalCourier],
                            info[RetrievalCourierGender], retrievalPhone,
                            LocalDate.ofYearDay(Integer.parseInt(info[RetrievalStartTime].substring(0,4)) -
                                    Integer.parseInt(info[RetrievalCourierAge]), 1), company);
                    couriers.put(retrievalPhone, courier);
                }

                retrieves.add(new Retrieve(item,
                        LocalDate.parse(info[RetrievalStartTime]), courier));

                //export
                if (info[ItemExportTime] != null) {
                    Container container = containers.get(info[ContainerCode]);
                    if (container == null) {
                        container = new Container(info[ContainerCode], info[ContainerType]);
                        containers.put(info[ContainerCode], container);
                    }
                    Ship ship = ships.get(info[ShipName]);
                    if (ship == null) {
                        ship = new Ship(shipId++, info[ShipName], company);
                        ships.put(info[ShipName], ship);
                    }
                    exports.add(new Export(item, LocalDate.parse(info[ItemExportTime]), container, ship));
                }

                //import
                if (info[ItemImportTime] != null) {
                    imports.add(new Import(item, LocalDate.parse(info[ItemImportTime])));
                }

                //delivery
                if (info[DeliveryFinishedTime] != null) {
                    String deliveryPhone = info[DeliveryCourierPhoneNumber].substring(5);
                    Courier deliveryCourier = couriers.get(deliveryPhone);
                    if (deliveryCourier == null) {
                        deliveryCourier = new Courier(courierId++, info[DeliveryCourier],
                                info[DeliveryCourierGender], deliveryPhone,
                                LocalDate.ofYearDay(Integer.parseInt(info[DeliveryFinishedTime].substring(0,4)) -
                                        (int)Double.parseDouble(info[DeliveryCourierAge]), 1), company);
                        couriers.put(deliveryPhone, deliveryCourier);
                    }
                    deliveries.add(new Delivery(item, LocalDate.parse(info[DeliveryFinishedTime]),
                            deliveryCourier));
                }

                Log log = new Log(item, LocalDateTime.parse(info[LogTime]
                        .replace(" ", "T")));
                logs.add(log);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file " + DataFile, e);
        }
        return new TableInfo(cities.values(), companies.values(), containers.values(),
                couriers.values(), deliveries, exports, imports,
                items, itemsViaCities, logs, retrieves, ships.values(), taxInfos);
    }

    public static void main(String[] args) {
        //DatabaseMsg.setStream(null);
        Logger.setStream(System.out);
        IDataOperator sqlOperator = new SQLDataOperator();
        sqlOperator.initialize();
        IDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();

        System.out.println("Loading data...");
        TableInfo info = importData();
        System.out.println("Successfully import data into memory");

        System.out.println("Start to insert data into database via sql");
        long sqlStart = System.currentTimeMillis();
        sqlOperator.importData(info);
        System.out.println("Insert data into database via sql finished，time cost: "
                + (System.currentTimeMillis() - sqlStart) + "ms");

        long fileStart = System.currentTimeMillis();
        System.out.println("Start to insert data into database via file");
        fileOperator.importData(info);
        System.out.println("Insert data into database via file finished，time cost: "
                + (System.currentTimeMillis() - fileStart) + "ms");
    }

}
