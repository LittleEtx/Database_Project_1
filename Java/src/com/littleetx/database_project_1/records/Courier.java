package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

//entity
public record Courier(
        int id,
        @NotNull String name,
        @NotNull String gender,
        @NotNull String phoneNumber,
        @NotNull LocalDate birthday,
        @NotNull Company company
) {
}
