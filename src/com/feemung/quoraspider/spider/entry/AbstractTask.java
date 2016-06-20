package com.feemung.quoraspider.spider.entry;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Created by feemung on 16/4/25.
 */
public abstract class AbstractTask implements Task{
    protected int delayCount;
    protected String url;
    protected String id;
    protected Date startDate;
    protected long delayTime=5000;
    protected String host;
    protected int priority=0;
    protected String previousUrl;
    protected String saveDate;
    @Override
    public int getDelayCount() {
        return delayCount;
    }

    @Override
    public void setURL(String url) {
        this.url=url;
    }

    @Override
    public String getURL() {
        return this.url;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date date) {
        this.startDate=date;
    }

    @Override
    public void setDelayCount(int delayCount) {
        this.delayCount=delayCount;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long r=unit.convert(startDate.getTime()+delayTime-new Date().getTime(),TimeUnit.MILLISECONDS);
        return r;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host=host;
    }

    @Override
    public int compareTo(Delayed o) {
        if(this.startDate.getTime() < ((AbstractTask)o).getStartDate().getTime()) {
            return -1;
        }
        else if(this.startDate.getTime() > ((AbstractTask)o).getStartDate().getTime())return 1;
        else return 0;

    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority=priority;
    }

    @Override
    public String getPreviousUrl() {
        return previousUrl;
    }

    @Override
    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }

    @Override
    public String getSaveDate() {
        return saveDate;
    }

    @Override
    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractTask that = (AbstractTask) o;

        return url.equals(that.url);

    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return "AbstractTask{" +
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
