package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

public record Ship(
        int id,
        @NotNull String name,
        @NotNull Company company
) {
}
