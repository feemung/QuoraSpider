package com.feemung.quoraspider.spider.entry;

import com.feemung.quoraspider.entry.Answer;

import java.util.List;

/**
 * Created by feemung on 16/4/28.
 */
public class DefaultHtmlData implements Data {
    private Task task;
    private List<Task> urlList;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }


    public List<Task> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<Task> urlList) {
        this.urlList = urlList;
    }

    @Override
    public String toString() {
        return "DefaultHtmlData{" +
                "task=" + task +
                ", urlList=" + urlList +
                '}';
    }
}
