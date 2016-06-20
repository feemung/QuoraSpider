package com.feemung.quoraspider.spider.Scheduler;

import com.feemung.quoraspider.spider.entry.Task;

import java.util.List;

/**
 * Created by feemung on 16/4/25.
 */
public interface Scheduler {
    CrawlerState getCrawlerState(String arg1,String arg2);
    boolean isStarted();
    void schedule(List<Task> list,Trigger trigger);
    void shutdown();
    void shutdown(boolean flag);
    void start();
    boolean unschedule(String arg1,String arg2);
}
