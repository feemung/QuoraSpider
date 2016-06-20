package com.feemung.quoraspider.spider.parse;

import com.feemung.quoraspider.spider.entry.Content;
import com.feemung.quoraspider.spider.entry.Data;
import com.feemung.quoraspider.spider.entry.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by feemung on 16/4/24.
 */
public class RegexParser implements Parser {
    @Override
    public Data parse(Content content, Task task, ParseContext parseContext) {
        return null;
    }

    @Override
    public void parse(Content content, Object object, Task task, ParseContext parseContext) {

    }
    public static String parse(String text,String regex){
          Pattern pattern=Pattern.compile(regex, Pattern.DOTALL);
         // Pattern pattern=Pattern.compile(regex, 2 | Pattern.DOTALL);
        Matcher matcher=pattern.matcher(text);
        StringBuffer result=new StringBuffer();
        while (matcher.find()){
            result.append(matcher.group(0));
        }
        return result.toString();
    }
    public static List<String> parseToList(String text,String regex){
        Pattern pattern=Pattern.compile(regex, 2 | Pattern.DOTALL);
        Matcher matcher=pattern.matcher(text);
        List<String> stringList=new ArrayList<>();
        while (matcher.find()){
            stringList.add(matcher.group(0));
        }
        return stringList;
    }
}
