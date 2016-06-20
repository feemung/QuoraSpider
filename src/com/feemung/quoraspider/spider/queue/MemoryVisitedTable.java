package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.spider.entry.Task;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by feemung on 16/4/24.
 */
public class MemoryVisitedTable implements VisitedTable{
//public class MemoryVisitedTable implements VisitedTable{
    private static MemoryVisitedTable intance;
    public static MemoryVisitedTable getInstance() {
        if(intance==null){
            intance=new MemoryVisitedTable();
        }
        return intance;
    }
    private Map<String,Date> taskMap=new TreeMap<>();//date为访问时间
    private MemoryVisitedTable(){}
    @Override
    public void add(Task task) {
        taskMap.put(task.getURL(),new Date());
    }

    @Override
    public void destroy() {
        taskMap.clear();
    }

    @Override
    public Collection<? extends Task> getAll() {

        //return taskMap.keySet();
        return null;
    }

    @Override
    public boolean isVisited(Task task) {
        return taskMap.containsKey(task.getURL());
    }

    @Override
    public boolean remove(Task task) {

         taskMap.remove(task.getURL());
        return true;
    }
}
