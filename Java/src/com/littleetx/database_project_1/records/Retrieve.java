package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record Retrieve(
        @NotNull Item item,
        @NotNull LocalDate date,
        @NotNull Courier courier
) {

}
