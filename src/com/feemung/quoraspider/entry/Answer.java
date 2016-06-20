package com.feemung.quoraspider.entry;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by feemung on 16/4/23.
 */
public class Answer implements Serializable{
    private String question;//问题
    private String answerUser;//答案的作者
    private String content;//答案内容
    private String lastEditDate;//最后修改的时间 时间格式 yyyyMMddHHmmss
    private int upvotersCount;//点赞数量
    private List upvotersUserList=new ArrayList<>();// 点赞的用户列表
    private int viewCount;//浏览的次数
    private Map<String,Integer> wordMap=new TreeMap<>();//单词出现的频率的集合
    private transient int wordMum=0;
    private transient int answerSize=0;//答案的总单词数
    private String saveDate;

    public String getAnswerUser() {
        return answerUser;
    }

    public void setAnswerUser(String answerUser) {
        this.answerUser = answerUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(String lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public int getUpvotersCount() {
        return upvotersCount;
    }

    public String getUpvotersCountStr() {
        return String.valueOf(upvotersCount);
    }

    public void setUpvotersCount(int upvotersCount) {
        this.upvotersCount = upvotersCount;
    }
    public void setUpvotersCountStr(String upvotersCount) {
        if(upvotersCount.contains(".0")){
            upvotersCount=upvotersCount.replace(".0","");
        }
        this.upvotersCount = Integer.valueOf(upvotersCount);
    }

    public List getUpvotersUserList() {
        return upvotersUserList;
    }
    public String getUpvotersUserListStr() {
        return new Gson().toJson(upvotersUserList);
    }

    public void setUpvotersUserList(List upvotersUserList) {
        this.upvotersUserList = upvotersUserList;
    }

    public void setUpvotersUserList(String upvotersUserList) {

        this.upvotersUserList = new Gson().fromJson(upvotersUserList,List.class);
    }

    public int getViewCount() {
        return viewCount;
    }
    public String getViewCountStr() {
        return String.valueOf(viewCount);
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public void setViewCountStr(String viewCount) {
        if(viewCount.contains(".0")){
            viewCount=viewCount.replace(".0","");
        }
        this.viewCount = Integer.valueOf(viewCount);
    }

    public Map<String, Integer> getWordMap() {
        return wordMap;
    }

    public String getWordMapStr() {
        return new Gson().toJson(wordMap);
    }

    public void setWordMap(Map<String, Integer> wordMap) {
        this.wordMap = wordMap;
        setWordMum(wordMap.size());
    }

    public void setWordMap(String wordMap) {

        this.wordMap = new Gson().fromJson(wordMap,TreeMap.class);
    }

    public int getWordMum() {
        return wordMum;
    }

    public void setWordMum(int wordMum) {
        this.wordMum = wordMum;
    }

    public int getAnswerSize() {
        return answerSize;
    }

    public void setAnswerSize(int answerSize) {
        this.answerSize = answerSize;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (!question.equals(answer.question)) return false;
        if (!answerUser.equals(answer.answerUser)) return false;
        return lastEditDate.equals(answer.lastEditDate);

    }

    @Override
    public int hashCode() {
        int result = question.hashCode();
        result = 31 * result + answerUser.hashCode();
        result = 31 * result + lastEditDate.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "question='" + question + '\'' +
                ", answerUser='" + answerUser + '\'' +
                ", content='" + content + '\'' +
                ", lastEditDate='" + lastEditDate + '\'' +
                ", upvotersCount=" + upvotersCount +
                ", upvotersUserList=" + upvotersUserList +
                ", viewCount=" + viewCount +
                ", wordMap=" + wordMap +
                ", wordMum=" + wordMum +
                ", answerSize=" + answerSize +
                ", saveDate='" + saveDate + '\'' +
                '}';
    }
}
