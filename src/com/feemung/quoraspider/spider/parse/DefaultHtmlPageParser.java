package com.feemung.quoraspider.spider.parse;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.spider.entry.*;
import com.feemung.quoraspider.spider.parse.quora.AnswerPagedListDomParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by feemung on 16/4/27.
 */
public class DefaultHtmlPageParser extends HTMLParser {
    private List<String> notLinkList=new ArrayList<>();
    private LogFM logFM=LogFM.getInstance(DefaultHtmlPageParser.class);
    public DefaultHtmlPageParser(){
        notLinkList.add("#");
        notLinkList.add("/");
        notLinkList.add("javascript");
    }

    @Override
    public DefaultHtmlData parse(Content content, Task task, ParseContext parseContext) throws Exception {
        DefaultHtmlData pageData=new DefaultHtmlData();
        Element pageElement=Jsoup.parse(content.getCharset());
        Elements linkElements=pageElement.getElementsByAttribute("href");
        Iterator<Element> iterator=linkElements.iterator();
        List<Task> linkList=new ArrayList<>();
        Object[] notLinkStr=notLinkList.toArray();
        String host= RegexParser.parse(task.getURL(),"[a-z]+://[^/]*");
        while (iterator.hasNext()){
            String link=iterator.next().attr("href");

                if(link.contains("#")|link.contains("javascript")){

                    continue;
                }

                if(!link.contains("://")){
                    link=host+link;

                }
                Task t=new HTMLTask();
                linkList.add(t);



        }
        pageData.setUrlList(linkList);

        return pageData;
    }
}
