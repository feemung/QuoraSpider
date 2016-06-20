package com.feemung.quoraspider.spider;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.entry.*;
import com.feemung.quoraspider.spider.queue.MemoryQueue;
import com.feemung.quoraspider.spider.queue.TaskQueue;
import com.feemung.quoraspider.spider.worker.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by feemung on 16/4/24.
 */
public class DefaultMaskMaster implements TaskMaster {
    private static DefaultMaskMaster instance;

    public static DefaultMaskMaster getInstance() {
        if(instance==null){
            instance=new DefaultMaskMaster();
        }
        return instance;
    }

    private LogFM logFM=LogFM.getInstance(DefaultMaskMaster.class);
    private WorkerThreadPool pool=new WorkerThreadPool();
    private DefaultWorkerProcessor processor=DefaultWorkerProcessor.getInstance();
    private TaskQueue taskQueue= MemoryQueue.getInstance();
    private TaskController taskController=null;
    public DefaultMaskMaster(){
        logFM.stopFlag=true;
    }
    public void start(){
        taskController=new TaskController(this);
        Thread taskControllerThread=new Thread(taskController);
        taskControllerThread.start();

    }
    @Override
    public void executeTasks(List<Task> taskList) {
        //logFM.d("executeTasks");
        Iterator<Task> taskIterator=taskList.iterator();

        while(taskIterator.hasNext()){
            Worker worker=null;
            Task task=taskIterator.next();
            if(task instanceof HTMLTask){
                worker=new HTMLWorker();
            }else if(task instanceof DBTask){
                worker=new DBWorker();
            }else {
                logFM.e(new Exception("不知道任务类型"));
                continue;
            }

            worker.execute(task);
            pool.execute(worker);
        }
    }

    public void executeTasks(Task task) {
        //logFM.d("executeTasks");
            Worker worker=null;
            if(task instanceof HTMLTask){
                worker=new HTMLWorker();
            }else if(task instanceof DBTask){
                worker=new DBWorker();
            }else {
                logFM.e(new Exception("不知道任务类型"));
                //continue;
            }

            worker.execute(task);
            pool.execute(worker);
    }
    public boolean executeAddTask(){
        logFM.d("addTask");
        List<Task> list=new ArrayList<>();
        for(int i=0;i<pool.getTaskQueueMaxSize()-pool.getTaskQueueSize()-1;i++){
            Task task=taskQueue.poll();
            //logFM.d("====",task);
            if(task==null){
                break;
            }else {
                //list.add(task);
                executeTasks(task);
            }
        }
       // executeTasks(list);
        return true;
    }
    @Override
    public ExecutionState getExecutionState() {
        return null;
    }
}
