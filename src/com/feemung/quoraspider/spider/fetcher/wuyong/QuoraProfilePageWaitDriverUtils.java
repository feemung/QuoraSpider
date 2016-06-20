package com.feemung.quoraspider.spider.fetcher.wuyong;

/**
 * Created by feemung on 16/5/9.
 */

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.fetcher.quora.WaitDriver;
import com.feemung.quoraspider.spider.parse.RegexParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

public class QuoraProfilePageWaitDriverUtils implements WaitDriver {

    private RemoteWebDriver driver;
    private int count=0;
    private int lastSize=0;
    private long lastTime=0;
    private int loadCount=0;
    private long seeTime=0;
    private int update=0;
    private long start=0;
    private LogFM logFM=LogFM.getInstance(QuoraProfilePageWaitDriverUtils.class);
    public QuoraProfilePageWaitDriverUtils(RemoteWebDriver driver){
        logFM.stopFlag=false;
        this.driver=driver;
        start=System.currentTimeMillis();
        seeTime=System.currentTimeMillis();
    }
    public boolean isWait(){


        WebElement countElement=null;
        if(driver.getCurrentUrl().contains("following")){
            countElement=driver.findElement(By.className("active"));
        }else {

            countElement=driver.findElement(By.className("list_header"));
        }
        count=Integer.valueOf(RegexParser.parse
                (RegexParser.parse(countElement.getText(), "[0-9,]+\\s").replace(",", ""), "[0-9]+"));
        List<WebElement> pagedListElement=driver.findElements(By.className("pagedlist_item"));
        if(count==0){
            return false;
        }
        update=update+1;
        if((System.currentTimeMillis()-seeTime)>10000){
            logFM.i("===第",update,"次加载了",loadCount,"次和",pagedListElement.size(),"个内容（内容总共有",count,"个）");
            //logFM.i("加载了", pagedListElement.size(), "个内容");

            seeTime=System.currentTimeMillis();

        }
        if(pagedListElement.size()>=count||pagedListElement.size()>1300){
            return false;
        }
        if(pagedListElement.size()>lastSize){
            //logFM.i(driver.getCurrentUrl()," 第",update,"次加载了",loadCount,"次和",pagedListElement.size(),"个内容（内容总共有",count,"个）");

            loadCount++;
            lastTime=System.currentTimeMillis();


            lastSize=pagedListElement.size();
            /*
            driver.executeScript("javascript:window.scroll( 0,0);");


            List<WebElement> moreList=driver.findElementsByXPath("//a[@class='more_link']");
            Iterator<WebElement> iterator=moreList.iterator();
            while (iterator.hasNext()){

                WebElement key=iterator.next();
                if(key.isDisplayed()){
                    key.click();
                }
                logFM.d("点击了更多按钮");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            */
        }

        logFM.i(driver.getTitle()," 第",update,"次加载了",loadCount,"次和",pagedListElement.size(),"个内容（内容总共有",count,"个）");

        if((System.currentTimeMillis()-lastTime)>22000){
            logFM.i("加载了",loadCount,"次和",pagedListElement.size(),"个内容");
            if(lastSize<pagedListElement.size()){
                lastSize=pagedListElement.size();
                lastTime=System.currentTimeMillis();
                loadCount++;
            }else {
                logFM.e("网络出错，没有加载成功");
                return false;
            }
        }
        if(count>pagedListElement.size()){

            return true;
        }


        return false;
    }
    public void executeThing(){
        WebElement head=driver.findElementByClassName("header");
        WebElement followersCountWE=head.findElement(By.className("count"));
        if(followersCountWE!=null&&followersCountWE.getText().contains("k")) {
            logFM.d(followersCountWE.getText());
            WebElement followBut = head.findElement(By.className("primary_item"));
            if (!followBut.getText().contains("Unfollow")) {
                logFM.d(followBut.getText());
                followBut.click();
            }
        }
    }

    public void waitLoad(){
            //driver.executeScript("javascript:window.scroll( 0,document.body.scrollHeight);");



            List<WebElement> pagedListElement=driver.findElements(By.className("pagedlist_item"));

            logFM.d("answer count is ", pagedListElement.size());


        /*
        List<WebElement> moreList=driver.findElementsByXPath("//a[@class='more_link']");
        int moreI=0;
        while(!moreList.isEmpty()){
            if(moreI>5){
                logFM.e("等待网页展开失败，请检查网络");
                break;
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            moreI++;
        }
        */
    }


}