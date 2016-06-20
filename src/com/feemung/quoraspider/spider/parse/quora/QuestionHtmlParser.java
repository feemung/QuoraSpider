package com.feemung.quoraspider.spider.parse.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.Question;
import com.feemung.quoraspider.spider.entry.*;
import com.feemung.quoraspider.spider.parse.HTMLParser;
import com.feemung.quoraspider.spider.parse.ParseContext;
import com.feemung.quoraspider.spider.parse.RegexParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by feemung on 16/4/27.
 */
public class QuestionHtmlParser extends HTMLParser {
    private List<Answer> answerList=new ArrayList<>();
    private List<String> notLinkList=new ArrayList<>();

    private LogFM logFM=LogFM.getInstance(QuestionHtmlParser.class);
    public QuestionHtmlParser(){
        notLinkList.add("#");
        notLinkList.add("/");
        notLinkList.add("javascript");
    }

    @Override
    public QuoraQuestionHtmlData parse(Content content, Task task, ParseContext parseContext) throws Exception {
        QuoraQuestionHtmlData questionHtmlData=new QuoraQuestionHtmlData();
        Element pageElement=Jsoup.parse(content.getCharset());
        Element answerListElement= pageElement.getElementsByClass("AnswerListDiv").first();

        List<Answer> list=parserAnswer(answerListElement, task);
        questionHtmlData.setAnswerList(list);
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
            t.setURL(link);
            if(!linkList.contains(t)) {

                linkList.add(t);
            }
        }

        Question question=parserQuestion(pageElement);
        question.setRealId(task.getURL().replace("https://www.quora.com/", ""));

        questionHtmlData.setUrlList(linkList);
        questionHtmlData.setQuestion(question);
        return questionHtmlData;
    }
    public Question parserQuestion(Element pageElement){
        Element questionElement= pageElement.getElementsByClass("QuestionArea").first();
    Elements topicEl= questionElement.select("a");
        Iterator<Element>iterator=topicEl.iterator();
        Question question=new Question();
        List<String> topicList=new ArrayList<>();
        while (iterator.hasNext()){
            Element element=iterator.next();
            if(element.attr("class").equals("TopicNameLink HoverMenu topic_name")){
                topicList.add(element.attr("href").replace("/topic/", ""));
            }
        }
        question.setTopicList(topicList);
        question.setText(questionElement.html());
        return question;
    }
    public  List<Answer> parserAnswer(Element element,Task task)throws Exception{

        logFM.stopFlag=true;
        List<Answer> answerList=new ArrayList<>();
        Elements pagedElements=element.getElementsByClass("pagedlist_item");
        String question=task.getURL().replace("https://www.quora.com/", "");
        Element answerElement =null;
        Iterator<Element> pagedIterator=pagedElements.iterator();
        while (pagedIterator.hasNext()){


            answerElement = pagedIterator.next();
            Answer answer= AnswerElementParser.extractAnswer(answerElement);
            answer.setQuestion(question);
            if(answer!=null){
                answerList.add(answer);
            }

        }
        logFM.d(answerList.toString());
        return answerList;
    }
}
