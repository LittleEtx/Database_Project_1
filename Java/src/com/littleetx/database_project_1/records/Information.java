package com.littleetx.database_project_1.records;

import java.util.Collection;

public record Information(
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
        Collection<Retrieval> retrievals,
        Collection<Ship> ships,
        Collection<TaxInfo> taxInfos
) {
}
