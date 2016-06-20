package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.sql.VisitedTableSQLUtils;

import java.util.Collection;

/**
 * Created by feemung on 16/4/24.
 */
public class SQLVisitedTable implements VisitedTable {
    private static SQLVisitedTable instance;

    public static SQLVisitedTable getInstance() {
        if(instance==null){
            instance=new SQLVisitedTable();
        }
        return instance;
    }
    private VisitedTableSQLUtils sqlUtils=VisitedTableSQLUtils.getInstance();
    private SQLVisitedTable(){
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
