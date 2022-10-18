package com.littleetx.database_project_1.file_database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FileOperator {
    protected final String filePath;

    FileOperator(@NotNull String fileName) {
        this.filePath = fileName;
    }

    abstract protected boolean hasNextRow(BufferedReader reader);


    abstract protected String[] getNextRow(BufferedReader reader);

    abstract protected String rowToStr(String[] values);

    public FileOperatorReader getReader() throws FileNotFoundException {
        return new FileOperatorReader() {
            private final BufferedReader reader = new BufferedReader(new FileReader(filePath));


            @NotNull
            @Override
            public Iterator<String[]> iterator () {
                return new Iterator<>() {

                    @Override
                    public boolean hasNext() {
                        return hasNextRow(reader);
                    }

                    @Override
                    public String[] next() {
                        return getNextRow(reader);
                    }
                };
            }

            @Override
            public void close() throws IOException {
                reader.close();
            }
        };
    }


    public void modifyRowsTo(int columnIndices, @Nullable String value, @Nullable String newValue) {
        modifyRowsTo(new int[]{columnIndices}, new String[]{value}, new String[]{newValue});
    }
    public void modifyRowsTo(int @NotNull [] columnIndices, String @NotNull [] values,
                             String @NotNull [] newValues) {
        if (columnIndices.length == 0 || columnIndices.length != values.length ||
                columnIndices.length != newValues.length) {
            throw new IllegalArgumentException(
                    "columnIndices, values and newValues must have same length");
        }

        int maxIndex = getMaxIndex(columnIndices);

        try (FileWriter writer = new FileWriter(filePath + ".tmp")) {
            try(FileOperatorReader reader = getReader()) {
                Iterator<String[]> it = reader.iterator();
                while (it.hasNext()) {
                    String[] row = it.next();
                    if (row.length <= maxIndex) {
                        throw new IllegalArgumentException("column index" + maxIndex + "out of table range!");
                    }

                    //modify satisfied row to new value
                    boolean isSatisfied = isRowSatisfies(row, values, columnIndices);
                    if (isSatisfied) {
                        for (int i = 0; i < columnIndices.length; i++) {
                            row[columnIndices[i]] = newValues[i];
                        }
                    }
                    writer.write(rowToStr(row));
                    if (it.hasNext()) {
                        writer.write(System.getProperty("line.separator"));
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Fail to read: " + filePath, e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to write to " + filePath + ".tmp", e);
        }
        renameTempFile();
    }

    public void deleteRows(int columnIndices, @Nullable String value) {
        deleteRows(new int[]{columnIndices}, new String[]{value});
    }

    public void deleteRows(int @NotNull [] columnIndices, String @NotNull [] values) {
        if (columnIndices.length == 0 || columnIndices.length != values.length) {
            throw new IllegalArgumentException(
                    "columnIndices and values must have same length");
        }

        int maxIndex = getMaxIndex(columnIndices);

        try (FileWriter writer = new FileWriter(filePath + ".tmp")) {
            try(FileOperatorReader reader = getReader()) {
                boolean hasWrittenFirstLine = false;
                for (String[] row : reader) {
                    if (row.length <= maxIndex) {
                        throw new IllegalArgumentException("column index" + maxIndex + "out of table range!");
                    }

                    if (!isRowSatisfies(row, values, columnIndices)) {
                        if (!hasWrittenFirstLine) {
                            hasWrittenFirstLine = true;
                        } else {
                            writer.write(System.getProperty("line.separator"));
                        }
                        writer.write(rowToStr(row));
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Fail to read: " + filePath, e);
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to write to " + filePath + ".tmp", e);
        }
        renameTempFile();
    }

    public List<String[]> findRowsSatisfied(int columnIndices, @Nullable String value) {
        return findRowsSatisfied(new int[]{columnIndices}, new String[]{value});
    }

    public List<String[]> findRowsSatisfied(int @NotNull [] columnIndices, String @NotNull [] values) {
        if (columnIndices.length == 0 || columnIndices.length != values.length) {
            throw new IllegalArgumentException(
                    "columnIndices and values must have same length");
        }

        int maxIndex = getMaxIndex(columnIndices);
        List<String[]> result = new ArrayList<>();
        try(FileOperatorReader reader = getReader()) {
            for (String[] row : reader) {
                if (row.length <= maxIndex) {
                    throw new IllegalArgumentException("column index " + maxIndex + "out of table range!");
                }
                if (isRowSatisfies(row, values, columnIndices)) {
                    result.add(row);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to read: " + filePath, e);
        }
        return result;
    }

    public void insertRow(String @NotNull [] row) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(System.getProperty("line.separator") + rowToStr(row));
        } catch (IOException e) {
            throw new RuntimeException("Fail to write to " + filePath, e);
        }
    }

    public void insertRows(@NotNull List<String @NotNull []> rows) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            for (int i = 0; i < rows.size(); i++) {
                if (i != 0) {
                    writer.write(System.getProperty("line.separator"));
                }
                writer.write(rowToStr(rows.get(i)));
            }
        } catch (IOException e) {
            throw new RuntimeException("Fail to write to " + filePath, e);
        }
    }

    //delete old file and rename new file
    private void renameTempFile() {
        File oldFile = new File(filePath);
        File newFile = new File(filePath + ".tmp");
        if (!oldFile.delete()) {
            throw new RuntimeException("Cannot delete old file: " + filePath);
        }
        if (!newFile.renameTo(oldFile)) {
            throw new RuntimeException("Cannot rename new file: " + filePath + ".tmp");
        }
    }
    private boolean isRowSatisfies(String[] row, String @NotNull [] values, int @NotNull [] columnIndices) {
        boolean isSatisfied = true;
        for (int i = 0; i < columnIndices.length; i++) {
            String value = row[columnIndices[i]];
            if (value == null) {
                if (values[i] != null) {
                    isSatisfied = false;
                    break;
                }
            } else if (!value.equals(values[i])) {
                isSatisfied = false;
                break;
            }
        }
        return isSatisfied;
    }

    private int getMaxIndex(int[] columnIndices) {
        int maxIndex = 0;
        for (int column : columnIndices) {
            if (column > maxIndex) {
                maxIndex = column;
            }
        }
        return maxIndex;
    }
}
