package com.littleetx.database_project_1;

import com.littleetx.database_project_1.file_database.DatabaseMsg;
import com.littleetx.database_project_1.file_database.FileOperator;
import com.littleetx.database_project_1.file_database.FileOperatorReader;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.records.*;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportData {

    private static final String DataFile = "data.csv";

    public static List<Log> importData() {
        FileOperator fileOperator = new FileOperator_CSV(DataFile);
        List<Log> list = new ArrayList<>();
        try {
            Iterator<String[]> reader = fileOperator.getReader().iterator();
            reader.next();
            while (reader.hasNext()) {
                String[] info = reader.next();
                Item item = new Item(info[0], info[1], Integer.parseInt(info[2]));
                City retrievalCity = new City(info[3]);
                City deliveryCity = new City(info[10]);
                City exportCity = new City(info[15]);
                City importCity = new City(info[18]);
                Route route = new Route(retrievalCity, exportCity, importCity, deliveryCity);
                TaxInfo taxInfo = new TaxInfo(new BigDecimal(info[16]), new BigDecimal(info[19]));

                Company company = new Company(info[24]);

                //retrieval
                Courier courier = new Courier(info[11], info[12], info[13], Integer.parseInt(info[14]), company);
                DateTimeFormatter dateFormatter = (new DateTimeFormatterBuilder())
                        .appendValue(ChronoField.YEAR, 4)
                        .appendLiteral('/')
                        .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
                        .appendLiteral('/')
                        .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                        .toFormatter();
                Log_Retrieval retrieval = new Log_Retrieval(LocalDate.parse(info[4], dateFormatter), courier);

                //export
                Log_Export export = null;
                if (info[17] != null) {
                    Container container = new Container(info[21], info[22]);
                    Ship ship = new Ship(info[23], company);
                    export = new Log_Export(LocalDate.parse(info[17], dateFormatter), container, ship);
                }

                //import
                Log_Import imports = null;
                if (info[20] != null) {
                    imports = new Log_Import(LocalDate.parse(info[20], dateFormatter));
                }

                //delivery
                Log_Delivery delivery = null;
                if (info[9] != null) {
                    Courier deliveryCourier = new Courier(info[11], info[12], info[13],
                            Integer.parseInt(info[14]), company);
                    delivery = new Log_Delivery(LocalDate.parse(info[9], dateFormatter), deliveryCourier);
                }

                DateTimeFormatter timeFormatter = (new DateTimeFormatterBuilder())
                        .appendValue(ChronoField.YEAR, 4)
                        .appendLiteral('/')
                        .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
                        .appendLiteral('/')
                        .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
                        .appendLiteral(' ')
                        .appendValue(ChronoField.HOUR_OF_DAY, 1, 2, SignStyle.NOT_NEGATIVE)
                        .appendLiteral(':')
                        .appendValue(ChronoField.MINUTE_OF_HOUR, 1, 2, SignStyle.NOT_NEGATIVE)
                        .toFormatter();

                Log log = new Log(item, route, taxInfo, retrieval, export, imports, delivery,
                        LocalDateTime.parse(info[25], timeFormatter));
                list.add(log);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file " + DataFile, e);
        }

        return list;
    }


    public static void main(String[] args) {
        DatabaseMsg.setStream(null);

        List<Log> list = importData();
        System.out.println("Successfully import data into memory");
        System.out.println("Start to insert data into database via sql");
        long sqlStart = System.currentTimeMillis();
        IDataOperator sqlOperator = new SQLDataOperator();
        for (Log log : list) {
            sqlOperator.insert(log);
            sqlOperator.insert(log);
        }
        System.out.println("Insert data into database via sql finished，time cost: "
                + (System.currentTimeMillis() - sqlStart) + "ms");

        System.out.println("Start to insert data into database via file");
        long fileStart = System.currentTimeMillis();
        IDataOperator fileOperator = new FileDataOperator();
        for (Log log : list) {
            sqlOperator.insert(log);
            fileOperator.insert(log);
        }
        System.out.println("Insert data into database via file finished，time cost: "
                + (System.currentTimeMillis() - fileStart) + "ms");
    }

}
