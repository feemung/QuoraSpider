package com.feemung.quoraspider.Utils;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.parse.RegexParser;
import org.jsoup.nodes.Element;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by feemung on 16/4/29.
 */
public class TimeStamp {
       private static LogFM logFM=LogFM.getInstance(TimeStamp.class);
    public static String defaultTimeFormat(Date date){
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(date);
    }


    public static String defaultTimeFormat(String date)throws Exception{
        SimpleDateFormat formatAnswer=new SimpleDateFormat("yyyyMMddHHmmss");
        Date result=null;
        String year=RegexParser.parse(date,"(19[0-9]{2})|(20[0-9]{2})");
        String mondd=RegexParser.parse(date, "[A-Z][a-z]{2}\\s(([1-2][0-9])|([1-9])|[30]|[31])");
        String week=RegexParser.parse(date,"Sun|Mon|Tue|Wed|Thu|Fri|Sat");
        String clock=RegexParser.parse(date,"[0-9]{1,2}|[am]|[pm]");

        if(!mondd.isEmpty()){
            if(year.isEmpty()) {
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                year =yearFormat.format(new Date());
            }
           String newDateStr=mondd+" "+year;
            SimpleDateFormat f=new SimpleDateFormat("MMM dd yyyy", Locale.US);
            result=f.parse(newDateStr);
        }else if(!week.isEmpty()){
            long l=System.currentTimeMillis();
            SimpleDateFormat f=new SimpleDateFormat("EEE", Locale.US);
            Map<String,Date> map=new HashMap<>();
            for(int i=0;i<7;i++){
                long day=l-3600*1000*24*i;
                Date dayDate=new Date(day);
                map.put(f.format(dayDate),dayDate);
            }
            if(map.containsKey(week)){
                result=map.get(week);
            }else {
                logFM.e("日期出错，没有这种格式"+date);
            }

        }else {
            long finishLong=0;
            if(date.contains("h ago")){
                finishLong=Long.valueOf(RegexParser.parse(date, "[0-9]{1,2}"))*3600000;

            } else if(date.contains("d ago")){
                finishLong=Long.valueOf(RegexParser.parse(date, "[0-9]{1,2}"))*3600000*24;
            } else if (date.contains("yesterday")){
                finishLong=24*3600000;
            }else if(!clock.isEmpty()){
                finishLong=0;
            }else {
                logFM.e("没有这种时间转换＝＝＝"+date);
                return null;
            }
            Date nowDate= new Date();
            long l = nowDate.getTime() - finishLong;
            result=new Date(l);
        }
        return formatAnswer.format(result);
    }
    
}
