package com.feemung.quoraspider.spider.entry;

/**
 * Created by feemung on 16/4/24.
 */
public interface Content {
    String getCharset();
    Object obtainContent();

    Task getTask();

    void setTask(Task task);

}
