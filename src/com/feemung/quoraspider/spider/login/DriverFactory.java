package com.feemung.quoraspider.spider.login;

import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;


public class DriverFactory {
    private static ChromeDriver webDriver;
    public static WebDriver create() {

        // TODO Auto-generated method stub
        String chromdriver="/Users/feemung/IdeaProjects/global/chromedriver";
        System.setProperty("webdriver.chrome.driver", chromdriver);
        ChromeOptions options = new ChromeOptions();

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("chrome.switches",
                Arrays.asList("--start-maximized"));
        options.addArguments("--test-type", "--start-maximized");
        ChromeDriver driver=new ChromeDriver(options);
        webDriver=driver;
        return driver;
    }

    public static ChromeDriver getChromeDriver() {
        return webDriver;
    }
}