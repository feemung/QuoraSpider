package com.feemung.quoraspider.spider.utils;

/**
 * Created by feemung on 16/5/10.
 */
public class HtmlParserUtils {
    public static int htmlCount(String countStr){
        int count=0;
        if(countStr.contains("k")) {
            countStr=countStr.replace("k","");
            float temp=Float.valueOf(countStr)*1000;
            count = (int)temp;

        }else if(countStr.contains("m")){
            countStr=countStr.replace("m","");
            float temp=Float.valueOf(countStr)*1000000;
            count = (int)temp;

        }else {
            count=Integer.valueOf(countStr);
        }
        return count;
    }
}
