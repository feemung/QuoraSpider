package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.spider.entry.Task;

import java.util.Collection;

/**
 * Created by feemung on 16/4/24.
 */
public interface VisitedTable {
    void add(Task task);
    void destroy();
    Collection<?extends Task> getAll();
    boolean isVisited(Task task);

    boolean remove(Task task);
}
