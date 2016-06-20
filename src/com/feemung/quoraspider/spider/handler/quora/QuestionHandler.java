package com.feemung.quoraspider.spider.handler.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.FileUtils;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.Question;
import com.feemung.quoraspider.spider.entry.Data;
import com.feemung.quoraspider.spider.entry.QuoraQuestionHtmlData;
import com.feemung.quoraspider.spider.handler.WareHandler;
import com.feemung.quoraspider.sql.AnswerSQL;
import com.feemung.quoraspider.sql.QuestionMySQLUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/29.
 */
public class QuestionHandler extends WareHandler{
    private static QuestionHandler instance;

    public static QuestionHandler getInstance() {
        if(instance==null){
            instance=new QuestionHandler();
        }
        return instance;
    }
    private LogFM logFM=LogFM.getInstance(QuestionHandler.class);
    private AnswerSQL answerSQL;
    private QuestionMySQLUtils questionMySQL;
    private Lock lock;
    public QuestionHandler(){
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void init()throws Exception{
        lock=new ReentrantLock();
         answerSQL=AnswerSQL.getInstance();
        answerSQL.connectionMysql();
        questionMySQL=QuestionMySQLUtils.getInstance();
        questionMySQL.connectionMysql();
    }
    @Override
    public void handleData(Data questionHtmlData) {
        lock.lock();
        QuoraQuestionHtmlData data=(QuoraQuestionHtmlData)questionHtmlData;
        Question question=data.getQuestion();
        if(!questionMySQL.containQuestion(question)){
            questionMySQL.insert(question);
            logFM.e("insert question "+question.getRealId());
        }
        List<Answer> answerList=data.getAnswerList();
        Iterator<Answer> answerIterator=answerList.iterator();
        while (answerIterator.hasNext()){
            Answer answer=answerIterator.next();
            if(!answerSQL.containAnswer(answer)) {
                answerSQL.insert(answer);
                logFM.e("insert sql "+answer.getQuestion()+"/"+answer.getAnswerUser());
            }
        }
        lock.unlock();
    }
}
