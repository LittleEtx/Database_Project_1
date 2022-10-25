package com.littleetx.database_project_1;

import java.io.PrintStream;

public class Logger {
    private static PrintStream out;
    public static void setStream(PrintStream stream) {
        out = stream;
    }

    public static void log(String message) {
        if (out != null) {
            out.println(message);
        }
    }

}
