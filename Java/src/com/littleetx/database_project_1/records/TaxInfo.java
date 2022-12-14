package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public record TaxInfo(
        @NotNull Item item,
        @NotNull BigDecimal exportRate,
        @NotNull BigDecimal importRate
) {
}
