package com.feemung.quoraspider.spider.fetcher.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.exception.ExceptionManager;
import com.feemung.quoraspider.spider.exception.LoadFailedException;
import com.feemung.quoraspider.spider.parse.RegexParser;
import com.feemung.quoraspider.spider.utils.HtmlParserUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by feemung on 16/5/23.
 */
public class QuoraProfileAnswerHtmlWebDriverUtils {
    private  LogFM logFM=LogFM.getInstance(QuoraProfileAnswerHtmlWebDriverUtils.class);
    private RemoteWebDriver driver;
    private int upvoteCount=500;//超过该数值会点击more链接
    private boolean upvoteCountFilter=true;
    public QuoraProfileAnswerHtmlWebDriverUtils(RemoteWebDriver driver){
        this.driver=driver;
        init();
    }
    private void init(){

        try {
            WebElement countElement = driver.findElement(By.xpath("//div[@class='layout_3col_center']/div[@class='list_header']"));

            itemSum = Integer.valueOf(RegexParser.parse
                    (RegexParser.parse(countElement.getText(), "[0-9,]+\\s").replace(",", ""), "[0-9]+"));
        }catch (Exception e){

        }
    }
    public void waitRun(){
        QuoraWebDriverExecute.clickUserFollow(driver.findElementByClassName("header"),1000);
        if(upvoteCountFilter){
            upvoteCountFilterClickMore();
        }else {
            clickAllMore();
        }
    }
    public void clickAllMore(){

        boolean flag=true;
        long time=System.currentTimeMillis();
        try {

            int lastSize=0;
            int count=0;
            long t=System.currentTimeMillis();

            while (flag) {
                logFM.i("==============");
                long c=System.currentTimeMillis();
                flag =isWait();
                List<WebElement> moreEls=driver.findElementsByXPath("//div[@class='layout_3col_center']//a[@class='more_link']");
                int size=moreEls.size();
                if(size==lastSize){
                    driver.executeScript("javascript:window.scroll( 0,document.body.scrollHeight);");
                    Thread.sleep(1000);
                    logFM.i("通过滑动到低进行拉取数据");
                }

                lastSize=size;

                for(int m=count;m<size;m++){
                    c=System.currentTimeMillis();
                    WebElement  moreEl = moreEls.get(m);
                    count++;
                    logFM.i(count, "---获取moreEle耗时", System.currentTimeMillis() - c);
                    QuoraWebDriverExecute.clickMoreElement(driver, moreEl, false);
                    logFM.i("------------------");
                }
                logFM.e("共用时"+String.valueOf(System.currentTimeMillis()-t)+"毫秒");
            }

        }catch (Exception e){

            e.printStackTrace();

        }finally {

        }
        logFM.i("加载完数据，耗时", System.currentTimeMillis() - time);

            time=System.currentTimeMillis();
            List<WebElement> moreList = driver.findElementsByXPath("//a[@class='more_link']");
            ArrayList failedMoreWE = new ArrayList();
            Iterator<WebElement> iterator = moreList.iterator();
            while (iterator.hasNext()) {

                WebElement moreWE = iterator.next();
                try {
                    QuoraWebDriverExecute.clickMoreElement(driver, moreWE, false);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            logFM.i("耗时", System.currentTimeMillis() - time, "毫秒检验，点击拉取数据失败共有", failedMoreWE.size());


    }
    public void upvoteCountFilterClickMore(){

        boolean flag=true;
        long time=System.currentTimeMillis();
        WebElement webElement=null;
        try {

            int lastSize=0;
            int count=0;
            long loadTime=0;
            long t=System.currentTimeMillis();


            while (flag) {
                logFM.i("==============");
                long c=System.currentTimeMillis();
                flag =isWait();
                logFM.d("iswait 耗时", System.currentTimeMillis() - c);
                c=System.currentTimeMillis();
                List<WebElement> itemList=driver.findElementsByXPath("//div[@class='layout_3col_center']//div[@class='pagedlist_item']");

                logFM.d("获得itemlist 耗时",System.currentTimeMillis()-c);
                c=System.currentTimeMillis();
                int size=itemList.size();
                if(size==lastSize){
                    driver.executeScript("javascript:window.scroll( 0,document.body.scrollHeight);");
                    Thread.sleep(500);
                    logFM.i("通过滑动到低进行拉取数据,包含数据条目＝",size);
                }

                lastSize=size;

                for(int m=count;m<size;m++){
                    c=System.currentTimeMillis();
                    long itemTime=System.currentTimeMillis();
                    logFM.d("item=",m);

                    WebElement  itemEl = itemList.get(m);
                    try {
                    logFM.d("itemel 耗时", System.currentTimeMillis() - c);
                    c=System.currentTimeMillis();
                    count++;
                    WebElement upvoteCountEl = itemEl.findElement(
                                By.xpath("./div/div/div/div/span/a[@action_click='AnswerUpvote']/span[@class='count']"));
                    int upvote = HtmlParserUtils.htmlCount(upvoteCountEl.getText());

                    logFM.d("upvote 耗时", System.currentTimeMillis() - c);
                    c=System.currentTimeMillis();
                    if (upvote < this.upvoteCount) {
                        logFM.d("没通过点赞数量筛选,赞同数＝",upvote);
                        continue;
                    }
                    logFM.d("通过点赞数量筛选,赞同数＝",upvote);

                        c=System.currentTimeMillis();
                        WebElement moreEl = itemEl.findElement(By.xpath(".//a[@class='more_link']"));
                        logFM.d("moreEl 耗时", System.currentTimeMillis() - c);
                        QuoraWebDriverExecute.clickMoreElement(driver, moreEl, false);
                        logFM.i("------------------");
                    }catch (Exception e){
                        //logFM.e(itemEl.getText());
                        //logFM.e(String.valueOf(count)+" 出错－－－－－－－－－－－－－");
                    }
                    logFM.e(String.valueOf(m)+"-"+"item 耗时 "+String.valueOf(System.currentTimeMillis()-itemTime));
                }
                logFM.e("共用时"+String.valueOf(System.currentTimeMillis()-t)+"毫秒");
            }

        }catch (Exception e){

            e.printStackTrace();

        }finally {

        }
        logFM.i("加载完数据，耗时", System.currentTimeMillis() - time);

        time=System.currentTimeMillis();
        List<WebElement> itemList = driver.findElementsByXPath("//div[@class='layout_3col_center']//div[@class='pagedlist_item']");
        ArrayList failedMoreWE = new ArrayList();
        Iterator<WebElement> iterator = itemList.iterator();
        while (iterator.hasNext()) {

            WebElement  itemEl = iterator.next();
            try {
                WebElement upvoteCountEl = itemEl.findElement(
                        By.xpath("./div/div/div/div/span/a[@action_click='AnswerUpvote']/span[@class='count']"));
                int upvote = HtmlParserUtils.htmlCount(upvoteCountEl.getText());
                if (upvote < this.upvoteCount) {
                    continue;
                }
                logFM.d("通过点赞数量筛选");
                WebElement moreEl = itemEl.findElement(By.xpath(".//a[@class='more_link']"));
                if(QuoraWebDriverExecute.clickMoreElement(driver, moreEl, false)){
                    failedMoreWE.add(itemEl);
                }
                logFM.i("------------------");
            }catch (Exception e){
                failedMoreWE.add(itemEl);
            }
        }

        logFM.i("耗时", System.currentTimeMillis() - time, "毫秒检验，点击拉取数据失败共有", failedMoreWE.size());


    }
    private int lastSize=0;
    private long lastTime=0;
    private int update=0;
    private int itemSum=0;
    private long seeTime=0;

    private boolean isWait(){

        List<WebElement> pagedListElement=driver.findElements(By.className("pagedlist_item"));

        int loadItemCount=pagedListElement.size();

        if(itemSum==0||loadItemCount>5000){
            return false;
        }
        update++;
        if((System.currentTimeMillis()-lastTime)>30000){
            logFM.i("第",update,"次加载了",loadItemCount,"个内容（内容总共有",itemSum,"个）");

            if(lastSize<loadItemCount){
                lastSize=loadItemCount;
                lastTime=System.currentTimeMillis();

            }else {
                logFM.e("网络出错，没有加载成功" + "加载了" + String.valueOf(loadItemCount) + "个内容（内容总共有" + String.valueOf(itemSum) + "个");

                return false;
            }
        }
        if(itemSum>loadItemCount){

            return true;
        }else {
            loadItemCount=0;
            Iterator<WebElement> pagedIter=pagedListElement.iterator();
            while (pagedIter.hasNext()){
                WebElement key=pagedIter.next();

                if(key.isDisplayed()) {
                    loadItemCount = loadItemCount + 1;

                }
            }
            if(loadItemCount<itemSum){
                return true;
            }else {
                logFM.i("加载完成,加载了",loadItemCount,"个，总共",itemSum,"个内容");
            }
        }
        return false;
    }
}
