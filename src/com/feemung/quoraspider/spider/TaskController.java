package com.feemung.quoraspider.spider;

import com.feemung.quoraspider.spider.fetcher.ChromeDriverFetcher;

/**
 * Created by feemung on 16/4/25.
 */
public class TaskController implements Runnable{
    private DelayTaskController delayTaskController;
    private Thread delayTaskControllerThread;
    private DefaultMaskMaster defaultMaskMaster;
    public TaskController(DefaultMaskMaster defaultMaskMaster){
        this.defaultMaskMaster=defaultMaskMaster;
        init();
    }
    private void init(){
        delayTaskController=new DelayTaskController();
        delayTaskControllerThread=new Thread(delayTaskController,DelayTaskController.class.getSimpleName()+"-thread");
    }
    @Override
    public void run() {
        start();
        for(int i=0;i<5033;i++){
            defaultMaskMaster.executeAddTask();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //ChromeDriverFetcher.stopServer();
        System.exit(-1);
    }
    public void stop(){
        delayTaskController.stopFlag=true;
        delayTaskControllerThread.interrupt();
    }
    public void start(){
        delayTaskController.stopFlag=false;
        delayTaskControllerThread.start();
    }
}
