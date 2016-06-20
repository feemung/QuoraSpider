package com.feemung.quoraspider.spider.entry;

import com.feemung.quoraspider.entry.Answer;

import java.util.List;

/**
 * Created by feemung on 16/4/24.
 */
public interface Data {
    public Task getTask();

    public void setTask(Task task);


    public List<Task> getUrlList();

    public void setUrlList(List<Task> urlList);

}
