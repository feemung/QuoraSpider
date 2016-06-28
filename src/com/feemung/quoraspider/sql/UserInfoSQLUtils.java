package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.UserInfo;
import com.feemung.quoraspider.spider.entry.HTMLTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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
public class UserInfoSQLUtils extends SQLBase{
    private static UserInfoSQLUtils instance;

    public static UserInfoSQLUtils getInstance() {
        if(instance==null){
            instance=new UserInfoSQLUtils();
        }
        return instance;
    }

    private Lock lock=new ReentrantLock();

    private UserInfoSQLUtils(){

        tableType="(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
        "uid VarCHAR(255)," +
        "realUid VarCHAR(255)NOT NULL UNIQUE ," +
        "nickname VarCHAR(255) ,"+
        "sex VarCHAR(5) ," +//sex=1代表是女的；
        "followersMap LONGTEXT,"+
        "followingMap LONGTEXT,"+
        "answerMap LONGTEXT,"+
        "questionMap LONGTEXT,"+
        "highlightsMap LONGTEXT,"+

        "educationMap LONGTEXT,"+
        "locationMap LONGTEXT,"+
        "employmentMap  LONGTEXT,"+

        "userIcon VarCHAR(255)," +
        "answerViewsLast30Days VarCHAR(255)," +
        "answerViewsAllTime VarCHAR(255)," +
        "saveDate VARCHAR(20),"+

        "followersCount VARCHAR(255),"+
        "followingCount VARCHAR(255),"+
        "answerCount VARCHAR(255),"+
        "questionCount VARCHAR(255),"+
         "visitAnswerLastDate VARCHAR(20)"+
                ")";


        tableName="QuoraUserInfo";
        logFM= LogFM.getInstance(UserInfoSQLUtils.class);
        logFM.stopFlag=true;
    }
    public void deleteTable(){
        logFM.d("成功删除表" + tableName);
        super.deleteTable(tableName);
    }
    public void addColumn(String column,String columnType){
        super.addColumn(column, columnType);
    }

