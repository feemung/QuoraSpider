package com.feemung.quoraspider.spider.worker;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.TimeStamp;
import com.feemung.quoraspider.spider.entry.*;
import com.feemung.quoraspider.spider.fetcher.WebFetcher;
import com.feemung.quoraspider.spider.exception.LoginFailedException;
import com.feemung.quoraspider.spider.handler.WareHandler;
import com.feemung.quoraspider.spider.handler.quora.HomeHandler;
import com.feemung.quoraspider.spider.handler.quora.ProfileHandler;
import com.feemung.quoraspider.spider.handler.quora.QuestionHandler;
import com.feemung.quoraspider.spider.parse.DefaultHtmlPageParser;
import com.feemung.quoraspider.spider.parse.quora.HomeHtmlParser;
import com.feemung.quoraspider.spider.parse.quora.ProfileHtmlParser;
import com.feemung.quoraspider.spider.parse.quora.QuestionHtmlParser;
import com.feemung.quoraspider.spider.queue.*;
import com.feemung.quoraspider.sql.FailedVisitedTableSQLUtils2;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by feemung on 16/4/24.
 */
public class HTMLWorker implements Worker {
    private Task task;
    private MemoryQueue memoryQueue=MemoryQueue.getInstance();
    private DelayTaskQueue delayTaskQueue=DelayTaskQueue.getInstance();
    private MemoryVisitedTable memoryVisitedTable=MemoryVisitedTable.getInstance();
    private SQLVisitedTable sqlVisitedTable=SQLVisitedTable.getInstance();
    private SQLNotVisitedTable sqlNotVisitedTable=SQLNotVisitedTable.getInstance();
    private FailedTaskQueue failedTaskQueue=FailedTaskQueue.getInstance();
    private FailedVisitedTableSQLUtils2 failedVisitedTableSQLUtils=FailedVisitedTableSQLUtils2.getInstance();
    private LogFM logFM=LogFM.getInstance(HTMLWorker.class);
    public HTMLWorker(){
        logFM.stopFlag=true;
    }
    @Override
    public void execute(Task task) {
        this.task=task;
    }

    @Override
    public void run() {
        if(memoryVisitedTable.isVisited(task)){
            logFM.e("从内存里查询得知已经访问过，任务结束"+task.toString());
            return;
        }else if(sqlVisitedTable.isVisited(task)){
            memoryVisitedTable.add(task);
            logFM.e("从数据库里查询得知已经访问过，任务结束"+task.toString());
            if(sqlNotVisitedTable.isVisited(task)){
                sqlNotVisitedTable.remove(task);
            }

            return;
       }
        WebFetcher webFetcher=new WebFetcher();
        Content htmlPage=null;
        try {
           htmlPage=webFetcher.fetch(task);
        } catch (LoginFailedException e){
            logFM.e("未成功下载该任务，因为登陆失败"+task.toString());
            System.exit(-1);
        } catch (Exception e) {
           // delayTaskQueue.add(task);
            task.setPreviousUrl("wei download2");
            failedTaskQueue.add(task);
           // sqlNotVisitedTable.remove(task);
            e.printStackTrace();
            logFM.e("未成功下载该任务"+task.toString());
            return;
        }
        if(task.getURL().matches("^https://www\\.quora\\.com/[\\w-]+$")){
            QuestionHtmlParser questionHtmlParser =new QuestionHtmlParser();
            QuoraQuestionHtmlData data=null;
            try {
                data= questionHtmlParser.parse(htmlPage,task,null);
                addUrl(data);
                WareHandler wareHandler=QuestionHandler.getInstance();
                wareHandler.handleData(data);
            }
            catch (Exception e) {
                logFM.e("出错链接" + task.toString());
                e.printStackTrace();
                return;
            }
            logFM.e("已经完成" + task.getURL());



        }else if(task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+$")||
                task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+/questions$")||
                task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+/followers$")||
                task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+/following$"))
        {
            ProfileHtmlParser profileHtmlParser =new ProfileHtmlParser();
            QuoraProfileHtmlData data=null;
            try {
                data= profileHtmlParser.parse(htmlPage,task,null);
                //addUrl(data);
                logFM.d(data);
                WareHandler wareHandler= ProfileHandler.getInstance();
                wareHandler.handleData(data);
            } catch (Exception e) {
                logFM.e("出错链接" + task.toString());
                task.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));
                //sqlNotVisitedTable.remove(task);
                task.setPreviousUrl("link wrong2");
                failedTaskQueue.add(task);
                e.printStackTrace();
                return;
            }

            logFM.e("已经完成" + task);
        }else if(task.getURL().matches("^https://www\\.quora\\.com[/]*$")){
            HomeHtmlParser parser=new HomeHtmlParser();
            QuoraHomeHtmlData data=null;
            try {
                data=parser.parse(htmlPage,task,null);
               // addUrl(data);
                logFM.d(data);
                WareHandler wareHandler= HomeHandler.getInstance();
                wareHandler.handleData(data);
            } catch (Exception e) {
                logFM.e("出错链接" + task.toString());
                task.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));
                //sqlNotVisitedTable.remove(task);
                task.setPreviousUrl("link wrong2");
                failedTaskQueue.add(task);
                e.printStackTrace();
                return;
            }
        }else {
            DefaultHtmlPageParser pageParser=new DefaultHtmlPageParser();
            DefaultHtmlData data=null;
            try {
                data = pageParser.parse(htmlPage,task,null);
                //addUrl(data);

            } catch (Exception e) {
                logFM.e("出错链接" + task.toString());
                task.setPreviousUrl("default");
                failedTaskQueue.add(task);
                e.printStackTrace();
                return;
            }
            logFM.e("已经完成" + task.getURL());


        }


        task.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));
        if(!sqlVisitedTable.isVisited(task)) {
            sqlVisitedTable.add(task);
        }
        if(!memoryVisitedTable.isVisited(task)){
            logFM.d("添加进memoryVisitedTable＝"+task.getURL());
            memoryVisitedTable.add(task);
        }

        if(sqlNotVisitedTable.isVisited(task)) {
            sqlNotVisitedTable.remove(task);
        }
        System.exit(-1);
    }
    private void addUrl(Data data){
        List<Task> list=data.getUrlList();
        Iterator<Task> iterator=list.iterator();
        while (iterator.hasNext()){
            Task task=iterator.next();
            task.setStartDate(new Date());
            //if(task.getURL().contains("https://www.quora.com/")) {
           // if(task.getURL().matches("^https://www\\.quora\\.com/[\\w-]+$")) {
            if(task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+$")) {
                String str=task.getURL()+"/following";
                task.setURL(str);
                task.setPreviousUrl(this.task.getURL());
                task.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));
                if(!memoryQueue.contains(task)
                        &&!memoryVisitedTable.isVisited(task)
                        &&!sqlVisitedTable.isVisited(task)&&!sqlNotVisitedTable.isVisited(task)
                        &&!failedTaskQueue.contain(task)) {
                    memoryQueue.add(task);
                    sqlNotVisitedTable.add(task);
                }
            }else if(task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+/following$")) {
                String str=task.getURL();
                task.setURL(str);
                task.setPreviousUrl(this.task.getURL());
                task.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));
                if(!memoryQueue.contains(task)
                        &&!memoryVisitedTable.isVisited(task)
                        &&!sqlVisitedTable.isVisited(task)&&!sqlNotVisitedTable.isVisited(task)
                        &&!failedTaskQueue.contain(task)) {
                    memoryQueue.add(task);
                    sqlNotVisitedTable.add(task);

                }
            }

        }
    }
    @Override
    public Task getTask() {
        return task;
    }
}
