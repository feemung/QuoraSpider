package com.feemung.quoraspider.spider.parse.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.TimeStamp;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.spider.parse.RegexParser;
import com.feemung.quoraspider.spider.utils.HtmlParserUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

/**
 * Created by feemung on 16/5/26.
 */
public class AnswerElementParser {
    private static LogFM logFM=LogFM.getInstance(AnswerElementParser.class);
    public static Answer extractAnswer(Element answerElement){
        logFM.stopFlag=true;
        Answer answer = new Answer();
        try {
            String questionId=extractQuestion(answerElement);
            answer.setQuestion(questionId);

            int upvotedCount=extractUpvote(answerElement);
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
            int viewCount=extractViewCount(answerHeaderElement);
            answer.setViewCount(viewCount);
            try {
            String lastEditDate=extractLastEditDate(answerElement);
            answer.setLastEditDate(lastEditDate);

                Element wordMapEl = answerElement.getElementsByAttributeValueMatching("id", "__w2_[\\w]+__expanded").first().getElementsByClass("rendered_qtext").first();

                answer.setContent(wordMapEl.html());
                List<String> listWord = getWordList(wordMapEl);
                answer.setAnswerSize(listWord.size());
                answer.setWordMap(getWordMap(listWord));
                answer.setContent(wordMapEl.html());
                answer.setSaveDate(TimeStamp.defaultTimeFormat(new Date()));
            }catch (Exception e){

            }

            logFM.d(answer);
            logFM.d(answer.getUpvotersCountStr(),"===",answer.getQuestion(),"  ",answer.getContent());
        }catch (Exception e){
            try {
                //FileUtils.saveFile("answer" + TimeStamp.defaultTimeFormat(new Date()) + ".html", answerElement.html(), false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return null;
        }
        //logFM.d(answer);
        return answer;
    }
    public static String extractQuestion(Element questionAncestorElement){
        Elements questionLinkEls=questionAncestorElement.getElementsByAttributeValue("class", "question_link");
        logFM.d(questionLinkEls.text());
        if(questionLinkEls!=null) {
            String questionLink = questionLinkEls.first().attr("href");
            String questionId=questionLink.replace("/", "");
            logFM.d(questionLink);
            return questionId;

        }else {
            return null;
        }
    }
    public static int extractUpvote(Element questionAncestorElement){
        Element upvoteCountElement=questionAncestorElement.getElementsByAttributeValue("class", "count").first();
        int upvoteCount =0;
        if(upvoteCountElement!=null){
            Element upvoteButElement=upvoteCountElement.parent();


            if(upvoteButElement.text().contains("Upvote")||upvoteButElement.text().contains("Upvoted")){

                String countStr=upvoteCountElement.text();
                upvoteCount = HtmlParserUtils.htmlCount(countStr);

            }

        }
        return upvoteCount;
    }
    public static int extractViewCount(Element viewCountAncestorElement){
        Element viewCountElement = viewCountAncestorElement.getElementsByClass("meta_num").first();
        String viewCountStr=(viewCountElement.text());
        int viewCount= HtmlParserUtils.htmlCount(viewCountStr);
        return viewCount;
    }
    public static String extractLastEditDate(Element lastEditDateAncestorElement)throws Exception{
        Element element=lastEditDateAncestorElement.getElementsByClass("answer_permalink").first();

        String temp=element.text();


        return TimeStamp.defaultTimeFormat(temp);
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
}
