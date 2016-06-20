package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.spider.entry.Task;

import java.util.Date;
import java.util.concurrent.DelayQueue;

/**
 * Created by feemung on 16/4/25.
 */
public class DelayTaskQueue implements TaskQueue {
    private static DelayTaskQueue instance;

    public static DelayTaskQueue getInstance() {
        if(instance==null){
            instance=new DelayTaskQueue();
        }
        return instance;
    }
    private FailedTaskQueue failedTaskQueue=FailedTaskQueue.getInstance();
    private DelayQueue<Task> queue=new DelayQueue<>();
    private DelayTaskQueue(){}
    @Override
    public boolean add(Task task) {
        if(task.getDelayCount()>1){
            failedTaskQueue.add(task);
        }else {
            task.setStartDate(new Date());
            task.setDelayCount(task.getDelayCount() + 1);
            queue.add(task);
        }
        return true;
    }

    @Override
    public int getSize() {
        return queue.size();
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
        return queue.poll();
    }
    public Task take()throws Exception{
        return queue.take();
    }
}
