package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

//entity
public record Container(
        @NotNull String code, //primary key
        @NotNull String type
) {
}
