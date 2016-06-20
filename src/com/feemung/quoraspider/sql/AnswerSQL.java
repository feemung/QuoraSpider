package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.UserInfo;
import com.google.gson.Gson;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/23.
 */
public class AnswerSQL extends SQLBase{
    private static AnswerSQL instance;

    public static AnswerSQL getInstance() {
        if(instance==null){
            instance=new AnswerSQL();
        }
        return instance;
    }

    private Lock lock=new ReentrantLock();

    private AnswerSQL(){
        tableType="(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                "question VarCHAR(2048)NOT NULL ," +//问题
                "answerUser VARCHAR(2048)NOT NULL,"+//答案的作者
                "content LONGTEXT,"+//答案内容
                //"content LONGTEXT,"+//答案内容
                "lastEditDate VARCHAR(20),"+//最后修改的时间
                "upvotersCount VARCHAR(10),"+//点赞数量
                "upvotersUserList LONGTEXT,"+// 点赞的用户列表
                "viewCount VARCHAR(20),"+//浏览的次数
                "wordMap LONGTEXT,"+//单词出现的频率
                "saveDate VARCHAR(20)"+
                ")";

        /*
        tableType="(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,"// 主键
                + "uid VARCHAR(20)NOT NULL UNIQUE,"
                + "realUid VARCHAR(20)NOT NULL UNIQUE,"
                + "nickname LONGTEXT,"
                + "sex VARCHAR(3),"// 性别
                + "sexValue VARCHAR(3),"// 性别
                + "randAttr VARCHAR(50),"
                + "marriage VARCHAR(50),"
                + "height VARCHAR(10),"
                + "education VARCHAR(50),"
                + "income VARCHAR(50),"
                + "work_location VARCHAR(50),"
                + "work_sublocation VARCHAR(50),"
                + "age INT(3),"
                + "image VARCHAR(150),"
                + "count VARCHAR(10),"
                + "online INT(3),"
                + "randTag VARCHAR(50),"
                + "helloUrl VARCHAR(150),"
                + "sendMsgUrl VARCHAR(150),"
                + "shortnote TEXT(2000),"
                + "matchCondition VARCHAR(200))";
                */
        tableName="QuoraAnswer2";
        logFM= LogFM.getInstance(AnswerSQL.class);
        logFM.stopFlag=true;
    }
    public void deleteTable(){
        super.deleteTable(tableName);
    }
    public void insert2(Object answer){
        if(!(answer instanceof Answer)){
            Exception exception=new Exception("插入的类型出错,插入的类应该是Answer, 但实际插入的确是"+answer);
            logFM.e(exception);
            return;
        }
        lock.lock();
        Answer an=(Answer)answer;

        insertCount++;
        logFM.d(answer);
        String sql = "insert into "+tableName
                + "(" +
                "question,answerUser,content,lastEditDate,upvotersCount,upvotersUserList,viewCount,wordMap,saveDate"+
                ")values('"+

                an.getQuestion()+"','"+
                an.getAnswerUser()+"','"+
                an.getContent()+"','"+
                an.getLastEditDate()+"','"+
                an.getUpvotersCountStr()+"','"+
                an.getUpvotersUserListStr()+"','"+
                an.getViewCountStr()+"','"+
                an.getWordMapStr()+"','"+
                an.getSaveDate()+
                "')";

        try {
            int result = stmt.executeUpdate(sql);
            logFM.d("insert 成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public boolean containAnswer(Answer answer){
        return contain(answer.getQuestion(),answer.getAnswerUser());
    }

    public boolean contain(String question, String answerUser) {
        lock.lock();
        String sql="SELECT * FROM "+tableName+"  WHERE question = '"+question+
                    "'and answerUser='"+answerUser+"'";
        ResultSet rs=null;
        boolean flag=false;
        try {
            rs=stmt.executeQuery(sql);
            flag= rs.next();

        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }
    public boolean delete(String question, String answerUser){
        lock.lock();
        String sql="DELETE FROM "+tableName+"  WHERE question  =  '"+question+
                "'and answerUser='"+answerUser+"'";
        boolean flag=false;
        try {
            flag=stmt.execute(sql);


        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }
    public Answer searchAnswer(String question,String answerUser){
        lock.lock();
        String sql="select * FROM "+tableName+"  WHERE question  =  '"+question+
                "'and answerUser='"+answerUser+"'";
        ResultSet rs;
        Answer answer=new Answer();
        try {
            rs=stmt.executeQuery(sql);
            boolean flag=rs.next();
            if(!flag){
                return null;
            }
            answer.setQuestion(question);
            answer.setAnswerUser(answerUser);
            answer.setContent(rs.getString("content"));
            answer.setLastEditDate(rs.getString("lastEditDate"));
            answer.setUpvotersCountStr(rs.getString("upvotersCount"));
            answer.setSaveDate(rs.getString("saveDate"));
            answer.setViewCountStr(rs.getString("viewCount"));
            answer.setUpvotersUserList(rs.getString("upvotersUserList"));
            answer.setWordMap(rs.getString("wordMap"));

        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return answer;
    }
    @Override
    public void insert(Object answer){
        if(!(answer instanceof Answer)){
            Exception exception=new Exception("插入的类型出错,插入的类应该是Answer, 但实际插入的确是"+answer);
            logFM.e(exception);
            return;
        }
        lock.lock();
        Answer an=(Answer)answer;

        insertCount++;
        logFM.d(answer);
        String sql = "insert into "+tableName
                + "(" +
                "question,answerUser,content,lastEditDate,upvotersCount,upvotersUserList,viewCount,wordMap,saveDate"+
                ")values(?,?,?,?,?,?,?,?,?)";
        try {
        PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,an.getQuestion());
                pstmt.setString(2, an.getAnswerUser());
                pstmt.setString(3, an.getContent());
                pstmt.setString(4, an.getLastEditDate());
                pstmt.setString(5, an.getUpvotersCountStr());
                pstmt.setString(6, an.getUpvotersUserListStr());
                pstmt.setString(7, an.getViewCountStr());
                pstmt.setString(8, an.getWordMapStr());
                pstmt.setString(9, an.getSaveDate());




        boolean rss = pstmt.execute();

            logFM.d("insert 成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
    public ArrayList<Answer> searchAnswerUser(String answerUser){
        return searchAnser("answerUser",answerUser);
    }
    public ArrayList<Answer> searchQuestionUser(String question){
        return searchAnser("answerUser",question);
    }
    public ArrayList<Answer> searchAnser(String key,String value){
        lock.lock();
        ArrayList<Answer> answerArrayList=new ArrayList<>();
        String sql="select * FROM "+tableName+"  WHERE "+key+"  =  '"+value+"'";
        ResultSet rs;

        try {
            rs=stmt.executeQuery(sql);
            while(rs.next()) {
                Answer answer=new Answer();
                answer.setQuestion(rs.getString("question"));
                answer.setAnswerUser(rs.getString("answerUser"));
                answer.setContent(rs.getString("content"));
                answer.setLastEditDate(rs.getString("lastEditDate"));
                answer.setUpvotersCountStr(rs.getString("upvotersCount"));
                answer.setSaveDate(rs.getString("saveDate"));
                answer.setViewCountStr(rs.getString("viewCount"));
                answer.setUpvotersUserList(rs.getString("upvotersUserList"));
                answer.setWordMap(rs.getString("wordMap"));
                answerArrayList.add(answer);
            }
        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return answerArrayList;
    }

    public void update(Answer answer){
        //logFM.i(answer.getAnswerUser());
        if(answer.getAnswerUser()==null||"null".equals(answer.getAnswerUser())){
            logFM.i(answer.toString());
        }
        if(!containAnswer(answer)){
            insert(answer);
        }else {
            Gson gson=new Gson();
            HashMap map=gson.fromJson(gson.toJson(answer),HashMap.class);
            Iterator<String> iterator=map.keySet().iterator();
            while (iterator.hasNext()){
                String key=iterator.next();
                String value=map.get(key).toString();
                if(value==null){
                    continue;
                }
                update("question",answer.getQuestion(),"answerUser",answer.getAnswerUser(),key,value);
            }

        }
    }

}
