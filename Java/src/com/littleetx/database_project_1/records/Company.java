package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

//entity
public record Company(
        int id,
        @NotNull String name
) {
}
