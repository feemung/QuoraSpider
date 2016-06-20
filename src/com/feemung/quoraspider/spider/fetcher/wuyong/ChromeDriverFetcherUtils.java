package com.feemung.quoraspider.spider.fetcher.wuyong;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.fetcher.quora.QuoraProfileDefaultHtmlWebDriverUtils;
import com.feemung.quoraspider.spider.fetcher.quora.QuoraQuestionHtmlWebDriverUtils;
import com.feemung.quoraspider.spider.login.LoginWebDriverUtils;
import com.feemung.quoraspider.spider.login.quora.CheckLoginStatus;
import org.jsoup.Jsoup;
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
public class ChromeDriverFetcherUtils implements Runnable{


    private static Map<String,ChromeDriverFetcherUtils> threadMap=new TreeMap<>();///<Thread,WebDriver>
    private LogFM logFM=LogFM.getInstance(ChromeDriverFetcherUtils.class);
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
    public static ChromeDriverFetcherUtils getChromeDriverFetcher(String mode){
        if(threadMap.size()==0){
            try {
                createAndStartService();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String thread=Thread.currentThread().getName();
        if(!threadMap.containsKey(mode)) {
            ChromeDriverFetcherUtils fetcher=new ChromeDriverFetcherUtils();
            threadMap.put(mode,fetcher);
        }
        return threadMap.get(mode);
    }
    private ChromeDriverFetcherUtils(){
        logFM.stopFlag=true;
        createClient();

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
                    driver.quit();
                    throw e;
                }
            }
        }

        if(url.matches("^https://www\\.quora\\.com/[\\w-]+$")&&url.contains("-")) {
            QuoraQuestionHtmlWebDriverUtils waitDriver=new QuoraQuestionHtmlWebDriverUtils(driver);
            waitDriver.waitLoad();
        }else if(url.matches("^https://www\\.quora\\.com/profile/[\\w-]+$")||
                url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/questions$")||
                url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/followers$")||
                url.matches("^https://www\\.quora\\.com/profile/[\\w-]+/following$"))
        {
            QuoraProfileDefaultHtmlWebDriverUtils waitDriver=new QuoraProfileDefaultHtmlWebDriverUtils(driver);
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

    private Map<String,String> urlMap=new HashMap<>();//key=threadName,value=url;
    private Map<String,String> handleMap=new HashMap<>();//key=threadName,value=handle;
    private Map<String,LinkedBlockingQueue<String>> resultMap=new HashMap<>();//key=threadName,value=get page result
    public void addTask(String url){
        String thread=Thread.currentThread().getName();
        urlMap.put(thread,url);
        if(!resultMap.containsKey(thread)) {
            resultMap.put(thread, new LinkedBlockingQueue<>());
        }
    }
    public String getResult()throws Exception{
        String thread=Thread.currentThread().getName();
        return resultMap.get(thread).take();
    }

    @Override
    public void run() {
        new Thread(new Load()).start();
        while (true){
            if(urlMap.isEmpty()){
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Iterator<String> iterator=urlMap.keySet().iterator();
            while (iterator.hasNext()){
                String thread=iterator.next();
                String url=urlMap.get(thread);
                urlMap.remove(url);
                driver.executeScript("Window.open(\"" + url + "\");");
                Set<String> handleSet= driver.getWindowHandles();
                Iterator<String> handleSetIter=handleSet.iterator();
                while (handleSetIter.hasNext()){
                    String handle=handleSetIter.next();
                    if(!handleMap.containsKey(thread)){
                        handleMap.put(thread, handle);

                    }

                }
            }
        }
    }
    class Load implements Runnable{
        @Override
        public void run() {
            Map<String,QuoraProfilePageWaitDriverUtils> loadMap
                    =new HashMap<>();
            while (true) {
                if(handleMap.isEmpty()){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Iterator<String> handleIter = handleMap.keySet().iterator();
                while (handleIter.hasNext()) {
                    String thread = handleIter.next();
                    String handle = handleMap.get(thread);
                    driver.switchTo().window(handle);
                    driver.executeScript("javascript:window.scroll( 0,document.body.scrollHeight);");
                    if (!loadMap.containsKey(handle)) {
                        loadMap.put(handle, new QuoraProfilePageWaitDriverUtils(driver));
                    }
                    QuoraProfilePageWaitDriverUtils utils = loadMap.get(handle);
                    if (!utils.isWait()) {
                        resultMap.get(thread).add(driver.getPageSource());
                        handleMap.remove(thread);
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

}