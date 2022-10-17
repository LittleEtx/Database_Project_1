package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

public record Container(
        @NotNull String type,
        @NotNull String code
) {
}
