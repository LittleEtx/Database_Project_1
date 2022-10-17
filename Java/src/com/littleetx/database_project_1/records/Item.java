package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

public record Item(
        @NotNull String name,
        @NotNull String type,
        int price
) {
}
