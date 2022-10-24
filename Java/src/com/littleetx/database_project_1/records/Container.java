package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

public record Container(
        @NotNull String code, @NotNull String type
) {
}
