package com.littleetx.database_project_1;

public record LoginInfo(
	int port,
	String host,
	String databaseName,
	String username,
	String password
	) {
}
