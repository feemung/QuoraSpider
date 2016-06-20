package com.feemung.quoraspider;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.FileUtils;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.UserInfo;
import com.feemung.quoraspider.spider.DefaultMaskMaster;
import com.feemung.quoraspider.spider.entry.HTMLTask;
import com.feemung.quoraspider.spider.fetcher.ChromeDriverFetcher;
import com.feemung.quoraspider.spider.parse.quora.HomeHtmlParser;
import com.feemung.quoraspider.spider.parse.quora.ProfileHtmlParser;
import com.feemung.quoraspider.spider.queue.MemoryQueue;
import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.sql.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.*;
import java.util.List;

public class Main {



/**
 *
 [main] .isWait(QuoraProfileDefaultHtmlWebDriverUtils.java:65)>>>>第171次加载了1141个内容（内容总共有1133个）
 [main] .isWait(QuoraProfileDefaultHtmlWebDriverUtils.java:101)>>>>加载完成,加载了1133个，总共1133个内容
 [main] .get(ChromeDriverFetcher.java:193)>>>>1113定位耗时3928
 [main] .get(ChromeDriverFetcher.java:193)>>>>1114定位耗时6228
 [main] .get(ChromeDriverFetcher.java:193)>>>>1115定位耗时8087
 [main] .get(ChromeDriverFetcher.java:193)>>>>1116定位耗时10443
 [main] .get(ChromeDriverFetcher.java:266)>>>>加载完数据，耗时2944908
 [main] .get(ChromeDriverFetcher.java:293)>>>>耗时218348毫秒检验，点击拉取数据失败共有30
 [main] .get(ChromeDriverFetcher.java:293)>>>>耗时102882毫秒检验，点击拉取数据失败共有0
 [main] .get(ChromeDriverFetcher.java:293)>>>>耗时94911毫秒检验，点击拉取数据失败共有0
 [main] .saveFile(FileUtils.java:49)>>>>pt3.html文件已经保存在了/Users/feemung/Desktop/question/pt3.html
 [main] .main3(Main.java:125)>>>>yongshi=3404378
 *

 [main] .main(Main.java:208)>>>>总共采集用户共计 size=231983
 [main] .main(Main.java:209)>>>>每个用户的关注用户加起来共计672347
 [main] .main(Main.java:210)>>>>完整采集关注用户的共计1691
 [main] .main(Main.java:211)>>>>采集用户的文章共计1323399
 [main] .main(Main.java:212)>>>>超过1000个答案的用户共计338
 [main] .main(Main.java:213)>>>>被关注的人数共计18421892
 [main] .main(Main.java:229)>>>>关注超过1000人共计5403
 [main] .main(Main.java:230)>>>>关注人数少于1000人共计226580
 [main] .main(Main.java:231)>>>>关注超过1000的用户占总用户的比例2.32905%
 [main] .main(Main.java:243)>>>>关注超过2000人共计2641
 [main] .main(Main.java:244)>>>>关注人数少于2000人共计229342
 [main] .main(Main.java:245)>>>>关注超过2000的用户占总用户的比例1.1384455%


 [main] .main(Main.java:226)>>>>总共采集用户共计 size=237562
 [main] .main(Main.java:227)>>>>每个用户的关注用户加起来共计701587
 [main] .main(Main.java:228)>>>>完整采集关注用户的共计1757
 [main] .main(Main.java:229)>>>>采集用户的文章共计1363908
 [main] .main(Main.java:230)>>>>超过1000个答案的用户共计351
 [main] .main(Main.java:231)>>>>被关注的人数共计18641348
 [main] .main(Main.java:232)>>>>=================
 [main] .main(Main.java:247)>>>>关注超过2000人共计2641
 [main] .main(Main.java:248)>>>>关注人数少于2000人共计234921
 [main] .main(Main.java:249)>>>>关注超过2000的用户占总用户的比例1.1117098%
 [main] .main(Main.java:256)>>>>关注超过1000人共计5415
 [main] .main(Main.java:257)>>>>关注人数少于1000人共计232147
 [main] .main(Main.java:258)>>>>关注超过1000的用户占总用户的比例2.2794049%


 [main] .main(Main.java:242)>>>>总共采集用户共计 size=243691
 [main] .main(Main.java:243)>>>>每个用户的关注用户加起来共计729580
 [main] .main(Main.java:244)>>>>完整采集关注用户的共计1800
 [main] .main(Main.java:245)>>>>采集用户的文章共计1401834
 [main] .main(Main.java:246)>>>>超过1000个答案的用户共计356
 [main] .main(Main.java:247)>>>>被关注的人数共计18801941
 [main] .main(Main.java:248)>>>>=================
 [main] .main(Main.java:249)>>>>=================
 [main] .main(Main.java:250)>>>>=================
 [main] .main(Main.java:263)>>>>关注超过2000人共计5417
 [main] .main(Main.java:264)>>>>关注人数少于1000人共计238274
 [main] .main(Main.java:265)>>>>关注超过1000的用户占总用户的比例2.222897%
 [main] .main(Main.java:231)>>>>关注超过2000人共计2642
 [main] .main(Main.java:232)>>>>关注人数少于2000人共计241049
 [main] .main(Main.java:233)>>>>关注超过2000的用户占总用户的比例1.0841599%
 * */


