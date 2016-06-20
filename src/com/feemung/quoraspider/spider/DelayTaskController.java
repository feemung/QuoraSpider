package com.feemung.quoraspider.spider;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.queue.DelayTaskQueue;
import com.feemung.quoraspider.spider.queue.MemoryQueue;
import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/25.
 */
public class DelayTaskController implements Runnable {
    private DelayTaskQueue queue=DelayTaskQueue.getInstance();
    private MemoryQueue memoryQueue=MemoryQueue.getInstance();
    private LogFM logFM=LogFM.getInstance(DelayTaskController.class);
    public boolean stopFlag=false;
    @Override
    public void run() {
        logFM.i("DelayTaskController已经启动");
        while(!stopFlag) {
            Task task= null;
            try {
                logFM.d("准备从延时队列中提取任务");
                task = queue.take();
                logFM.d("从延时队列中提取任务：",task);
                memoryQueue.add(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logFM.i("DelayTaskController已经停止");
    }
}
