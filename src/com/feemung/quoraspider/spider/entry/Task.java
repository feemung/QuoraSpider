package com.feemung.quoraspider.spider.entry;

import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Delayed;

/**
 * Created by feemung on 16/4/24.
 */
public interface Task extends Delayed {
    String getHost();
    void setHost(String host);
    void setURL(String url);
    String getURL();
    String getId();
    Date getStartDate();
    void setStartDate(Date date);
    int getDelayCount();
    void setDelayCount(int delayCount);
    void setPriority(int priority);
    int getPriority();
    String getPreviousUrl();
    void setPreviousUrl(String url);
    void setSaveDate(String date);
    String getSaveDate();

}
