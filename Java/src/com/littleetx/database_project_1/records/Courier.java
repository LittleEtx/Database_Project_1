package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

public record Courier(
        @NotNull String name,
        @NotNull String gender,
        @NotNull String phoneNumber,
        int age,
        @NotNull Company company
) {
}
