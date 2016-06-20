package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.sql.NotVisitedTableSQLUtils;

import java.util.Collection;

/**
 * Created by feemung on 16/4/24.
 */
public class SQLNotVisitedTable implements VisitedTable {
    private static SQLNotVisitedTable instance;

    public static SQLNotVisitedTable getInstance() {
        if(instance==null){
            instance=new SQLNotVisitedTable();
        }
        return instance;
    }
    private NotVisitedTableSQLUtils sqlUtils=NotVisitedTableSQLUtils.getInstance();
    private SQLNotVisitedTable(){
        try {
            sqlUtils.connectionMysql();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void add(Task task) {
        sqlUtils.addTask(task);
    }

    @Override
    public void destroy() {
        sqlUtils.deleteTable();
    }

    @Override
    public Collection<? extends Task> getAll() {
        return null;
    }

    @Override
    public boolean isVisited(Task task) {
        return sqlUtils.contain(task);
    }

    @Override
    public boolean remove(Task task) {
        return sqlUtils.deleteUrl(task.getURL());
    }
}
