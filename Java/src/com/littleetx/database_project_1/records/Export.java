package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

//relationship
public record Export(
        int export_id,
        @NotNull LocalDate date,
        @NotNull Container container,
        @NotNull Ship Ship
) {
}
