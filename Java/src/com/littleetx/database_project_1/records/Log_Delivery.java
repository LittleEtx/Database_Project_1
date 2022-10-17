package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record Log_Delivery(
        @NotNull LocalDate deliveryDate,
        @NotNull Courier courier
) {
}
