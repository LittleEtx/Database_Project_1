package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

//relationship
public record Delivery(
        @NotNull Item item,
        @NotNull LocalDate deliveryDate,
        @NotNull Courier courier
) {
}
