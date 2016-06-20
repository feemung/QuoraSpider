package com.feemung.quoraspider.spider.worker;

import java.util.concurrent.*;

/**
 * Created by feemung on 16/4/24.
 */
public class WorkerThreadPool extends ThreadPoolExecutor{
    private int taskQueueMaxSize=20;
    public WorkerThreadPool(){
        super(4,6,50,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(20),new TaskMasterRejectedExecutionHandler());

    }
    public void execute(WorkerRunnable runnable){

        super.execute(runnable);

    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        Worker worker=(Worker)r;
        WorkerProcessor workerProcessor=DefaultWorkerProcessor.getInstance();
        workerProcessor.afterExecute(worker.getTask(), worker);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        Worker worker=(Worker)r;
        WorkerProcessor workerProcessor=DefaultWorkerProcessor.getInstance();
        workerProcessor.beforeExecute(worker.getTask(), worker);
    }
    public int getTaskQueueSize(){
        return this.getQueue().size();
    }

    public int getTaskQueueMaxSize() {
        return taskQueueMaxSize;
    }
}
