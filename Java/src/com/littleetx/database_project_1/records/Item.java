package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

//entity
public record Item(
        @NotNull String id, //primary key
        @NotNull String type,
        int price
) {
}
