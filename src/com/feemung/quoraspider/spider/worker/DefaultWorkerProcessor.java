package com.feemung.quoraspider.spider.worker;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/25.
 */
public class DefaultWorkerProcessor implements WorkerProcessor {
    private static DefaultWorkerProcessor instance;
    public static DefaultWorkerProcessor getInstance() {
        if(instance==null){
            instance=new DefaultWorkerProcessor();

        }
        return instance;
    }


    private LogFM logFM=LogFM.getInstance(DefaultWorkerProcessor.class);
    private int taskFinishCount=0;
    private DefaultWorkerProcessor(){logFM.stopFlag=true;}

    public int getTaskFinishCount() {
        return taskFinishCount;
    }

    public void setTaskFinishCount(int taskFinishCount) {
        this.taskFinishCount = taskFinishCount;
    }

    @Override
    public void afterExecute(Task task, Worker worker) {
        taskFinishCount++;
        logFM.d("线程结束，已经完成任务,完成第",taskFinishCount,"个任务，该任务为",task.toString());
    }

    @Override
    public void beforeExecute(Task task, Worker worker) {
        logFM.d("线程启动，开始启动任务",task.toString());
    }

}
