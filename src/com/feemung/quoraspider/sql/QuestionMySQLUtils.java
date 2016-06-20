package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.TimeStamp;
import com.feemung.quoraspider.entry.Answer;
import com.feemung.quoraspider.entry.Question;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/23.
 */
public class QuestionMySQLUtils extends SQLBase{
    private static QuestionMySQLUtils instance;

    public static QuestionMySQLUtils getInstance() {
        if(instance==null){
            instance=new QuestionMySQLUtils();
        }
        return instance;
    }

    private Lock lock=new ReentrantLock();

    private QuestionMySQLUtils(){
        tableType="(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                "realId VARCHAR(1024)NOT NULL UNIQUE," +
                "content TEXT," +
                "topicList TEXT," +
                "saveDate VARCHAR(20))";
  tableName="QuoraQuestion";
        logFM= LogFM.getInstance(QuestionMySQLUtils.class);
        logFM.stopFlag=true;
    }
    public void deleteTable(){
        super.deleteTable(tableName);
    }

    public boolean containQuestion(Question question){
        return contain(question.getRealId());
    }

    public boolean contain(String realId) {
        lock.lock();
        String sql="SELECT * FROM "+tableName+"  WHERE realId = '"+realId+ "'";
        ResultSet rs;
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
    public boolean delete(String realId){
        lock.lock();
        String sql="DELETE FROM "+tableName+"  WHERE realId  =  '"+realId+"'";
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
    public Question search(String realId){
        lock.lock();
        String sql="select * FROM "+tableName+"  WHERE realId  =  '"+realId+"'";
        ResultSet rs;
        Question q=new Question();
        try {
            rs=stmt.executeQuery(sql);
            boolean flag=rs.next();
            if(!flag){
                return null;
            }
            q.setText(rs.getString("content"));
            q.setSaveDate(rs.getString("saveDate"));
            q.setTopicList(rs.getString("topicList"));
            q.setRealId(rs.getString("realId"));
        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return q;
    }
    @Override
    public void insert(Object question){
        if(!(question instanceof Question)){
            Exception exception=new Exception("插入的类型出错,插入的类应该是Question, 但实际插入的确是"+question);
            logFM.e(exception);
            return;
        }
        lock.lock();
        Question an=(Question)question;

        insertCount++;
        logFM.d(question);
        String sql = "insert into "+tableName
                + "(" +
                "realId,content,topicList,saveDate"+
                ")values(?,?,?,?)";
        try {
        PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,an.getRealId());
                pstmt.setString(2, an.getText());
            pstmt.setString(3, an.getTopicListStr());
            pstmt.setString(4, TimeStamp.defaultTimeFormat(new Date()));
            logFM.d(an.getText());
            boolean rss = pstmt.execute();

            logFM.d("insert ",rss);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }


}
