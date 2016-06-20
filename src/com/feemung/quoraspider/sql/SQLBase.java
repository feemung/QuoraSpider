package com.feemung.quoraspider.sql;


import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.UserInfo;
import com.feemung.quoraspider.spider.entry.Task;
import com.google.gson.Gson;

import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/13.
 */
public abstract   class SQLBase {
    protected String tableName="jiayuanuser";
    protected String tableType="";
    protected Connection conn = null;
    public boolean isConnection;
    public static int insertCount=0;
    protected Lock lock=new ReentrantLock();

    protected LogFM logFM= LogFM.getInstance(SQLBase.class);
    private final String url = "jdbc:mysql://localhost:3306/feemung?"
            + "user=root&password=root&useUnicode=true&characterEncoding=UTF8&useSSL=true";
    protected Statement stmt;
    public SQLBase(){}

    private void createTable()throws Exception{
        String sql = "create table if not exists " + tableName+
                tableType
                + "ENGINE=InnoDB DEFAULT CHARSET=utf8";
        int result = stmt.executeUpdate(sql);// executeUpdate语句会返回一个受影响的行数，如果返回-1就没有成功
        if (result != -1) {
            //logFM.i("创建数据表成功");
        }
    }
    public void connectionMysql() throws Exception {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            logFM.d("成功加载MySQL驱动程序");
            // 一个Connection代表一个数据库连接
            conn = DriverManager.getConnection(url);
            // Statement里面带有很多方法，比如executeUpdate可以实现插入，更新和删除等
           // stmt = conn.createStatement();
            stmt = conn.createStatement();
            logFM.i("创建数据库连接成功！");
            isConnection=true;
            createTable();

        } catch (SQLException e) {
            logFM.e("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }
    public void close(){
        isConnection=false;
        try {
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void printTableDate(){
        try {
            String sql = "select * from " + tableName;
            ResultSet rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值

            ResultSetMetaData resultSetMetaData=rs.getMetaData();
            List<String> list=new ArrayList<>();
            while (rs.next()) {

                for(int i=1;i<resultSetMetaData.getColumnCount()+1;i++){

                    logFM.i(resultSetMetaData.getColumnName(i)+"="+rs.getString(i)+";");
                   // logFM.i("="+rs.getString(i)+";");

                }
                logFM.i("-------------------------------");
                if(rs.isLast()){
                    break;
                }
            }
            System.out.println(list.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public abstract void insert(Object obje);
    public void deleteTable(String tableName){
        try {
            stmt.execute("DROP TABLE " + tableName);
        } catch (SQLException e) {
            logFM.e(e);
        }
        logFM.i("delete table "+tableName);
    }
    public void addColumn(String column,String columnType){
        try {
            stmt.execute("alter table " + tableName + " add column " + column + " " + columnType + ";");
        } catch (SQLException e) {
            logFM.e(e);
        }
        logFM.i("add table "+tableName," 列 ",column," 类型 ",columnType);
    }



    protected boolean delete(String key,String value){
        lock.lock();
        String sql="DELETE FROM "+tableName+"  WHERE "+key+"  =  '"+value+"'";
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
    protected boolean contain(String key,String value) {
        lock.lock();
        String sql="SELECT * FROM "+tableName+"  WHERE "+key+"  =  ?";
        ResultSet rs;
        boolean flag=false;
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,value);
            rs=pstmt.executeQuery();
            if(rs.next()){
                return true;
            }
        } catch (SQLException e) {
            logFM.e(e);
        }finally {
            lock.unlock();
        }
        return flag;
    }
    public void update(String whereKey,String whereValue,String key,String value){
        lock.lock();
        String sql = " update "+tableName+" set "+key+"=? where "+whereKey+"=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,value);
            pstmt.setString(2,whereValue);

            boolean rss = pstmt.execute();

            logFM.d("update 成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }
    protected void update(String whereKey1,String whereValue1,String whereKey2,String
            whereValue2,String key,String value){
        lock.lock();
        String sql = " update "+tableName+" set "+key+"=? where "+whereKey1+"=? and "+whereKey2+"=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1,value);
            pstmt.setString(2,whereValue1);
            pstmt.setString(3,whereValue2);

            boolean rss = pstmt.execute();

            logFM.d("update 成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }



}
