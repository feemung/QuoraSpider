package com.feemung.quoraspider.spider.handler.quora;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.UserInfo;
import com.feemung.quoraspider.spider.entry.Data;
import com.feemung.quoraspider.spider.entry.QuoraHomeHtmlData;
import com.feemung.quoraspider.spider.entry.QuoraProfileHtmlData;
import com.feemung.quoraspider.spider.handler.WareHandler;
import com.feemung.quoraspider.sql.AnswerSQL;
import com.feemung.quoraspider.sql.UserInfoSQLUtils;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/29.
 */
public class HomeHandler extends WareHandler{
    private static HomeHandler instance;

    public static HomeHandler getInstance() {
        if(instance==null){
            instance=new HomeHandler();
        }
        return instance;
    }
    private LogFM logFM=LogFM.getInstance(HomeHandler.class);
    private AnswerSQL answerSQL;
    private Lock lock;
    public HomeHandler(){
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
    }
    @Override
    public void handleData(Data data) {
        lock.lock();
        QuoraHomeHtmlData d=(QuoraHomeHtmlData)data;
        List<Answer> answerList=d.getAnswerList();
        Iterator<Answer> iterator=answerList.iterator();
        while (iterator.hasNext()) {
            Answer key=iterator.next();
            answerSQL.update(key);
            logFM.i(key.toString());
        }

        logFM.e("成功更新用户数据"+data.getTask());
        lock.unlock();
    }
}
