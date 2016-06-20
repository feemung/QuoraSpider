package com.feemung.quoraspider.spider.entry;

import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.Question;

import java.util.List;

/**
 * Created by feemung on 16/4/28.
 */
public class QuoraQuestionHtmlData implements Data {
    private Task task;
    private List<Answer> answerList;
    private List<Task> urlList;
    private Question question;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Answer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        this.answerList = answerList;
    }

    public List<Task> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<Task> urlList) {
        this.urlList = urlList;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "QuoraQuestionHtmlData{" +
                "task=" + task +
                ", answerList=" + answerList +
                ", urlList=" + urlList +
                ", question=" + question +
                '}';
    }
}
