package com.feemung.quoraspider.spider.entry;

import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.Question;
import com.feemung.quoraspider.entry.UserInfo;

import java.util.List;

/**
 * Created by feemung on 16/4/28.
 */
public class QuoraProfileHtmlData implements Data {
    private Task task;
    private UserInfo userInfo;
    private List<Task> urlList;
    private List<Answer> answerList;

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    @Override
    public Task getTask() {
        return task;
    }

    @Override
    public void setTask(Task task) {
        this.task = task;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public List<Task> getUrlList() {
        return urlList;
    }

    @Override
    public void setUrlList(List<Task> urlList) {
        this.urlList = urlList;
    }

    @Override
    public String toString() {
        return "QuoraProfileHtmlData{" +
                "task=" + task +
                ", userInfo=" + userInfo +
                ", urlList=" + urlList +
                ", answerList=" + answerList +
                '}';
    }
}
