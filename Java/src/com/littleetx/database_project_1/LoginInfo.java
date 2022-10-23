package com.littleetx.database_project_1;

public record LoginInfo(
	String password,
	String databaseName,
	int port,
	String host,
	String username
) {
}
