package com.feemung.quoraspider.spider.worker;

import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/24.
 */
public interface WorkerProcessor {
    void afterExecute(Task task,Worker worker);
    void beforeExecute(Task task,Worker worker);
}
