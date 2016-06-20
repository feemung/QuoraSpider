package com.feemung.quoraspider.spider.Scheduler;

import com.feemung.quoraspider.spider.DefaultMaskMaster;
import com.feemung.quoraspider.spider.entry.Task;

import java.util.List;

/**
 * Created by feemung on 16/4/25.
 */
public class DefaultScheduler extends AbstractScheduler {
    @Override
    public CrawlerState getCrawlerState(String arg1, String arg2) {
        return null;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public void schedule(List<Task> list, Trigger trigger) {

    }

    @Override
    public void shutdown() {

    }

    @Override
    public void shutdown(boolean flag) {

    }

    @Override
    public void start() {
        DefaultMaskMaster defaultMaskMaster=new DefaultMaskMaster();
        defaultMaskMaster.start();;
    }

    @Override
    public boolean unschedule(String arg1, String arg2) {
        return false;
    }
}
