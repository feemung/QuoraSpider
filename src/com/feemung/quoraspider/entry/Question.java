package com.feemung.quoraspider.entry;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by feemung on 16/4/30.
 */
public class Question implements Serializable{
    static final long serialVersionUID = 42L;
    private String realId;
    private String text;
    private List<String> topicList=new ArrayList<>();
    private String saveDate;
    public Question(){}

    public String getRealId() {
        return realId;
    }

    public void setRealId(String realId) {
        this.realId = realId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<String> topicList) {
        this.topicList = topicList;
    }

    public String getTopicListStr() {
        return new Gson().toJson(topicList);
    }

    public void setTopicList(String topicList) {
        this.topicList = new Gson().fromJson(topicList,ArrayList.class);
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    @Override
    public String toString() {
        return "Question{" +
                "realId='" + realId + '\'' +
                ", text='" + text + '\'' +
                ", topicList=" + topicList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return realId.equals(question.realId);

    }

    @Override
    public int hashCode() {
        return realId.hashCode();
    }
}
