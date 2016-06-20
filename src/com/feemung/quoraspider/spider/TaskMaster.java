package com.feemung.quoraspider.spider;

import com.feemung.quoraspider.spider.entry.ExecutionState;
import com.feemung.quoraspider.spider.entry.Task;

import java.util.List;

/**
 * Created by feemung on 16/4/24.
 */
public interface TaskMaster {
    void executeTasks(List<Task> taskList);
    ExecutionState getExecutionState();
}
