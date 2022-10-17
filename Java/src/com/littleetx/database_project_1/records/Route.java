package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

public record Route(
        @NotNull City retrievalCity,
        @NotNull City exportCity,
        @NotNull City importCity,
        @NotNull City deliveryCity
) {
}
