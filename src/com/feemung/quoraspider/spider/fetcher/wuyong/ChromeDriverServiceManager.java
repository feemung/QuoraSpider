package com.feemung.quoraspider.spider.fetcher.wuyong;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.File;
import java.net.URL;

/**
 * Created by feemung on 16/4/27.
 */
public class ChromeDriverServiceManager {
    private static ChromeDriverService service;
    private static boolean runFlag=false;

    public static void createAndStartService() throws Exception{
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
        }catch (Exception e){
            runFlag=false;
            throw e;
        }
    }


    public static void createAndStopService() {
        service.stop();
    }

    public static URL getServerUrl(){
        return service.getUrl();
    }


}
