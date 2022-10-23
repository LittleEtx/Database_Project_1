package com.littleetx.database_project_1.file_database;

import java.io.PrintStream;

public class DatabaseMsg {
    private final PrintStream stream;

    public DatabaseMsg(PrintStream stream) {
        this.stream = stream;
    }

    public void print(String msg) {
        stream.println(msg);
    }
}
