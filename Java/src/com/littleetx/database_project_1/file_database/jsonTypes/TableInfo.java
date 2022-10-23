package com.littleetx.database_project_1.file_database.jsonTypes;

import java.util.List;

public record TableInfo(
	List<ColumnInfo> columns,
	String name
) {
}