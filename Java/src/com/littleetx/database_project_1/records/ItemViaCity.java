package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

public record ItemViaCity(
        @NotNull Item item,
        @NotNull City retrievalCity,
        @NotNull City exportCity,
        @NotNull City importCity,
        @NotNull City deliveryCity
) {
}
