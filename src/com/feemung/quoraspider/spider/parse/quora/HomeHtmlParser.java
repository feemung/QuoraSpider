package com.feemung.quoraspider.spider.parse.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.FileUtils;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.spider.entry.*;
import com.feemung.quoraspider.spider.handler.WareHandler;
import com.feemung.quoraspider.spider.handler.quora.HomeHandler;
import com.feemung.quoraspider.spider.parse.ParseContext;
import com.feemung.quoraspider.spider.parse.Parser;
import com.feemung.quoraspider.spider.parse.RegexParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by feemung on 16/5/20.
 */
public class HomeHtmlParser implements Parser {
    private LogFM logFM=LogFM.getInstance(HomeHtmlParser.class);
    private Task task;
    private List<Task> taskList=new ArrayList<>();
    public HomeHtmlParser(){
        logFM.stopFlag=true;
    }
    @Override
    public QuoraHomeHtmlData parse(Content content, Task task, ParseContext parseContext) {
        this.task=task;
        Element pageEl= Jsoup.parse(content.getCharset());
        Element centerElement=pageEl.getElementsByAttributeValueMatching("class", "layout_[\\w]+_center").first();
        ArrayList<Answer> arrayList=parserAnswers(centerElement);
        QuoraHomeHtmlData htmlData=new QuoraHomeHtmlData();
        htmlData.setAnswerList(arrayList);
        htmlData.setTask(task);
        htmlData.setUrlList(taskList);
        return htmlData;


    }


    @Override
    public void parse(Content content, Object object, Task task, ParseContext parseContext) {

    }

    private ArrayList<Answer> parserAnswers(Element centerElement){
        String baseUrl= RegexParser.parse(task.getURL(), "[a-z]+://[^/]+");
        ArrayList<Answer> answerList=new ArrayList<>();
        Elements centerElements=centerElement.getElementsByClass("pagedlist_item");
        Iterator<Element> iterator=centerElements.iterator();
        while (iterator.hasNext()){
            Element element=iterator.next();

            Answer answer= AnswerElementParser.extractAnswer(element);
            if(answer==null){
                continue;
            }
            logFM.d(answer);
            answerList.add(answer);

            String url=baseUrl+"/"+answer.getQuestion();
            Task t=new HTMLTask();
            t.setURL(url);
            taskList.add(t);
            String url2=baseUrl+"/profile/"+answer.getAnswerUser();
            Task t2=new HTMLTask();
            t2.setURL(url2);
            taskList.add(t2);

            String url3=baseUrl+"/profile/"+answer.getAnswerUser()+"/following";
            Task t3=new HTMLTask();
            t2.setURL(url3);
            taskList.add(t3);


        }

        return answerList;
    }

    public void test(){
        task=new HTMLTask();
        task.setURL("https://www.quora.com/");
        // String baseUrl= RegexParser.parse(task.getURL(), "[a-z]+://[^/]+");

        Element pageElement=Jsoup.parse(FileUtils.readFile("pt4.html"));
        Element centerElement=pageElement.getElementsByAttributeValueMatching("class", "layout_[\\w]+_center").first();
        // logFM.d(centerElement.text());
        HTMLContent content=new HTMLContent();
        content.setPage(pageElement.html());
        Data d=parse(content, task, null);

        WareHandler wareHandler= HomeHandler.getInstance();
        wareHandler.handleData(d);
        // parserFollowing(centerElement);
        //logFM.e(taskList.toString());

    }
}
