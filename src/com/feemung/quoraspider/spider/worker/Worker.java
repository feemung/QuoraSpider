package com.feemung.quoraspider.spider.worker;

import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/24.
 */
public interface Worker extends WorkerRunnable{
    void execute(Task task);
}
