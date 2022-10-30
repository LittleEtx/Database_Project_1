package com.littleetx.database_project_1.execution;

import com.littleetx.database_project_1.FileDataOperator;
import com.littleetx.database_project_1.IDataOperator;
import com.littleetx.database_project_1.Logger;
import com.littleetx.database_project_1.SQLDataOperator;
import com.littleetx.database_project_1.records.Item;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class SelectData {

    public static void main(String[] args) {
        Logger.setStream(System.out);
        IDataOperator sqlOperator = new SQLDataOperator();
        sqlOperator.initialize();
        System.out.println("Select unfinished items from SQL...");
        Collection<Item> list1 = getUnfinishedItems(
                sqlOperator, "delete if find", IDataOperator.FindType.DeleteIfFind);
        Collection<Item> list2 = getUnfinishedItems(
                sqlOperator, "query and delete", IDataOperator.FindType.QueryAndDelete);

        IDataOperator fileOperator = new FileDataOperator();
        fileOperator.initialize();
        System.out.println("Select unfinished items from File...");
        Collection<Item> list3 = getUnfinishedItems(
                fileOperator, "query and delete", IDataOperator.FindType.QueryAndDelete);

        if (list1.size() != list2.size() || list1.size() != list3.size()) {
            throw new RuntimeException("Error: the size of the result is not equal.");
        }
    }

    @NotNull
    private static Collection<Item> getUnfinishedItems(IDataOperator dataOperator,
                                                       String type, IDataOperator.FindType findType) {
        long startTime = System.currentTimeMillis();
        System.out.println("select type: " + type);
        var list1 = dataOperator.findUnfinishedItems(findType);
        long time = System.currentTimeMillis() - startTime;
        System.out.println("Selected " + list1.size() + " items in " + time + "ms, speed: " +
                String.format("%.4f", ((double) list1.size()) / time) + " records/s");
        return list1;
    }
}
