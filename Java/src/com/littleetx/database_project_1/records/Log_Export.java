package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

public record Log_Export(
        @NotNull LocalDate date,
        @NotNull Container container,
        @NotNull Ship Ship
) {
}