    public boolean contain(String realUid, String tag) {
        lock.lock();
        String sql="SELECT * FROM "+tableName+"  WHERE realUid = '"+realUid+"'";
        ResultSet rs=null;
        boolean flag=false;
        try {
            rs=stmt.executeQuery(sql);
            flag= rs.next();
            if(!flag){
                return false;
            }
            String result=rs.getString(tag);
            if(result!=null){
                flag=true;
            }
        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }
    public boolean contain(String realUid) {
        return super.contain("realUid",realUid);

    }
    public boolean delete(String realUid){
        lock.lock();
        String sql="DELETE FROM "+tableName+"  WHERE realUid  =  '"+realUid+"'";
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
    public UserInfo searchRealUid(String realUid){

        return searchUserInfo("realUid",realUid).get(0);
    }
    public UserInfo searchId(int id){
        return searchUserInfo("id",String.valueOf(id)).get(0);
    }
    public ArrayList<UserInfo> getAllData(){
        lock.lock();
        String sql="select * FROM "+tableName;
        ResultSet rs;
        ArrayList<UserInfo> list=new ArrayList<>();
        Gson gson=new Gson();
        try {
            rs=stmt.executeQuery(sql);
            while (rs.next()) {
                UserInfo userInfo=new UserInfo();
                userInfo.setRealUid(rs.getString("realUid"));
                userInfo.setUid(rs.getString("uid"));
                userInfo.setNickname(rs.getString("nickname"));
                userInfo.setSex(rs.getString("sex"));
                userInfo.setFollowersMap(gson.fromJson(rs.getString("followersMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setFollowingMap(gson.fromJson(rs.getString("followingMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setAnswerMap(gson.fromJson(rs.getString("answerMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setQuestionMap(gson.fromJson(rs.getString("questionMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setHighlightsMap(gson.fromJson(rs.getString("highlightsMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setEducationMap(gson.fromJson(rs.getString("educationMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setLocationMap(gson.fromJson(rs.getString("locationMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setEmploymentMap(gson.fromJson(rs.getString("employmentMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setUserIcon(rs.getString("userIcon"));
                userInfo.setAnswerViewsLast30Days(rs.getString("answerViewsLast30Days"));
                userInfo.setAnswerViewsAllTime(rs.getString("answerViewsAllTime"));
                userInfo.setFollowersCount(rs.getString("followersCount"));
                userInfo.setFollowingCount(rs.getString("followingCount"));
                userInfo.setAnswerCount(rs.getString("answerCount"));
                userInfo.setQuestionCount(rs.getString("questionCount"));
                list.add(userInfo);

            }
        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return list;
    }
    public ArrayList<UserInfo> searchUserInfo(String key,String value){
        lock.lock();
        String sql="select * FROM "+tableName+"  WHERE "+key+"  =  '"+value+"'";
        ResultSet rs;
        ArrayList<UserInfo> list=new ArrayList<>();
        Gson gson=new Gson();
        try {
            rs=stmt.executeQuery(sql);
            while (rs.next()) {
                UserInfo userInfo=new UserInfo();
                userInfo.setRealUid(rs.getString("realUid"));
                userInfo.setUid(rs.getString("uid"));
                userInfo.setNickname(rs.getString("nickname"));
                userInfo.setSex(rs.getString("sex"));
                userInfo.setFollowersMap(gson.fromJson(rs.getString("followersMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setFollowingMap(gson.fromJson(rs.getString("followingMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setAnswerMap(gson.fromJson(rs.getString("answerMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setQuestionMap(gson.fromJson(rs.getString("questionMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setHighlightsMap(gson.fromJson(rs.getString("highlightsMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setEducationMap(gson.fromJson(rs.getString("educationMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setLocationMap(gson.fromJson(rs.getString("locationMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setEmploymentMap(gson.fromJson(rs.getString("employmentMap"),  new TypeToken<HashMap<String, Integer>>() {}.getType()));
                userInfo.setUserIcon(rs.getString("userIcon"));
                userInfo.setAnswerViewsLast30Days(rs.getString("answerViewsLast30Days"));
                userInfo.setAnswerViewsAllTime(rs.getString("answerViewsAllTime"));
                userInfo.setFollowersCount(rs.getString("followersCount"));
                userInfo.setFollowingCount(rs.getString("followingCount"));
                userInfo.setAnswerCount(rs.getString("answerCount"));
                userInfo.setQuestionCount(rs.getString("questionCount"));
                list.add(userInfo);

            }
        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return list;
    }
    @Override
    public void insert(Object userInfo){
        if(!(userInfo instanceof UserInfo)){
            Exception exception=new Exception("插入的类型出错,插入的类应该是Answer, 但实际插入的确是"+userInfo);
            logFM.e(exception);
            return;
        }
        lock.lock();
        UserInfo an=(UserInfo)userInfo;

        insertCount++;
//        logFM.d(userInfo);
        String sql = "insert into "+tableName
                + "(" +
                "uid," +
                "realUid," +
                "nickname,"+
                "sex," +//sex=1代表是女的；
                "followersMap,"+
                "followingMap,"+
                "answerMap,"+
                "questionMap,"+
                "highlightsMap,"+

                "educationMap,"+
                "locationMap,"+
                "employmentMap ,"+

                "userIcon," +
                "answerViewsLast30Days," +
                "answerViewsAllTime," +
                "saveDate,"+

                "followersCount,"+
                "followingCount,"+
                "answerCount,"+
                "questionCount"+
                 ")values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Gson gson=new Gson();
        try {
        PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1,an.getUid());
            pstmt.setString(2, an.getRealUid());
            pstmt.setString(3, an.getNickname());
            pstmt.setString(4, an.getSex());
            pstmt.setString(5, gson.toJson(an.getFollowersMap()));
            pstmt.setString(6, gson.toJson(an.getFollowingMap()));
                pstmt.setString(7, gson.toJson(an.getAnswerMap()));
                pstmt.setString(8, gson.toJson(an.getQuestionMap()));
                pstmt.setString(9, gson.toJson(an.getHighlightsMap()));
                pstmt.setString(10, gson.toJson(an.getEducationMap()));
                pstmt.setString(11, gson.toJson(an.getLocationMap()));
                pstmt.setString(12, gson.toJson(an.getEmploymentMap()));
            pstmt.setString(13,an.getUserIcon() );
                pstmt.setString(14, an.getAnswerViewsLast30Days());
                pstmt.setString(15, an.getAnswerViewsAllTime());
            pstmt.setString(16, an.getSaveDate());
            pstmt.setString(17, an.getFollowersCount());
            pstmt.setString(18, an.getFollowingCount());
            pstmt.setString(19, an.getAnswerCount());
            pstmt.setString(20, an.getQuestionCount());




        boolean rss = pstmt.execute();

            logFM.d("insert 成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
    private void update(String readUid,String key,String value){
        lock.lock();
        String sql = " update "+tableName+" set "+key+"=? where realUid=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,value);
            pstmt.setString(2,readUid);

            boolean rss = pstmt.execute();

            logFM.d("insert 成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
    public void update(UserInfo user){
        String realUid=user.getRealUid();
        if(!contain(realUid)){
            insert(user);
        }else {
            Gson gson=new Gson();
            HashMap map=gson.fromJson(gson.toJson(user),HashMap.class);
            Iterator<String> iterator=map.keySet().iterator();
            while (iterator.hasNext()){
                String key=iterator.next();
                String value=map.get(key).toString();
                update(realUid,key,value);
            }

        }
    }


}
