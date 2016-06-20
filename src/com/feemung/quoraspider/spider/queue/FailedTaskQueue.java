package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.Utils.TimeStamp;
import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.sql.FailedVisitedTableSQLUtils;

import java.util.Date;

/**
 * Created by feemung on 16/4/25.
 */
public class FailedTaskQueue implements TaskQueue {
    private static FailedTaskQueue instance;

    public static FailedTaskQueue getInstance() {
        if(instance==null){
            instance=new FailedTaskQueue();
        }
        return instance;
    }
    private FailedTaskQueue(){
        try {
            sqlUtils.connectionMysql();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private FailedVisitedTableSQLUtils sqlUtils=FailedVisitedTableSQLUtils.getInstance();
    @Override
    public boolean add(Task task) {
        task.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));
        sqlUtils.addTask(task);
        return true;
    }

    @Override
    public int getSize() {
        return sqlUtils.getAllDate().size();
    }

    @Override
    public Task getTask(String name) {
        return null;
    }
    public boolean contain(Task task){
        return sqlUtils.contain(task);
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Task next() {
        return null;
    }

    @Override
    public Task peek() {
        return null;
    }

    @Override
    public Task poll() {
        return null;
    }
}
