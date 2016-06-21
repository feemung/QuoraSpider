package com.feemung.quoraspider.spider.fetcher;

import com.feemung.quoraspider.spider.entry.Content;
import com.feemung.quoraspider.spider.entry.HTMLContent;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import com.gargoylesoftware.htmlunit.html.HtmlScript;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.StrictErrorReporter;
import com.gargoylesoftware.htmlunit.javascript.background.DefaultJavaScriptExecutor;
import com.gargoylesoftware.htmlunit.javascript.regexp.HtmlUnitRegExpProxy;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Jdk14Logger;


import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by feemung on 16/4/25.
 */

public class HtmlunitFetcher {

    public static String donwload(String url)throws Exception{
        closeAllLogger();
        WebClient wc=new WebClient();

        //request.setProxyHost("120.120.120.x");
        // request.setProxyPort(8080);
       // wc.addRequestHeader("Referer", refer);//设置请求报文头里的refer字段
        wc.addRequestHeader("Connection", "keep-alive");
        wc.addRequestHeader("Cache-Control", "no-cache");

         wc.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        wc.addRequestHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:45.0) Gecko/20100101 Firefox/45.0");
  //其他报文头字段可以根据需要添加
        wc.getCookieManager().setCookiesEnabled(true);//开启cookie管理
        wc.getOptions().setJavaScriptEnabled(true);//开启js解析。对于变态网页，这个是必须的
        wc.getOptions().setCssEnabled(true);//开启css解析。对于变态网页，这个是必须的。
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);

        HtmlPage page=null;
        page = wc.getPage(url);

        if(page==null)
        {
            System.out.println("采集 "+url+" 失败!!!");
            return null;
        }
        String pageUrl=page.getBaseURL().getProtocol()+"://"+page.getBaseURL().getHost();
        HTMLContent htmlContent=new HTMLContent();

        List list=page.getDocumentElement().getByXPath("//a[@href]");
        Iterator iterator=list.iterator();
        while (iterator.hasNext()){
            HtmlAnchor anchor=(HtmlAnchor)iterator.next();
            String link=anchor.getAttribute("href");
            if(link!=null&&(!link.contains("javascript"))){

                if(!link.contains("://")&&!link.contains(pageUrl)){
                    link=pageUrl+link;
                }
                htmlContent.addUrl(link);
            }
           // System.out.println("=============");
           // System.out.println(link);
        }
        String pageXml=page.asXml();//网页内容保存在content里
        //System.out.println(pageXml);
        //saveFile("question2.html",pageXml);
        wc.close();
       // return page;
        return pageXml;
    }
    public static void saveFile(String fileName,String text)throws Exception{
        String path="/Users/feemung/Desktop/";
        File file=new File(path+fileName);
        FileWriter out=new FileWriter(file);
        out.write(text);
        out.flush();
        out.close();
    }
    public static void donwload2(String  url)throws Exception{
        closeAllLogger();
        URL link=new URL(url);
        WebClient wc=new WebClient();

        Log LOG = LogFactory.getLog(WebClient.class);




        WebRequest request=new WebRequest(link);
        request.setCharset("UTF-8");
        //request.setProxyHost("120.120.120.x");
       // request.setProxyPort(8080);
      //  request.setAdditionalHeader("Referer", refer);//设置请求报文头里的refer字段
        request.setAdditionalHeader("Connection", "keep-alive");
        request.setAdditionalHeader("Cache-Control", "no-cache");
       // request.setAdditionalHeader("Pragma", "no-cache");
       // request.setAdditionalHeader("Cookie",getBai2());
        request.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        ////设置请求报文头里的User-Agent字段
        request.setAdditionalHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:45.0) Gecko/20100101 Firefox/45.0");

        //wc.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        //wc.addRequestHeader和request.setAdditionalHeader功能应该是一样的。选择一个即可。
        //其他报文头字段可以根据需要添加

        wc.getCookieManager().setCookiesEnabled(true);//开启cookie管理
        wc.getOptions().setJavaScriptEnabled(true);//开启js解析。对于变态网页，这个是必须的
        wc.getOptions().setCssEnabled(true);//开启css解析。对于变态网页，这个是必须的。
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getOptions().setThrowExceptionOnScriptError(false);
        wc.getOptions().setTimeout(10000);
        //设置cookie。如果你有cookie，可以在这里设置
        String c[]=getcookieStr().split("\n");
        for(String str:c){
            String temp[]=str.split(";");
            Cookie cookie;
            String maindo=null;
            for(int i=0;i<temp.length;i++) {
                if (temp[i].contains("domain")) {
                    maindo =temp[i].split("=")[1];
                }
            }
            cookie=new Cookie(maindo,temp[0].split("=")[0],temp[0].replace(temp[0].split("=")[0]+"=",""));
           // wc.getCookieManager().addCookie(cookie);
            // System.out.println(cookie.getDomain());
            //System.out.println(cookie.getName());
            //System.out.println(cookie.getValue());
            //System.out.println("=======");
        }

        //准备工作已经做好了
        HtmlPage page=null;
        try {
            page = wc.getPage(request);
        }catch (Exception e){
            System.out.println("错误");
        }


        if(page==null)
        {
            System.out.println("采集 "+url+" 失败!!!");
            return ;
        }
        List list=page.getDocumentElement().getByXPath("//a");
        Iterator iterator=list.iterator();
        while (iterator.hasNext()){
            HtmlAnchor anchor=(HtmlAnchor)iterator.next();
            System.out.println(anchor.getAttribute("href"));
        }
        String content=page.asText();//网页内容保存在content里
        if(content==null)
        {
            System.out.println("采集 "+url+" 失败!!!");
            return ;
        }
       // System.out.println(content);
        //搞定了
        CookieManager CM = wc.getCookieManager(); //WC = Your WebClient's name
        Set<Cookie> cookies_ret = CM.getCookies();//返回的Cookie在这里，下次请求的时候可能可以用上啦。
        Iterator<Cookie> i = cookies_ret.iterator();
        while (i.hasNext()&false)
        {
            Cookie cookie=i.next();

             System.out.println(cookie.getDomain());
            System.out.println(cookie.getName());
            System.out.println(cookie.getValue());
            System.out.println("=======");
        }
        System.out.println("完成了 size＝"+cookies_ret.size());
    }
    public static String getcookieStr() {
        return
                        "BAIDUID=B6B27FF92167CC1C310F88A202EB00FE:FG=1; expires=Fri, 31 Mar 2017 13:23:13 GMT; path=/; domain=.baidu.com\n" +
                                "BDUSS=NwdGFRcnIwbnFUTFN3dkxMWllQS1ZkbEw4ekpnZzRDflhZQUR1WFRpdkozRVZYQVFBQUFBJCQAAAAAAAAAAAEAAACZ1woCv9XW0Lqj0eAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMlPHlfJTx5XOE; expires=Fri, 12 Jul 2024 17:11:36 GMT; path=/; domain=.baidu.com; HttpOnly\n" +
                                "BD_HOME=1; path=/; domain=www.baidu.com\n" +
                                "BD_UPN=133252; expires=Thu, 05 May 2016 17:11:38 GMT; path=/; domain=www.baidu.com\n" +
                                "BIDUPSID=03BD77786F82EB0B9B04137AF5D27922; expires=Mon, 23 Mar 2048 16:35:35 GMT; path=/; domain=.baidu.com\n" +
                                "H_PS_645EC=9482aeO8NtgKIvKQwpq08dFh9B28tEVGPVRCpFQAHO4I6APyuEB0h8d6ck4; expires=Thu, 14 Apr 2016 17:38:26 GMT; path=/; domain=www.baidu.com\n" +
                                "H_PS_PSSID=10299_19513_19719_1458_19671_18241_19782_19803_19806_19808_18134_17001_15497_11448_19810; path=/; domain=.baidu.com\n" +
                                "PSTM=1459430584; expires=Tue, 18 Apr 2084 16:37:12 GMT; path=/; domain=.baidu.com\n" +
                                "__bsi=10539326096342145460_00_0_I_R_181_0303_C02F_N_I_I_0; expires=Mon, 25 Apr 2016 17:11:45 GMT; path=/; domain=.www.baidu.com\n";
    }

    public static void closeAllLogger(){
        Jdk14Logger l2=(Jdk14Logger) LogFactory.getLog(WebClient.class);
        l2.getLogger().setLevel(Level.OFF);

        Jdk14Logger l3=(Jdk14Logger) LogFactory.getLog(JavaScriptEngine.class);
        l3.getLogger().setLevel(Level.OFF);

        Jdk14Logger l4=(Jdk14Logger) LogFactory.getLog(HtmlPage.class);
        l4.getLogger().setLevel(Level.OFF);

        Jdk14Logger l5=(Jdk14Logger) LogFactory.getLog(HtmlUnitRegExpProxy.class);
        l5.getLogger().setLevel(Level.OFF);
        Jdk14Logger l6=(Jdk14Logger) LogFactory.getLog(DefaultCssErrorHandler.class);
        l6.getLogger().setLevel(Level.OFF);
        Jdk14Logger l7=(Jdk14Logger) LogFactory.getLog(HtmlScript.class);
        l7.getLogger().setLevel(Level.OFF);
        Jdk14Logger l8=(Jdk14Logger) LogFactory.getLog(IncorrectnessListenerImpl.class);
        l8.getLogger().setLevel(Level.OFF);
        Jdk14Logger l9=(Jdk14Logger) LogFactory.getLog(StrictErrorReporter.class);
        l9.getLogger().setLevel(Level.OFF);
        Jdk14Logger l10=(Jdk14Logger) LogFactory.getLog(DefaultJavaScriptExecutor.class);
        l10.getLogger().setLevel(Level.OFF);
        Jdk14Logger l11=(Jdk14Logger) LogFactory.getLog(WebConsole.class);
        l11.getLogger().setLevel(Level.OFF);
        Jdk14Logger l12=(Jdk14Logger) LogFactory.getLog(HttpWebConnection.class);
        l12.getLogger().setLevel(Level.OFF);
    }
}
