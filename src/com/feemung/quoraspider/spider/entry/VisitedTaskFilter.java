package com.feemung.quoraspider.spider.entry;

import com.feemung.quoraspider.spider.queue.VisitedTable;

/**
 * Created by feemung on 16/4/24.
 */
public class VisitedTaskFilter implements TaskFilter {
    private VisitedTable visitedTable;
    @Override
    public boolean isFiltered(Task task) {
        return visitedTable.isVisited(task);
    }
}