    private static LogFM logFM=LogFM.getInstance(Main.class);
    public static void main(String args[]) throws Exception {
        Map<String,Integer> map=new TreeMap<>();
        logFM.d(map.get("nu"));
    }
    public static void main2(String args[]) throws Exception {
        Element elemenl = Jsoup.parse(FileUtils.readFile("answer4.html"));
        Elements elements = elemenl.getElementsByAttribute("href");
        Iterator<Element> iterator = elements.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Element key = iterator.next();
            if (key.attr("href").contains("/hprofile/")) {
                logFM.d(key.toString());
                count++;
            }

        }
        logFM.d(count);
    }
    public static void main8() throws Exception {
        //main3();
        AnswerSQL sql=AnswerSQL.getInstance();
        sql.connectionMysql();
        ArrayList<Answer> list=sql.searchAnswerUser("Alex-Suchman");
        Iterator<Answer> iter=list.iterator();
        while (iter.hasNext()){
            Answer answer=iter.next();
            logFM.d(answer.getUpvotersCountStr(),"==",answer.getQuestion().replace("-"," "));
        }
        logFM.d(list.size());
        logFM.d("-----------------------------");
        new ProfileHtmlParser().test();
        ChromeDriverFetcher fetcher=ChromeDriverFetcher.getChromeDriverFetcher();
        FileUtils.saveFile("answer4.html",fetcher.get("https://www.quora.com/sitemap/people"),false);
    }
    public static void main3() throws Exception {

        DefaultMaskMaster defaultMaskMaster=new DefaultMaskMaster();
        defaultMaskMaster.start();
        Task task = new HTMLTask();
        //task.setURL("https://www.quora.com/profile/Dushka-Zapata");
        task.setURL("https://www.quora.com/profile/Alex-Suchman");

        defaultMaskMaster.executeTasks(task);


    }
    public static void main2() throws Exception {
        AnswerSQL answerSQL=AnswerSQL.getInstance();
        answerSQL.connectionMysql();
        List<Answer> list=answerSQL.searchAnswerUser("Kay-Aull");
        logFM.d(list.get(7));
        FileUtils.saveFile("an2.html",list.get(7).getContent(),false);
    }
    public static void start2() throws Exception {
        //start();
        //test2();
       // main();
        VisitedTableSQLUtils sqlUtils=VisitedTableSQLUtils.getInstance();
        sqlUtils.connectionMysql();

        DefaultMaskMaster defaultMaskMaster=new DefaultMaskMaster();
        defaultMaskMaster.start();
        Task task = new HTMLTask();
        task.setURL("https://www.quora.com/profile/Bruce-Upbin");
       // sqlUtils.deleteTask(task);
       // defaultMaskMaster.executeTasks(task);
        MemoryQueue memoryQueue=MemoryQueue.getInstance();
        memoryQueue.startPriority();
        UserInfoSQLUtils userInfoSQLUtils=UserInfoSQLUtils.getInstance();
        userInfoSQLUtils.connectionMysql();
        ArrayList<UserInfo> list=userInfoSQLUtils.getAllData();
        Iterator<UserInfo> infoIterator=list.iterator();
        while (infoIterator.hasNext()){
            UserInfo key=infoIterator.next();
            if(Integer.valueOf(key.getFollowersCount())>10000){
                Task t=new HTMLTask();
                t.setURL("https://www.quora.com/profile/" + key.getRealUid());
                memoryQueue.add(t);
            }
        }
        logFM.d("jiaru",memoryQueue.getSize());


    }
    public static void start() throws Exception {
        UserInfoSQLUtils userInfoSQLUtils=UserInfoSQLUtils.getInstance();
        userInfoSQLUtils.connectionMysql();
        ArrayList<UserInfo> userInfoArrayList=userInfoSQLUtils.getAllData();

        VisitedTableSQLUtils visitedTableSQLUtils=VisitedTableSQLUtils.getInstance();
        visitedTableSQLUtils.connectionMysql();
        ArrayList<HTMLTask> htmlTaskArrayList=visitedTableSQLUtils.getAllDate();

        NotVisitedTableSQLUtils notVisitedTableSQLUtils=NotVisitedTableSQLUtils.getInstance();
        notVisitedTableSQLUtils.connectionMysql();
        ArrayList<HTMLTask> notVistedList=notVisitedTableSQLUtils.getAllDate();

        FailedVisitedTableSQLUtils failedVisted=FailedVisitedTableSQLUtils.getInstance();
        failedVisted.connectionMysql();
        ArrayList<HTMLTask> failedList=failedVisted.getAllDate();

        DefaultMaskMaster defaultMaskMaster=new DefaultMaskMaster();
        defaultMaskMaster.start();

        MemoryQueue memoryQueue=MemoryQueue.getInstance();
        memoryQueue.startPriority();

        ArrayList list=new ArrayList();
        Iterator<HTMLTask> iter=notVistedList.iterator();
        HTMLTask htmlTask=new HTMLTask();
       // htmlTask.setURL("https://www.quora.com/");
      //  defaultMaskMaster.executeTasks(htmlTask);
        defaultMaskMaster.executeTasks(iter.next());
        Thread.sleep(3000);


        ArrayList list1=new ArrayList();
        while (iter.hasNext()){
            HTMLTask key=iter.next();


            if(key.getPriority()>1000){
               // list.add(key);
                memoryQueue.add(key);
                list.add(key);

            }else {
                list1.add(key);
            }
        }

        logFM.e("list is " + String.valueOf(list.size()));
        logFM.e("list1 is "+String.valueOf(list1.size()));
        logFM.e("userInfoArrayList size is "+String.valueOf(userInfoArrayList.size()));
        logFM.e("htmlTaskArrayList size is " + String.valueOf(htmlTaskArrayList.size()));
        logFM.e("faildVistedArrayList size is " + String.valueOf(failedList.size()));

    }
    public static void main() throws Exception {
       String url="https://www.quora.com/profile/C-Stuart-Hardwick/following";

        MemoryQueue memoryQueue=MemoryQueue.getInstance();
        memoryQueue.startPriority();
        String taskSt="{\"url\"='https://www.quora.com/profile/Jimmy-Wales/following',\"saveDate\"='20160510222158'}";

        VisitedTableSQLUtils utils=VisitedTableSQLUtils.getInstance();
        NotVisitedTableSQLUtils notutils=NotVisitedTableSQLUtils.getInstance();
        UserInfoSQLUtils userInfoSQLUtils=UserInfoSQLUtils.getInstance();
        utils.connectionMysql();
        notutils.connectionMysql();
        userInfoSQLUtils.connectionMysql();
        ArrayList<UserInfo> userInfoArrayList=userInfoSQLUtils.getAllData();
        Iterator<UserInfo> userInfoIterator=userInfoArrayList.iterator();
        //userInfoSQLUtils.printTableDate();
        Map<String,Integer> map=new HashMap<>();
        ArrayList<String> userList=new ArrayList<>();
        ArrayList rightList=new ArrayList();
        int count=0;
        int answerCount=0;
        int answer1000Count=0;
        int follersCount=0;
       while (userInfoIterator.hasNext()){
            UserInfo userInfo=userInfoIterator.next();
           if(userInfo==null){
               continue;
           }
            Map<String,Integer> followingMap=userInfo.getFollowingMap();
           map.putAll(followingMap);
           count=count+followingMap.size();

           String followingCountStr=userInfo.getFollowingCount();
           if(followingCountStr==null||"null".equals(followingCountStr)){
               followingCountStr="0";
           }
           if(followingMap!=null&&Integer.valueOf(followingCountStr)-followingMap.size()<50){
               rightList.add(userInfo);
           }
           String answerCountStr=userInfo.getAnswerCount();
           if(answerCountStr==null||"null".equals(answerCountStr)){
               answerCountStr="0";
           }
           answerCount=answerCount+Integer.valueOf(answerCountStr);
           if(Integer.valueOf(answerCountStr)>1000){
               answer1000Count=answer1000Count+1;
           }
           String followersCountStr=userInfo.getFollowersCount();
           if(followersCountStr==null||"null".equals(followersCountStr)){
               followersCountStr="0";
           }
           follersCount=follersCount+Integer.valueOf(followersCountStr);
          // logFM.d("i=", i);
           //logFM.d("following size=", followingMap.size());
           //logFM.d("map size=",map.size());
           userList.add(userInfo.getRealUid());
        }
        logFM.d("总共采集用户共计 size=", map.size());
        logFM.d("每个用户的关注用户加起来共计",count);
        logFM.d("完整采集关注用户的共计",rightList.size());
        logFM.d("采集用户的文章共计",answerCount);
        logFM.d("超过1000个答案的用户共计",answer1000Count);
        logFM.d("被关注的人数共计",follersCount);
        logFM.d("=================");
        logFM.d("=================");
        logFM.d("=================");
        Iterator<String> mapIter=map.keySet().iterator();
        ArrayList l=new ArrayList();
        ArrayList l2=new ArrayList();
        while (mapIter.hasNext()){
            String key=mapIter.next();
            int value=map.get(key);
            if(value>2000){
                l.add(key);
            }else {
                l2.add(key);
            }
        }
        logFM.d("关注超过2000人共计",l.size());
        logFM.d("关注人数少于2000人共计",l2.size());
        logFM.d("关注超过2000的用户占总用户的比例",((float)l.size())/map.size()*100,"%");

    }
    public static void delAll() throws Exception {
        QuestionMySQLUtils q=QuestionMySQLUtils.getInstance();
        q.connectionMysql();
        q.deleteTable();
        AnswerSQL answerSQL=AnswerSQL.getInstance();
        answerSQL.connectionMysql();
        answerSQL.deleteTable();
        VisitedTableSQLUtils u=VisitedTableSQLUtils.getInstance();
        u.connectionMysql();
        u.deleteTable();
    }
    public static void test2()throws Exception{
       String url="https://www.quora.com/What-are-some-realistic-ways-to-get-rich-in-5-years";

        ChromeOptions options = new ChromeOptions();
        options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");

        System.setProperty("webdriver.chrome.driver", "/Users/feemung/IdeaProjects/global/chromedriver");
        String PROXY = "127.0.0.1:1080";
        String host="127.0.0.1";
        String port="1080";
        options.addArguments("--proxy-server=socks5://" + host + ":" + port);
        WebDriver driver = new ChromeDriver(options);

        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();

        //proxy.setHttpProxy(PROXY);
        proxy.setSocksProxy(PROXY).setSocksUsername("").setSocksPassword("");

        // .setFtpProxy(PROXY)
                //.setSslProxy(PROXY);

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.PROXY, proxy);
        //cap.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true);
        //cap.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true);
       // System.setProperty("http.nonProxyHosts", "localhost");
       // ChromeDriver driver = new ChromeDriver(cap);
       // driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
       // driver.get("https://www.quora.com/profile/Anshu-Singh-21");
       // LoginWebDriverUtils.login(driver, "https://www.quora.com/");
        //driver.get("https://www.quora.com/profile/Adam-Leffert");
        driver.get("http://www.baidu.com/");
        driver.get("http://www.ip138.com/");
        boolean flag=true;
        while(flag){
            try {
                Thread.sleep(3000);
               logFM.d("c==", driver.getWindowHandles().size());
                WebElement we = driver.findElement(By.className("photo_modal_container"));

                if (we != null) {
                    logFM.e("faxian le");
                    WebElement closeWE=driver.findElement(By.className("photo_modal_close active"));
                    closeWE.click();
                    Thread.sleep(3000);
                }

            }catch (Exception e){
                //e.printStackTrace();
            }
        }
        Thread.sleep(60000);
        driver.quit();
    }

}
