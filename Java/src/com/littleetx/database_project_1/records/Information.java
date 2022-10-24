package com.littleetx.database_project_1.records;

import java.util.List;

public record Information(
        List<City> cities,
        List<Company> companies,
        List<Container> containers,
        List<Courier> couriers,
        List<Delivery> deliveries,
        List<Export> exports,
        List<Import> imports,
        List<Item> items,
        List<ItemViaCity> itemsViaCities,
        List<Log> logs,
        List<Retrieval> retrievals,
        List<Ship> ships,
        List<TaxInfo> taxInfos
) {
}
