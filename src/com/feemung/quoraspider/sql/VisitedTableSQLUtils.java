package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.entry.Answer;
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
public class VisitedTableSQLUtils extends UrlTableSQLUtils{
    private static VisitedTableSQLUtils instance;

    public static VisitedTableSQLUtils getInstance() {
        if(instance==null){
            instance=new VisitedTableSQLUtils();
        }
        return instance;
    }

    private Lock lock=new ReentrantLock();

    private VisitedTableSQLUtils(){
        super();

        tableName="QuoraVisitedTableUrl2";
        logFM= LogFM.getInstance(VisitedTableSQLUtils.class);
        logFM.stopFlag=true;
    }

}
