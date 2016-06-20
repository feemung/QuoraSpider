package com.feemung.quoraspider.spider.fetcher;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.fetcher.quora.*;

import com.feemung.quoraspider.spider.login.LoginWebDriverUtils;
import com.feemung.quoraspider.spider.login.quora.CheckLoginStatus;
import org.apache.http.impl.execchain.RetryExec;
import org.jsoup.Jsoup;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by feemung on 16/4/27.
 */
public class ChromeDriverFetcher {


    private static Map<String,ChromeDriverFetcher> threadMap=new TreeMap<>();///<Thread,WebDriver>
    private LogFM logFM=LogFM.getInstance(ChromeDriverFetcher.class);
    public RemoteWebDriver driver;
    private Lock lock=new ReentrantLock();
    private static  ChromeDriverService service;
    private  static boolean runFlag=false;
    private String link;
    private boolean online=false;
    private boolean isLogin=false;
    private boolean login=true;

    public static   void createAndStartService() throws Exception{
        if(service!=null&&service.isRunning()){
            return;
        }
        // ChromeOptions options = new ChromeOptions();
        //options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");

        System.setProperty("webdriver.chrome.driver", "/Users/feemung/IdeaProjects/global/chromedriver");

        service = new ChromeDriverService.Builder().usingDriverExecutable(
                new File("/Users/feemung/IdeaProjects/global/chromedriver"))
                .usingAnyFreePort()
                .build();
        try {
            service.start();
            runFlag=true;
        }catch (Exception e){
            runFlag=false;
            throw e;
        }
    }
    public static ChromeDriverFetcher getChromeDriverFetcher(){
        if(threadMap.size()==0){
            try {
               // createAndStartService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String thread=Thread.currentThread().getName();
        if(!threadMap.containsKey(thread)) {
            ChromeDriverFetcher fetcher=new ChromeDriverFetcher();
            threadMap.put(thread,fetcher);
        }
        return threadMap.get(thread);
    }
    private ChromeDriverFetcher(){
        logFM.stopFlag=false;
        createClient();
        closeAllLogger();
        //driver.manage().timeouts().setScriptTimeout(15,TimeUnit.SECONDS);
    }
    private void createClient(){
       // driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");

        System.setProperty("webdriver.chrome.driver", "/Users/feemung/IdeaProjects/global/chromedriver");

        String host="127.0.0.1";
        String port="1080";
        options.addArguments("--proxy-server=socks5://" + host + ":" + port);
        driver = new ChromeDriver(options);

        //driver = new ChromeDriver();
    }
    public String get(String url)throws Exception{

        link=url;
        logFM.d("获取网页数据" + driver.getWindowHandle() + " " + driver);

          driver.get(url);
        if(login){
            if(!CheckLoginStatus.isOnline(Jsoup.parse(driver.getPageSource()))){
                try {
                    LoginWebDriverUtils.login(driver,"https://www.quora.com/");
                } catch (Exception e) {
                    e.printStackTrace();
                    logFM.e("在登陆访问模式时出现意外掉线并且无法再次登陆");
                    String thread=Thread.currentThread().getName();
                    threadMap.remove(thread);
                    driver.quit();
                    throw e;
                }
                driver.get(url);
            }
        }
        if(url.matches("^https://www\\.quora\\.com[/]*$")){
            QuoraHomeHtmlWebDriverUtils homeWebDriverUtils=new QuoraHomeHtmlWebDriverUtils(driver);
            homeWebDriverUtils.waitRun();

        }else if(url.matches("^https://www\\.quora\\.com/[\\w-]+$")&&url.contains("-")) {
            QuoraQuestionHtmlWebDriverUtils waitDriver=new QuoraQuestionHtmlWebDriverUtils(driver);
            waitDriver.waitLoad();
        }else if(url.matches("^https://www\\.quora\\.com/profile/[\\w-]+$")){
            QuoraProfileAnswerHtmlWebDriverUtils utils=new QuoraProfileAnswerHtmlWebDriverUtils(driver);
            utils.waitRun();

        }else if(url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/questions$")){
            QuoraProfileDefaultHtmlWebDriverUtils waitDriver=new QuoraProfileDefaultHtmlWebDriverUtils(driver);
            waitDriver.waitLoad();
        }else if(
                url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/followers$")||
                url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/following$"))
        {
            QuoraProfileDefaultHtmlWebDriverUtils waitDriver=new QuoraProfileDefaultHtmlWebDriverUtils(driver);
            QuoraWebDriverExecute.clickUserFollow(driver.findElementByClassName("header"),1000);
          waitDriver.waitLoad();
        }


        return driver.getPageSource();
    }
    public void quit(){
        driver.quit();
    }


    public static void stopServer(){
        service.stop();
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    public void closeAllLogger(){
        Logger logger = Logger.getLogger(RetryExec.class.getName());
        logger.setLevel(Level.OFF);
    }
}