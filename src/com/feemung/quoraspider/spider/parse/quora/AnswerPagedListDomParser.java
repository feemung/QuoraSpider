package com.feemung.quoraspider.spider.parse.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.FileUtils;
import com.feemung.quoraspider.Utils.TimeStamp;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.spider.parse.DomParser;
import com.feemung.quoraspider.spider.parse.RegexParser;
import com.feemung.quoraspider.spider.utils.HtmlParserUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by feemung on 16/4/27.
 */
public class AnswerPagedListDomParser  {
    private static LogFM logFM=LogFM.getInstance(AnswerPagedListDomParser.class);

    private static Task task;

    public static List<Answer> parser2(Element element,Task task)throws Exception{
        AnswerPagedListDomParser.task=task;
        logFM.stopFlag=true;
        List<Answer> answerList=new ArrayList<>();
        Elements pagedElements=element.getElementsByClass("pagedlist_item");
        String question=task.getURL().replace("https://www.quora.com/", "");
        Element answerElement =null;
                Iterator<Element> pagedIterator=pagedElements.iterator();
        while (pagedIterator.hasNext()){


               answerElement = pagedIterator.next();
              Answer answer=parserAnswer(answerElement);
            answer.setQuestion(question);
            if(answer!=null){
                answerList.add(answer);
            }

        }
        logFM.d(answerList.toString());
        return answerList;
    }

    public static Answer parserAnswer(Element answerElement){
        Answer answer = new Answer();
        try {

            Elements questionLinkEls=answerElement.getElementsByAttributeValue("class", "question_link");
            //logFM.d(element.text());
            if(questionLinkEls!=null) {
                String questionLink = questionLinkEls.first().attr("href");
                String questionId=questionLink.replace("/", "");
                answer.setQuestion(questionId);
            }
            Element upvotedCountElement=answerElement.getElementsByAttributeValue("class", "count").first();
             int upvotedCount =0;
            if(upvotedCountElement!=null){
                Element upvotedButElement=upvotedCountElement.parent();


                if(upvotedButElement.text().contains("Upvote")||upvotedButElement.text().contains("Upvoted")){

                    String countStr=upvotedCountElement.text();
                    upvotedCount = HtmlParserUtils.htmlCount(countStr);

                }

            }
            answer.setUpvotersCount(upvotedCount);
            Element answerHeaderElement = answerElement.getElementsByClass("AnswerHeader").first();
            if(answerHeaderElement==null){
                return null;
            }
            Element userElement = answerHeaderElement.getElementsByClass("user").first();
            if(userElement==null){
                answer.setAnswerUser("Anonymous");
            }else {
               // logFM.d(userElement.attr("href").replace("/profile/", ""));
                answer.setAnswerUser(userElement.attr("href").replace("/profile/", ""));
            }
            Element viewCountElement = answerHeaderElement.getElementsByClass("meta_num").first();
            String viewCountStr=(viewCountElement.text());
            int viewCount= HtmlParserUtils.htmlCount(viewCountStr);


            answer.setViewCount(viewCount);
            answer.setLastEditDate(getDatetime(answerElement.getElementsByClass("answer_permalink").first()));
            Element wordMapEl=answerElement.getElementsByAttributeValueContaining("id","expanded").first();
            answer.setContent(wordMapEl.html());
            List<String> listWord=getWordList(wordMapEl);
            answer.setAnswerSize(listWord.size());
            answer.setWordMap(getWordMap(listWord));
            answer.setContent(wordMapEl.html());
            answer.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));

            //logFM.d(answer);
        }catch (Exception e){
            try {
                FileUtils.saveFile("answer" + TimeStamp.defaultTimeFormat(new Date()) + ".html", answerElement.html(), false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        return answer;
    }



    public static List<String> getWordList(Element element){
        String str = element.text();
        List<String> list = RegexParser.parseToList(str, "[a-zA-Z]{2,}");
        return list;
    }
    public static Map<String,Integer> getWordMap(List<String> list) {
        Map<String, Integer> map = new TreeMap<>();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next();
            if (map.containsKey(temp)) {
                int count = map.get(temp) + 1;
                map.put(temp, count);
            } else {
                map.put(temp, 1);
            }
        }

        return map;
    }


    public static String getDatetime(Element element)throws Exception{

        String temp=element.text();


        return TimeStamp.defaultTimeFormat(temp);
    }
}
