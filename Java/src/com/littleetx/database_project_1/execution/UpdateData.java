package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.Logger;

public class UpdateData {
    public static void main(String[] args) {
        Logger.setStream(System.out);

        IDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();
        System.out.println("Updating item type from strawberry to blueberry...");
        fileOperator.updateItemType("strawberry", "blueberry");

        System.out.println("Updating item type from blueberry to strawberry...");
        fileOperator.updateItemType("strawberry", "blueberry");
    }
}
