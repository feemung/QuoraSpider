package com.feemung.quoraspider.sql.wuyong;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.entry.HTMLTask;
import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.sql.SQLBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by feemung on 16/4/23.
 */
public class NotVisitedTableSQLUtils extends SQLBase{
    private static NotVisitedTableSQLUtils instance;

    public static NotVisitedTableSQLUtils getInstance() {
        if(instance==null){
            instance=new NotVisitedTableSQLUtils();
        }
        return instance;
    }


    private NotVisitedTableSQLUtils(){
        tableType="(id INT PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                "url VARCHAR(1024)NOT NULL UNIQUE," +
                "saveDate VARCHAR(20),"+//最后一次保存时间
                "previousUrl VARCHAR(1024),"+
                "priority VARCHAR(20)"+
                ")";

        tableName="QuoraNotVisitedTableUrl2";
        logFM= LogFM.getInstance(NotVisitedTableSQLUtils.class);
        logFM.stopFlag=false;
    }
    public void deleteTable(){
        super.deleteTable(tableName);
    }
    @Override
    public void insert(Object task){
        if(!(task instanceof Task)){
            Exception exception=new Exception("插入的类型出错,插入的类应该是task, 但实际插入的确是"+task);
            logFM.e(exception);
            return;
        }
        lock.lock();
        Task t=(Task)task;
        insertCount++;
        logFM.d(task);
        String sql = "insert into "+tableName
                + "(" +
                "url,saveDate,previousUrl,priority)values('"+
                t.getURL()+"','"+
                t.getSaveDate()+"','"+
                t.getPreviousUrl()+"','"+
                String.valueOf(t.getPriority())+
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



    public boolean contain(Task task){
        return containUrl(task.getURL());
    }

    public boolean containUrl(String url) {
        return super.contain("url", url);
    }
    public boolean deleteTask(Task task){
        return super.delete("url", task.getURL());
    }
    public boolean deleteUrl(String url){
        return super.delete("url", url);
    }
    public boolean deletePreviousUrl(String previousUrl){
        return super.delete("previousUrl", previousUrl);
    }
    public boolean deleteId(int id){
        return super.delete("id", String.valueOf(id));
    }
    public HTMLTask searchHTMLTask(int id){
        String sql="select * FROM "+tableName+"  WHERE id = '"+id+"'";
        ResultSet rs;
        HTMLTask task=new HTMLTask();
        try {
            rs=stmt.executeQuery(sql);
            if(!rs.next()){
                return null;
            }
            task.setURL(rs.getString("url"));
            task.setSaveDate(rs.getString("saveDate"));

            task.setPriority(Integer.valueOf(rs.getString("priority")));
            task.setPreviousUrl(rs.getString("previousUrl"));
        } catch (SQLException e) {
            logFM.e(e);
        }finally {

        }
        return task;
    }
    public HTMLTask searchUrl(String url){
        String sql="select * FROM "+tableName+"  WHERE url = '"+url+"'";
        ResultSet rs;
        HTMLTask task=new HTMLTask();
        try {
            rs=stmt.executeQuery(sql);
            if(!rs.next()){
                return null;
            }
            task.setURL(rs.getString("url"));
            task.setSaveDate(rs.getString("saveDate"));

            task.setPriority(Integer.valueOf(rs.getString("priority")));
            task.setPreviousUrl(rs.getString("previousUrl"));
        } catch (SQLException e) {
            logFM.e(e);
        }finally {

        }
        return task;
    }
    public ArrayList<HTMLTask> searchPreviousUrl(String previousUrl){
        String sql="select * FROM "+tableName+"  WHERE previousUrl = '"+previousUrl+"'";
        ResultSet rs;
        HTMLTask task=new HTMLTask();
        ArrayList<HTMLTask> list=new ArrayList<>();
        try {
            rs=stmt.executeQuery(sql);
            while(rs.next()){

                task.setURL(rs.getString("url"));
                task.setSaveDate(rs.getString("saveDate"));
                task.setPriority(Integer.valueOf(rs.getString("priority")));
                task.setPreviousUrl(rs.getString("previousUrl"));
                list.add(task);
            }

        } catch (SQLException e) {
            logFM.e(e);
        }finally {

        }
        return list;
    }
    public void addTask(Task task){
        if(contain(task)){
            update("url",task.getURL(),"previousUrl",task.getPreviousUrl());
            update("url",task.getURL(),"priority",String.valueOf(task.getPriority()));
            update("url",task.getURL(),"saveDate", task.getSaveDate());
        }else {
            insert(task);
        }
    }

}
