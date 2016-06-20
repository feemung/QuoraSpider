package com.feemung.quoraspider.spider.entry;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by feemung on 16/4/25.
 */
public class HTMLContent  extends WebContent{
    private List<String> urlList=new ArrayList<>();
    private String page;
    private Task task;
    public HTMLContent(){}
    public HTMLContent(Task task){
        this.task=task;
    }
    public void addUrl(String url){
        urlList.add(url);
    }
    @Override
    public String getCharset() {
        return page;
    }

    @Override
    public Object obtainContent() {
        return null;
    }
    @Override
    public Task getTask() {
        return task;
    }
    @Override
    public void setTask(Task task) {
        this.task = task;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
