package com.feemung.quoraspider.spider.parse;

import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.spider.entry.TaskFilter;

/**
 * Created by feemung on 16/4/24.
 */
public class URLRegexFilter implements TaskFilter {
    @Override
    public boolean isFiltered(Task task) {
        return false;
    }
}
