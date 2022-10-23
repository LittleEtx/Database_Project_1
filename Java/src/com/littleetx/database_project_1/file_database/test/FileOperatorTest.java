package com.littleetx.database_project_1.file_database.test;

import com.littleetx.database_project_1.file_database.FileOperatorReader;
import com.littleetx.database_project_1.file_database.FileOperator_CSV;
import com.littleetx.database_project_1.file_database.FileOperator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileOperatorTest {
    public static void main(String[] args) {
        FileOperator oldFile = new FileOperator_CSV("data.csv");
        FileOperator newFile = new FileOperator_CSV("test.csv");
        try (FileOperatorReader reader = oldFile.getReader()) {
            Iterator<String[]> iterator = reader.iterator();
            iterator.next();
            List<String[]> rows = new ArrayList<>();
            for (int i = 0; i< 5; i++) {
                rows.add(iterator.next());
            }
            newFile.insertRows(rows);
            newFile.modifyRowsTo(3, "石家庄", 3, "Shijiazhuang");
            for (String[] foundRows : newFile.findRowsSatisfied(24, "Schwarz Group"))
                System.out.println(foundRows[0]);
            newFile.deleteRows(new int[]{0, 1}, new String[]{"banana-99bb9", "banana"});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
