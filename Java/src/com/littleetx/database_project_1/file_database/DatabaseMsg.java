package com.littleetx.database_project_1.file_database;

import java.io.PrintStream;

public class DatabaseMsg {
    private static PrintStream stream;
    public static void setStream(PrintStream stream) {
        DatabaseMsg.stream = stream;
    }

    public static void print(String msg) {
        if (stream != null) {
            stream.println(msg);
        }
    }
}
