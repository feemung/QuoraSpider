package com.feemung.quoraspider.spider.queue;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.spider.entry.TaskComparator;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/24.
 */
public class MemoryQueue implements TaskQueue{
    private static MemoryQueue instance;
    public static MemoryQueue getInstance() {
        if(instance==null){
            instance=new MemoryQueue();
        }
        return instance;
    }


    private MemoryQueue(){

        logFM.stopFlag=true;
    }
    private boolean isPriority=false;
    private LogFM logFM=LogFM.getInstance(MemoryQueue.class);
    private Queue<Task> queue=new LinkedBlockingQueue<>();
    private Lock lock=new ReentrantLock();

    @Override
    public boolean add(Task task) {
        lock.lock();
        logFM.d("add "+task);
         queue.add(task);
        lock.unlock();
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
        return !queue.isEmpty();
    }

    @Override
    public Task next() {
        return queue.poll();
    }

    @Override
    public Task peek() {
        return queue.peek();
    }

    @Override
    public Task poll() {
        return queue.poll();
    }

    public boolean isPriority() {
        return isPriority;
    }

    public void startPriority() {
        queue=new PriorityBlockingQueue<>(24,new TaskComparator());
        isPriority=true;
    }
    public void closePriority(){
        queue=new LinkedBlockingQueue();
        isPriority=false;
    }
    public boolean contains(Task task){
        return queue.contains(task);
    }
}
