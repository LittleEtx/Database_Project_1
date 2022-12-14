package com.littleetx.database_project_1;

import com.littleetx.database_project_1.records.*;

import java.util.Collection;

public record TableInfo(
        Collection<City> cities,
        Collection<Company> companies,
        Collection<Container> containers,
        Collection<Courier> couriers,
        Collection<Delivery> deliveries,
        Collection<Export> exports,
        Collection<Import> imports,
        Collection<Item> items,
        Collection<ItemViaCity> itemsViaCities,
        Collection<Log> logs,
        Collection<Retrieve> retrieves,
        Collection<Ship> ships,
        Collection<TaxInfo> taxInfos
) {
}
