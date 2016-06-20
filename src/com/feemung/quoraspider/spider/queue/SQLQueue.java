package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/24.
 */
public class SQLQueue implements TaskQueue {
    @Override
    public boolean add(Task task) {
        return false;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public Task getTask(String name) {
        return null;
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
