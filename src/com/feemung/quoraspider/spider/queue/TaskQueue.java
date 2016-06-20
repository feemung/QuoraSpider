package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/24.
 */
public interface TaskQueue {
    boolean add(Task task) ;
    int getSize();
    Task getTask(String name);
    boolean hasNext();
    Task next();
    Task peek();
    Task poll();
}
