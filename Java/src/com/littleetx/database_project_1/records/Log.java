package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

//weak entity
public record Log(
        @NotNull Item item,
        @NotNull LocalDateTime logTime
) {
}
