package com.feemung.quoraspider.spider.fetcher.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.exception.ExceptionManager;
import com.feemung.quoraspider.spider.exception.LoadFailedException;
import com.feemung.quoraspider.spider.utils.HtmlParserUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Iterator;
import java.util.List;

/**
 * Created by feemung on 16/5/23.
 */
public class QuoraWebDriverExecute {
    private static LogFM logFM=LogFM.getInstance(QuoraWebDriverExecute.class);
    public static boolean clickMoreElement(WebDriver driver,WebElement moreEl,boolean isWaitLoad){
        Point itemPoint = null;
        long c=System.currentTimeMillis();
        for (int j = 0; j < 3; j++) {


            if (moreEl.isDisplayed()) {
                try {

                    logFM.i("--判断耗时", System.currentTimeMillis() - c);
                    c=System.currentTimeMillis();
                    itemPoint = moreEl.getLocation();
                    logFM.i("---获取位置耗时", System.currentTimeMillis() - c);
                    c=System.currentTimeMillis();
                    ((JavascriptExecutor)driver).executeScript("javascript:window.scroll( 0," + String.valueOf(itemPoint.getY() - 60) + ");");
                    logFM.i("---移动位置耗时", System.currentTimeMillis() - c);
                    c=System.currentTimeMillis();

                    moreEl.click();

                    logFM.i("---点击moreEle耗时", System.currentTimeMillis() - c);
                    c=System.currentTimeMillis();

                    if(isWaitLoad){
                        String  id = (moreEl.findElement(By.xpath("..")).getAttribute("id"));
                        String loadId=id.replace("more","loading");
                        for(int i=0;i<60;i++){

                            WebElement loadWE=driver.findElement(By.id(loadId));
                            if(!loadWE.isDisplayed()){
                                return true;
                            }
                            Thread.sleep(200);
                        }
                    }else {
                        return true;
                    }

                } catch (Exception e){
                    logFM.e("点击出错");

                    try {
                        WebElement we = driver.findElement(By.className("photo_modal_container"));

                        if (we != null) {
                            logFM.e("发现图片悬浮窗错误");
                            Actions action = new Actions(driver);
                            action.sendKeys(Keys.ESCAPE).perform();
                        }
                        try {
                            Thread.sleep(3000);
                            WebElement we2 = driver.findElement(By.className("photo_modal_container"));
                            if (we2 != null) {
                                logFM.e("图片悬浮窗未能成功关闭");
                                ExceptionManager.getInstance().add( new LoadFailedException("图片悬浮窗未能成功关闭"));
                            }
                        }catch (Exception e3) {
                            logFM.e("已经关闭悬浮窗错误");
                        }
                        break;
                    }catch (Exception e2) {
                        //e.printStackTrace();

                    }

                }
            }
        }

        return false;
    }
    public static boolean clickUserFollow(WebElement userFollowParentNode,int followCount){
        try {


            List<WebElement> userFollowBut = userFollowParentNode.findElements(By.xpath(".//a[@action_click='UserFollow']"));
            Iterator<WebElement> iterator=userFollowBut.iterator();
            while (iterator.hasNext()){
                WebElement but=iterator.next();
                WebElement followCountWE=but.findElement(By.className("count"));
                int count= HtmlParserUtils.htmlCount(followCountWE.getText());
                if(count>followCount){
                    but.click();
                    logFM.d("已经关注");
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public static boolean clickUserUnfollow(WebElement userUnfollowParentNode,int followCount){
        try {
            List<WebElement> userFollowBut = userUnfollowParentNode.findElements(By.xpath(".//a[@action_click='UserUnfollow']"));
            Iterator<WebElement> iterator=userFollowBut.iterator();
            while (iterator.hasNext()){
                WebElement but=iterator.next();
                WebElement followCountWE=but.findElement(By.className("count"));
                int count= HtmlParserUtils.htmlCount(followCountWE.getText());
                if(count>followCount){
                    but.click();
                    logFM.d("已经取消关注");
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }



}
