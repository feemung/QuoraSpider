package com.feemung.quoraspider.spider.fetcher;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by feemung on 16/4/25.
 */
public class HttpURLConnectionFetcher {
    public static InputStream get(String url)throws Exception{
        URL purl = new URL(url);
        HttpURLConnection pconn = (HttpURLConnection) purl.openConnection();
        pconn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
       // pconn.setRequestProperty("Referer", "https://www.zhihu.com/");

        pconn.setRequestProperty("Connection", "keep-alive");
        //pconn.setRequestProperty("Cookie", myCookies.toString());
        //pconn.setRequestProperty("Cookie", cook);
        //pconn.setRequestProperty("Content-Length", "108");
        pconn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        pconn.connect();
        if(pconn.getResponseCode()!=200){
            return null;
        }
        InputStream inputStream1 = pconn.getInputStream();


        return inputStream1;

    }
}
