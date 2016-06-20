package com.feemung.quoraspider.spider.parse.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.FileUtils;
import com.feemung.quoraspider.Utils.TimeStamp;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.UserInfo;
import com.feemung.quoraspider.spider.entry.*;
import com.feemung.quoraspider.spider.handler.WareHandler;
import com.feemung.quoraspider.spider.handler.quora.ProfileHandler;
import com.feemung.quoraspider.spider.parse.ParseContext;
import com.feemung.quoraspider.spider.parse.Parser;
import com.feemung.quoraspider.spider.parse.RegexParser;
import com.feemung.quoraspider.spider.utils.HtmlParserUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by feemung on 16/5/8.
 */
public class ProfileHtmlParser implements Parser{
    private LogFM logFM=LogFM.getInstance(ProfileHtmlParser.class);
    private List<Task> taskList=new ArrayList<>();
    private List<Answer> answerList=new ArrayList<>();
    private Task task;
    public ProfileHtmlParser(){
        logFM.stopFlag=true;
    }
    @Override
    public QuoraProfileHtmlData parse(Content content, Task task, ParseContext parseContext) {
        this.task=task;
        Element pageEl=Jsoup.parse(content.getCharset());
        //Element pageEl=Jsoup.parse(FileUtils.readFile("f.html"));
       //this.task=new HTMLTask();
        //this.task.setURL("https://www.quora.com/profile/Emily-Lock-1/followers");
        //task=this.task;
         UserInfo userInfo=new UserInfo();
         String realUid= RegexParser.parse(task.getURL(), "/profile/[\\w-]+").replace("/profile/", "");

        userInfo.setRealUid(realUid);
        logFM.d("realUid=",realUid);
        String userNickname=pageEl.getElementsByClass("ProfileNameAndSig")
                .first().getElementsByAttributeValue("class","user").first().text();
        logFM.d(userNickname);
        userInfo.setNickname(userNickname);

        Element leftElement=pageEl.getElementsByAttributeValueMatching("class","layout_[\\w]+_left").first();
        Iterator<Element> leftIter=leftElement.getElementsByAttributeValue("class","list_count").iterator();
        while (leftIter.hasNext()){
            Element leftItem=leftIter.next();
            String leftItemText=leftItem.text();
            String parentItemText=leftItem.parent().ownText();
            logFM.d(parentItemText);
            logFM.d(leftItem.text());
            if(parentItemText.contains("Answers")){
                userInfo.setAnswerCount(leftItemText.replace(",",""));
            }else  if(parentItemText.contains("Questions")){
                userInfo.setQuestionCount(leftItemText.replace(",", ""));
            }else if(parentItemText.contains("Followers")){
                userInfo.setFollowersCount(leftItemText.replace(",",""));
            }else if(parentItemText.contains("Following")){
                userInfo.setFollowingCount(leftItemText.replace(",",""));
            }else{
               // logFM.e("左侧栏解析出现未知类型");
            }
        }


        Elements viewElements=pageEl.getElementsByClass("stat");
       for(int i=0;i<viewElements.size();i++){
           Element element=viewElements.get(i);
           if(element.text().contains("Last 30 Days")){
               String last30DaysStr=element.getElementsByClass("total_count").first().text().replace(",","");
               logFM.d("30day=",last30DaysStr);
               userInfo.setAnswerViewsLast30Days(last30DaysStr);
           }else  if(element.text().contains("All Time")){
               String allTimeStr=element.getElementsByClass("total_count").first().text().replace(",","");
               logFM.d("All Time=",allTimeStr);
               userInfo.setAnswerViewsAllTime(allTimeStr);
           }else {
               //logFM.e("找不到viewS");
           }
       }

        Element centerElement=pageEl.getElementsByAttributeValueMatching("class", "layout_[\\w]+_center").first();

        if(task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+$")){
            userInfo.setAnswerMap(parserAnswers(centerElement));
        }else if(task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+/questions$")){
            userInfo.setQuestionMap(parserQuestions(centerElement));
        }else if(task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+/followers$")){
            userInfo.setFollowersMap(parserFollowers(centerElement));
        }else if(task.getURL().matches("^https://www\\.quora\\.com/profile/[\\w-]+/following$")){
            userInfo.setFollowingMap(parserFollowing(centerElement));
        }else {
            logFM.e("解析出现错误"+task.toString());
        }

        userInfo.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));


        QuoraProfileHtmlData data=new QuoraProfileHtmlData();
        data.setTask(task);
        data.setUserInfo(userInfo);
        data.setUrlList(taskList);
        data.setAnswerList(answerList);
        logFM.d(userInfo);
          return data;
    }

    @Override
    public void parse(Content content, Object object, Task task, ParseContext parseContext) {

    }


    private HashMap<String,Integer> parserAnswers(Element centerElement){
        String baseUrl= RegexParser.parse(task.getURL(), "[a-z]+://[^/]+");

        Elements centerElements=centerElement.getElementsByClass("pagedlist_item");
        Iterator<Element> iterator=centerElements.iterator();

        HashMap<String,Integer> questionLinkMap=new HashMap<>();
        while (iterator.hasNext()){
            Element element=iterator.next();


            Answer answer= AnswerElementParser.extractAnswer(element);

            //logFM.i(answer);
            if(answer==null){
                continue;
            }
            answerList.add(answer);

            questionLinkMap.put(answer.getQuestion(),answer.getUpvotersCount());
            String url=baseUrl+"/"+answer.getQuestion();
            Task t=new HTMLTask();
            t.setURL(url);
            taskList.add(t);
        }
        return questionLinkMap;
    }
    private HashMap<String,Integer> parserQuestions(Element centerElement){

        String baseUrl= RegexParser.parse(task.getURL(), "[a-z]+://[^/]+");

        Elements centerElements=centerElement.getElementsByClass("pagedlist_item");
        Iterator<Element> iterator=centerElements.iterator();

        HashMap<String,Integer> questionLinkMap=new HashMap<>();
        while (iterator.hasNext()){
            Element element=iterator.next();
            Element questionLinkEl=element.getElementsByClass("question_link").first();
            String question=questionLinkEl.attr("href").replace("/", "");

            Element followCountElement=element.getElementsByAttributeValue("class", "count").first();
            int followCount =0;
            if(followCountElement!=null){
                String countStr=followCountElement.text();
                followCount = HtmlParserUtils.htmlCount(countStr);

            }
            if(!questionLinkMap.containsKey(question)&&question.matches("^[\\w-]+$")) {
                questionLinkMap.put(question, followCount);
                logFM.d(question);
            }
            String questionLink=null;
            questionLink=baseUrl+"/"+question;

            Task t=new HTMLTask();
            t.setURL(questionLink);

            t.setPriority(followCount);
            if(!taskList.contains(t)) {
                taskList.add(t);
                logFM.d("link＝"+questionLink);
            }

        }
        return questionLinkMap;
    }
    public HashMap<String,Integer> parserFollowing(Element centerElement){

        String baseUrl= RegexParser.parse(task.getURL(), "[a-z]+://[^/]+");
        HashMap<String,Integer> userMap=new HashMap<>();
        Elements itemElements=centerElement.getElementsByClass("pagedlist_item");
        Iterator<Element> iterator=itemElements.iterator();

        Element element = null;
        while (iterator.hasNext()){
            try {
                 element = iterator.next();
                String userLink = element.getElementsByClass("ObjectCard-header").first()
                        .getElementsByAttributeValue("class", "user").first().attr("href");
                String userRealUid = userLink.replace("/profile/", "");

                String countStr = element.getElementsByAttributeValue
                        ("class", "ObjectCard-footer").first()
                        .getElementsByClass("count").first().text();
                int count = HtmlParserUtils.htmlCount(countStr);

                if (!userMap.containsKey(userRealUid)) {
                    userMap.put(userRealUid, count);
                    logFM.d("userRealUid=" + userRealUid);
                    logFM.d("count=" + count);
                }

                if (!userLink.contains("://")) {
                    userLink = baseUrl + userLink;
                }
                Task t = new HTMLTask();
                t.setURL(userLink);
                t.setPriority(count);
                if (!taskList.contains(t)) {
                    taskList.add(t);
                    //logFM.d("包含全部link＝" + userLink);
                }
            }catch (Exception e){
                if(!element.text().contains("Quora User")){
                    e.printStackTrace();

                    logFM.e(element.text());
                }
            }
        }
        logFM.d("userMap.size=",userMap.size());
        return userMap;
    }
    public HashMap<String,Integer> parserFollowers(Element centerElement){

        return parserFollowing(centerElement);
    }
    public  void test(){
        task=new HTMLTask();
        task.setURL("https://www.quora.com/profile/Alex-Suchman");
       // String baseUrl= RegexParser.parse(task.getURL(), "[a-z]+://[^/]+");

        Element pageElement=Jsoup.parse(FileUtils.readFile("answer3.html"));
        Element centerElement=pageElement.getElementsByAttributeValueMatching("class", "layout_[\\w]+_center").first();
       // logFM.d(centerElement.text());
        HTMLContent content=new HTMLContent();
        content.setPage(pageElement.html());
        Data d=parse(content, task, null);

       // WareHandler wareHandler= ProfileHandler.getInstance();
       // wareHandler.handleData(d);
       // parserFollowing(centerElement);
        //logFM.e(taskList.toString());

    }



}
