package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public record Log(
        @NotNull Item item,
        @NotNull Route route,
        @NotNull TaxInfo taxInfo,
        @NotNull Log_Retrieval log_retrieval,
        @Nullable Log_Export log_export,
        @Nullable Log_Import log_import,
        @Nullable Log_Delivery log_delivery,
        @NotNull LocalDateTime logTime
) {
}
