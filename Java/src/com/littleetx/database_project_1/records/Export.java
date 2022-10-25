package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

//relationship
public record Export(
        @NotNull Item item,
        @NotNull LocalDate date,
        @NotNull Container container,
        @NotNull Ship ship
) {
}
