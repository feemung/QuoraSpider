package com.feemung.quoraspider.entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by feemung on 16/4/13.
 */
public class UserInfo {
    private String uid;
    private String realUid;
    private String nickname;
    private String sex;//sex=1代表是女的；sex=2 man；sex=0未知
    private HashMap<String,Integer> followersMap;
    private HashMap<String,Integer> followingMap;
    private HashMap<String,Integer> answerMap;
    private HashMap<String,Integer> questionMap;
    private HashMap<String,Integer> highlightsMap;

    private HashMap<String,Integer> educationMap;
    private HashMap<String,Integer> locationMap;
    private HashMap<String,Integer> employmentMap;

    private String followersCount;
    private String followingCount;
    private String answerCount;
    private String questionCount;

    private String userIcon;
    private String answerViewsLast30Days;
    private String answerViewsAllTime;
    private String saveDate;

    public UserInfo(){

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRealUid() {
        return realUid;
    }

    public void setRealUid(String realUid) {
        this.realUid = realUid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public HashMap<String, Integer> getFollowersMap() {
        return followersMap;
    }

    public void setFollowersMap(HashMap<String, Integer> followersMap) {
        this.followersMap = followersMap;
    }

    public HashMap<String, Integer> getFollowingMap() {
        return followingMap;
    }

    public void setFollowingMap(HashMap<String, Integer> followingMap) {
        this.followingMap = followingMap;
    }

    public HashMap<String, Integer> getAnswerMap() {
        return answerMap;
    }

    public void setAnswerMap(HashMap<String, Integer> answerMap) {
        this.answerMap = answerMap;
    }

    public HashMap<String, Integer> getQuestionMap() {
        return questionMap;
    }

    public void setQuestionMap(HashMap<String, Integer> questionMap) {
        this.questionMap = questionMap;
    }

    public HashMap<String, Integer> getHighlightsMap() {
        return highlightsMap;
    }

    public void setHighlightsMap(HashMap<String, Integer> highlightsMap) {
        this.highlightsMap = highlightsMap;
    }

    public HashMap<String, Integer> getEducationMap() {
        return educationMap;
    }

    public void setEducationMap(HashMap<String, Integer> educationMap) {
        this.educationMap = educationMap;
    }

    public HashMap<String, Integer> getLocationMap() {
        return locationMap;
    }

    public void setLocationMap(HashMap<String, Integer> locationMap) {
        this.locationMap = locationMap;
    }

    public HashMap<String, Integer> getEmploymentMap() {
        return employmentMap;
    }

    public void setEmploymentMap(HashMap<String, Integer> employmentMap) {
        this.employmentMap = employmentMap;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
    }

    public String getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(String answerCount) {
        this.answerCount = answerCount;
    }

    public String getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(String questionCount) {
        this.questionCount = questionCount;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getAnswerViewsLast30Days() {
        return answerViewsLast30Days;
    }

    public void setAnswerViewsLast30Days(String answerViewsLast30Days) {
        this.answerViewsLast30Days = answerViewsLast30Days;
    }

    public String getAnswerViewsAllTime() {
        return answerViewsAllTime;
    }

    public void setAnswerViewsAllTime(String answerViewsAllTime) {
        this.answerViewsAllTime = answerViewsAllTime;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        if (!uid.equals(userInfo.uid)) return false;
        if (!realUid.equals(userInfo.realUid)) return false;
        return nickname.equals(userInfo.nickname);

    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + realUid.hashCode();
        result = 31 * result + nickname.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", realUid='" + realUid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", followersMap=" + followersMap +
                ", followingMap=" + followingMap +
                ", answerMap=" + answerMap +
                ", questionMap=" + questionMap +
                ", highlightsMap=" + highlightsMap +
                ", educationMap=" + educationMap +
                ", locationMap=" + locationMap +
                ", employmentMap=" + employmentMap +
                ", followersCount='" + followersCount + '\'' +
                ", followingCount='" + followingCount + '\'' +
                ", answerCount='" + answerCount + '\'' +
                ", questionCount='" + questionCount + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", answerViewsLast30Days='" + answerViewsLast30Days + '\'' +
                ", answerViewsAllTime='" + answerViewsAllTime + '\'' +
                ", saveDate='" + saveDate + '\'' +
                '}';
    }
}
