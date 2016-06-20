package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.Utils.TimeStamp;
import com.feemung.quoraspider.entry.UserInfo;
import com.feemung.quoraspider.spider.entry.HTMLTask;
import com.feemung.quoraspider.spider.entry.Task;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by feemung on 16/4/23.
 */
public class NotVisitedTableSQLUtils extends UrlTableSQLUtils{
    private static NotVisitedTableSQLUtils instance;

    public static NotVisitedTableSQLUtils getInstance() {
        if(instance==null){
            instance=new NotVisitedTableSQLUtils();
        }
        return instance;
    }


    private NotVisitedTableSQLUtils(){
        super();
        tableName="QuoraNotVisitedTableUrl2";
        logFM= LogFM.getInstance(NotVisitedTableSQLUtils.class);
        logFM.stopFlag=true;
    }


}
