package com.feemung.quoraspider.spider.handler.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.Question;
import com.feemung.quoraspider.entry.UserInfo;
import com.feemung.quoraspider.spider.entry.Data;
import com.feemung.quoraspider.spider.entry.QuoraProfileHtmlData;
import com.feemung.quoraspider.spider.entry.QuoraQuestionHtmlData;
import com.feemung.quoraspider.spider.handler.WareHandler;
import com.feemung.quoraspider.sql.AnswerSQL;
import com.feemung.quoraspider.sql.CheckSQLUpdateData;
import com.feemung.quoraspider.sql.QuestionMySQLUtils;
import com.feemung.quoraspider.sql.UserInfoSQLUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/29.
 */
public class ProfileHandler extends WareHandler{
    private static ProfileHandler instance;

    public static ProfileHandler getInstance() {
        if(instance==null){
            instance=new ProfileHandler();
        }
        return instance;
    }
    private LogFM logFM=LogFM.getInstance(ProfileHandler.class);
    private UserInfoSQLUtils sql;
    private AnswerSQL answerSQL;
    private Lock lock;
    public ProfileHandler(){
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void init()throws Exception{
        lock=new ReentrantLock();
        sql=UserInfoSQLUtils.getInstance();
        sql.connectionMysql();

        answerSQL=AnswerSQL.getInstance();
        answerSQL.connectionMysql();
    }
    @Override
    public void handleData(Data quoraProfileHtmlData) {
        lock.lock();
        QuoraProfileHtmlData data=(QuoraProfileHtmlData)quoraProfileHtmlData;
        UserInfo userInfo=data.getUserInfo();
        sql.update(userInfo);
        List<Answer> answerList=data.getAnswerList();
        Iterator<Answer> iterator=answerList.iterator();
        while (iterator.hasNext()) {
            Answer answer=iterator.next();
            if(!CheckSQLUpdateData.checkAnswer(answer)) {
                logFM.e("数据库更新answer内容出错，"+answer.toString());
            }
                answerSQL.update(answer);

        }

       // logFM.e("成功更新用户数据"+data.getTask());
        if(!CheckSQLUpdateData.checkUserInfo(userInfo)){
            logFM.e("数据库更新answer内容出错，"+userInfo.toString());
        }
        logFM.d(userInfo.toString());
        lock.unlock();
    }
}
