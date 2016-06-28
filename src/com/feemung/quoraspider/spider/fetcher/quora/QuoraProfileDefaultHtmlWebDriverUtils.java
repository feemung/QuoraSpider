package com.feemung.quoraspider.spider.fetcher.quora;

/**
 * Created by feemung on 16/5/9.
 */

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.parse.RegexParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuoraProfileDefaultHtmlWebDriverUtils {
    private RemoteWebDriver driver;
    private int count=0;
    private int lastSize=0;
    private long lastTime=0;
    private int loadCount=0;
    private long seeTime=0;
    private int update=0;
    private long start=0;
    private LogFM logFM=LogFM.getInstance(QuoraProfileDefaultHtmlWebDriverUtils.class);
    private ArrayList<String> idList=new ArrayList<>();
    public QuoraProfileDefaultHtmlWebDriverUtils(RemoteWebDriver driver){
        logFM.stopFlag=true;
        this.driver=driver;
        start=System.currentTimeMillis();
        seeTime=System.currentTimeMillis();
    }
    public boolean isWait(){


        WebElement countElement=null;
        try {
            if (driver.getCurrentUrl().contains("following")) {
                countElement = driver.findElement(By.className("active"));
            } else {

                countElement = driver.findElement(By.className("list_header"));
            }

        count=Integer.valueOf(RegexParser.parse
                (RegexParser.parse(countElement.getText(), "[0-9,]+\\s").replace(",", ""), "[0-9]+"));
        }catch (Exception e){
            return false;
        }
        List<WebElement> pagedListElement=driver.findElements(By.className("pagedlist_item"));
        //Iterator<WebElement> pageIter=pagedListElement.iterator();
        int loadItemCount=0;
        loadItemCount=pagedListElement.size();

        if(count==0||loadItemCount>5000){
            return false;
        }
        update++;
        if((System.currentTimeMillis()-seeTime)>30000){
            logFM.i("第",update,"次耗时"+String.valueOf((System.currentTimeMillis()-start)/1000)+"秒加载了",loadItemCount,"个内容（内容总共有",count,"个）");
            //logFM.i("加载了", pagedListElement.size(), "个内容");

            seeTime=System.currentTimeMillis();

        }

        if((System.currentTimeMillis()-lastTime)>35000){
            //logFM.i("加载了",loadCount,"次和",loadItemCount,"个内容");
            if(lastSize<loadItemCount){
                lastSize=loadItemCount;
                lastTime=System.currentTimeMillis();

            }else {
                logFM.e("网络出错，没有加载成功"+"加载了"+String.valueOf(loadItemCount)+"个内容（内容总共有"+String.valueOf(count)+"个");

                return false;
            }
        }
        if(count>loadItemCount){

            return true;
        }else {
            //loadItemCount=0;
            /*
            Iterator<WebElement> pagedIter=pagedListElement.iterator();
            while (pagedIter.hasNext()){
                WebElement key=pagedIter.next();

                if(key.isDisplayed()) {
                    loadItemCount = loadItemCount + 1;

                }
            }
            */
            if(loadItemCount<count){
                return true;
            }else {
                logFM.i("加载完成,加载了",loadItemCount,"个，总共",count,"个内容");
            }
        }


        return false;
    }
    public void waitLoad(){

        start=System.currentTimeMillis();
        seeTime=System.currentTimeMillis();
        for(int i=0;i< 2000; i++) {
            logFM.d("run i=", i);

            driver.executeScript("javascript:window.scroll( 0,document.body.scrollHeight);");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!isWait()){
                break;
            }

        }

    }


}