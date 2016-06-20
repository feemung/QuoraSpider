package com.feemung.quoraspider.spider.login.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

/**
 * Created by feemung on 16/5/11.
 */
public class CheckLoginStatus {
    private static LogFM logFM=LogFM.getInstance(CheckLoginStatus.class);

    public static boolean isOnline(Element pageElement){
        logFM.stopFlag=true;
        if(isFindSignIn(pageElement)||isFindLoginWebElement(pageElement)){
            logFM.d("check find not online");
            return false;
        }
        logFM.d("check find online");
        return true;
    }
    private static boolean isFindSignIn(Element pageElement){
        Element element=pageElement.getElementsByClass("signup_login_buttons").first();

        if(element!=null&&element.text().contains("Sign In")){
            logFM.d("check find SignInElement");
            return true;
        }
        logFM.d("check not find SignInElement");
        return false;
    }
    public static boolean isFindLoginWebElement(Element pageElement){
        Element loginEln=pageElement.getElementsByClass("regular_login").first();
        if(loginEln!=null){
            Element user = loginEln
                    .getElementsByAttributeValue("name", "email").first();

            Element password = loginEln.getElementsByAttributeValue("name", "password").first();
            //logFM.d(pageElement.baseUri());
            //logFM.d(password.isBlock());
            if(user!=null&&password!=null){
                logFM.d("check find login");

                return true;
            }
        }
        logFM.d("check not find login");
        return false;
    }
    public static void test(){
        Element element= Jsoup.parse(FileUtils.readFile("quora.html"),"https://feemung");
        Iterator<Element> iterator=element.getElementsByAttribute("href").iterator();
        while (iterator.hasNext()){
            logFM.d(iterator.next().baseUri());
        }
        element.baseUri();
        isFindLoginWebElement(element);
        isFindSignIn(element);
        isOnline(element);
    }
}
