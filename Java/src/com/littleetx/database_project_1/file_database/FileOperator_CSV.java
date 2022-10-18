package com.littleetx.database_project_1.file_database;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FileOperator_CSV extends FileOperator {


    public FileOperator_CSV(@NotNull String fileName) {
        super(fileName);
    }

    @Override
    protected boolean hasNextRow(BufferedReader reader) {
        try {
            return reader.ready();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + filePath);
        }
    }

    @Override
    protected String[] getNextRow(BufferedReader reader) {
        if (!hasNextRow(reader)) {
            throw new IllegalStateException("No more rows");
        }
        ArrayList<String> results = new ArrayList<>();
        boolean isInQuote = false;
        StringBuilder value = new StringBuilder();
        try {
            String line = reader.readLine();
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (isInQuote) {
                    //in quote only ends with single quoteMark
                    if (c == '"') {
                        if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                            value.append('"');
                            i++;
                        } else {
                            isInQuote = false;
                            results.add(value.toString());
                            value = new StringBuilder();
                        }
                    } else {
                        value.append(c);
                    }
                }
                else { //not in quote
                    if (c == '"' && value.length() == 0) {
                        isInQuote = true;
                    } else if (c == ',') {
                        //null value
                        if (value.length() == 0) {
                            results.add(null);
                        } else {
                            results.add(value.toString());
                        }
                        value = new StringBuilder();
                    } else {
                        value.append(c);
                    }
                }

                if (i == line.length() - 1) {
                    //deal with multi-line value
                    if (isInQuote) {
                        value.append(System.getProperty("line.separator"));
                        i = -1;
                        line = reader.readLine();
                    } else {
                        //insert the last value
                        if (value.length() == 0 && line.charAt(i) != '"') {
                            results.add(null);
                        } else {
                            results.add(value.toString());
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return results.toArray(new String[0]);
    }

    @Override
    protected String rowToStr(String[] values) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            if (values[i] == null)
                continue;

            if (values[i].length() == 0) {
                sb.append("\"\"");
                continue;
            }

            boolean needQuote = true;
            if (values[i].charAt(0) == '"') {
                sb.append("\"").append(values[i]).append("\"");
            } else if (values[i].contains(",")) {
                sb.append("\"");
            }
            else {
                needQuote = false;
            }
            sb.append(values[i]);
            if (needQuote) {
                sb.append("\"");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        FileOperator fo = new FileOperator_CSV("data.csv");
        try (FileOperatorReader reader = fo.getReader()) {
            Iterator< String[]> iter = reader.iterator();
            for (String str : iter.next()) {
                System.out.print(str + ",");
            }
            System.out.println();
            for (String str : iter.next()) {
                System.out.print(str + ",");
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}



