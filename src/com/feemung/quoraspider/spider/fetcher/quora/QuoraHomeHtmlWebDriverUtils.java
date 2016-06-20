package com.feemung.quoraspider.spider.fetcher.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.exception.ExceptionManager;
import com.feemung.quoraspider.spider.exception.LoadFailedException;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * Created by feemung on 16/5/23.
 */
public class QuoraHomeHtmlWebDriverUtils {
    private  LogFM logFM=LogFM.getInstance(QuoraHomeHtmlWebDriverUtils.class);
    private RemoteWebDriver driver;
    public QuoraHomeHtmlWebDriverUtils(RemoteWebDriver driver){
        this.driver=driver;
    }
    public void waitRun(){

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
                List<WebElement> moreEls=driver.findElementsByXPath("//a[@class='more_link']");
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
                    logFM.i("------------------");
                    logFM.i(count,"---获取moreEle耗时", System.currentTimeMillis() - c);
                    QuoraWebDriverExecute.clickMoreElement(driver, moreEl, false);
                    count++;

                }
                logFM.e("共用时"+String.valueOf(System.currentTimeMillis()-t)+"毫秒");
            }

        }catch (Exception e){
            e.printStackTrace();

        }finally {

        }
        logFM.i("加载完数据，耗时", System.currentTimeMillis() - time);

    }
    private int lastSize=0;
    private long lastTime=0;
    private int update=0;

    private boolean isWait(){

        List<WebElement> pagedListElement=driver.findElements(By.className("pagedlist_item"));

        int loadItemCount=pagedListElement.size();
        update++;
        if((System.currentTimeMillis()-lastTime)>30000){
            logFM.i("第",update,"次加载了",loadItemCount,"个内容");

            if(lastSize<loadItemCount){
                lastSize=loadItemCount;
                lastTime=System.currentTimeMillis();
            }else {
                logFM.e("网络出错，没有加载成功"+"加载了"+String.valueOf(loadItemCount)+"个内容");
                ExceptionManager.getInstance().add(new LoadFailedException("网络出错，没能完整加载数据"));
                return false;
            }
        }

        try {
            WebElement agoWE = driver.findElement(By.className("datetime"));
            logFM.i("加载完成,加载了",loadItemCount,"ge");
        }catch (Exception e){
            return true;
        }

        return false;
    }
}
