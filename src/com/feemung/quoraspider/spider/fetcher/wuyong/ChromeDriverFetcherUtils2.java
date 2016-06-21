package com.feemung.quoraspider.spider.fetcher.wuyong;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.fetcher.quora.QuoraQuestionHtmlWebDriverUtils;
import com.feemung.quoraspider.spider.fetcher.quora.WaitDriver;
import com.feemung.quoraspider.spider.login.LoginWebDriverUtils;
import com.feemung.quoraspider.spider.login.quora.CheckLoginStatus;
import org.jsoup.Jsoup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/27.
 */
public class ChromeDriverFetcherUtils2{


    private static Map<String,ChromeDriverFetcherUtils2> threadMap=new TreeMap<>();///<Thread,WebDriver>
    private LogFM logFM=LogFM.getInstance(ChromeDriverFetcherUtils2.class);
    public RemoteWebDriver driver;
    private Lock lock=new ReentrantLock();
    private static  ChromeDriverService service;
    private  static boolean runFlag=false;
    private String link;
    private boolean online=false;
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
    public static ChromeDriverFetcherUtils2 getChromeDriverFetcher(String mode){

        if(threadMap.size()==0){
            try {
                createAndStartService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String thread=Thread.currentThread().getName();
        if(!threadMap.containsKey(mode)) {
            ChromeDriverFetcherUtils2 fetcher=new ChromeDriverFetcherUtils2();
            threadMap.put(mode,fetcher);
        }
        return threadMap.get(mode);
    }
    private ChromeDriverFetcherUtils2(){
        logFM.stopFlag=false;
        createClient();
        try {
            get("https://www.quora.com/");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //driver.manage().timeouts().setScriptTimeout(15,TimeUnit.SECONDS);
    }
    private void createClient(){
        driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
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
                   // driver.quit();
                    throw e;
                }
            }
        }
        Set<String> handleSet= driver.getWindowHandles();
        Iterator<String> handleSetIter=handleSet.iterator();
        while (handleSetIter.hasNext()){
            String handle=handleSetIter.next();
            if(!handleMap.containsValue(handle)){
                handleMap.put("login",handle );
                break;
            }

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

    private Map<String,String> urlMap=new HashMap<>();//key=threadName,value=url;
    private Map<String,String> handleMap=new HashMap<>();//key=threadName,value=handle;
    private Map<String,LinkedBlockingQueue<String>> resultMap=new HashMap<>();//key=threadName,value=get page result
    public void addTask(String url){
        lock.lock();
        driver.executeScript("window.open('" + url + "');");
        Set<String> handleSet= driver.getWindowHandles();
        Iterator<String> handleSetIter=handleSet.iterator();
        String thread=Thread.currentThread().getName();
        while (handleSetIter.hasNext()){
            String handle=handleSetIter.next();
            if(!handleMap.containsValue(handle)){
                logFM.d(thread,url+" ="+handle);
                handleMap.put(thread, handle);
                driver.switchTo().window(handle);
                break;
            }

        }
        logFM.d("handleMap.size=",handleMap.size());
        lock.unlock();
    }
    public boolean isLoad()throws Exception{
        lock.lock();
        /*
        Map<String,Object> loadMap
                =new HashMap<>();
        String thread=Thread.currentThread().getName();
        if(handleMap.isEmpty()&&!handleMap.containsKey(thread)){
            lock.unlock();
            throw new Exception();
        }
        String handle = handleMap.get(thread);
        WebDriver wd=driver.switchTo().window(handle);
        logFM.d(wd.getTitle());
        driver.executeScript("javascript:window.scroll( 0,document.body.scrollHeight);");
        if (!loadMap.containsKey(handle)) {

            String url = wd.getCurrentUrl();
            if (url.matches("^https://www\\.quora\\.com/[\\w-]+$") && url.contains("-")) {
                QuoraQuestionHtmlWebDriverUtils waitDriver = new QuoraQuestionHtmlWebDriverUtils(driver);
                loadMap.put(handle,waitDriver);
            } else if (url.matches("^https://www\\.quora\\.com/profile/[\\w-]+$") ||
                    url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/questions$") ||
                    url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/followers$") ||
                    url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/following$")) {
                QuoraProfilePageWaitDriverUtils waitDriver = new QuoraProfilePageWaitDriverUtils(driver);
                loadMap.put(handle,waitDriver);


            }

        }
        WaitDriver utils = loadMap.get(handle);
        if (!utils.isWait()) {
            lock.unlock();
            return false;
        }
        lock.unlock();
        return true;

*/

return false;
    }
    public String getResult()throws Exception{
        lock.lock();
        String thread=Thread.currentThread().getName();
        String handle = handleMap.get(thread);
        driver.switchTo().window(handle);
        if(login){
            if(!CheckLoginStatus.isOnline(Jsoup.parse(driver.getPageSource()))){
                try {
                    LoginWebDriverUtils.login(driver,"https://www.quora.com/");
                } catch (Exception e) {
                    e.printStackTrace();
                    logFM.e("在登陆访问模式时出现意外掉线并且无法再次登陆");
                    driver.quit();
                    throw e;
                }
            }
        }
        handleMap.remove(thread);
        lock.unlock();
        return driver.getPageSource();
    }



}