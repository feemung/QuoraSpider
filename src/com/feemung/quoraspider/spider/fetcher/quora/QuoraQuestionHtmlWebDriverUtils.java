package com.feemung.quoraspider.spider.fetcher.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.parse.RegexParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * Created by feemung on 16/5/9.
 */
public class QuoraQuestionHtmlWebDriverUtils {
    private RemoteWebDriver driver;
    private int count=0;
    private int lastSize=0;
    private long lastTime=0;
    private int loadCount=0;
    private long start=0;
    private LogFM logFM=LogFM.getInstance(QuoraQuestionHtmlWebDriverUtils.class);
    public QuoraQuestionHtmlWebDriverUtils(RemoteWebDriver driver){
        this.driver=driver;
        start=System.currentTimeMillis();
    }
    public boolean isWait(){
        WebElement sentinelEl=driver.findElement(By.className("pager_sentinel"));
        String sentinelId=sentinelEl.getAttribute("id");
        String moreId=sentinelId.replace("sentinel", "more");
        String loadId=sentinelId.replace("sentinel","loading");

        WebElement moreEle=driver.findElement(By.id(moreId));
        WebElement loadingEle=driver.findElement(By.id(loadId));

        if(moreEle.isDisplayed()){
            moreEle.click();
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logFM.e("出现更多按钮");
        }

        WebElement countElement=driver.findElement(By.className("answer_count"));
        count=Integer.valueOf( RegexParser.parse(countElement.getText(), "[0-9]+"));
        List<WebElement> pagedListElement=driver.findElements(By.className("pagedlist_item"));
        if(count==0){
            return false;
        }

        if(count>pagedListElement.size()){

            if(pagedListElement.size()>lastSize){
                logFM.d("jiazai",loadCount);
                logFM.d("answer count is ",pagedListElement.size());
                loadCount++;
                lastTime=System.currentTimeMillis();
            }
            lastSize=pagedListElement.size();
            return true;
        }else if(count<100) {
            if (pagedListElement.size() >= count) {
                return false;
            }
        }else{
            logFM.d("qingkuang 3=");
            logFM.d("jiazai",loadCount);
            logFM.d("answer count is ",pagedListElement.size());
            if((System.currentTimeMillis()-lastTime)>15000){
                logFM.d("加载了",loadCount);
                if(lastSize<pagedListElement.size()){
                    lastSize=pagedListElement.size();
                    lastTime=System.currentTimeMillis();
                    loadCount++;
                    return true;
                }else {
                    if(loadingEle.isDisplayed()){
                        logFM.e("未能全部加载数据，请检查网络");
                    }
                    return false;
                }
            }

        }
        return true;
    }
    public void waitLoad(){
        int c=0;
        for(int i=0;i<100;i++){
            logFM.d("run i=", i);
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.executeScript("javascript:window.scroll( 0,document.body.scrollHeight);");

            if(!isWait()){
                break;
            }
            List<WebElement> pagedListElement=driver.findElements(By.className("pagedlist_item"));

            logFM.d("answer count is ", pagedListElement.size());

        }
    }




}