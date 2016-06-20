package com.feemung.quoraspider.spider.entry;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by feemung on 16/4/24.
 */
public class HTMLTask extends AbstractTask {
    public HTMLTask(){
        startDate=new Date();
    }
    @Override
    public String toString() {
        return "HtmlTask{" +
                "delayCount=" + delayCount +
                ", url='" + url + '\'' +
                ", id='" + id + '\'' +
                ", startDate=" + startDate +
                ", delayTime=" + delayTime +
                ", host='" + host + '\'' +
                ", priority=" + priority +
                ", previousUrl='" + previousUrl + '\'' +
                ", saveDate='" + saveDate + '\'' +
                '}';
    }

}
