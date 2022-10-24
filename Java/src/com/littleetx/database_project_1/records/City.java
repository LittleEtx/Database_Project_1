package com.littleetx.database_project_1.records;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

//entity
public record City(
        int id, //primary key
        @NotNull String name,
        @Nullable String areaCode
) {
}
