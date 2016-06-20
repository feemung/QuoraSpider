package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.Log.LogFM;

/**
 * Created by feemung on 16/4/23.
 */
public class FailedVisitedTableSQLUtils2 extends UrlTableSQLUtils{
    private static FailedVisitedTableSQLUtils2 instance;

    public static FailedVisitedTableSQLUtils2 getInstance() {
        if(instance==null){
            instance=new FailedVisitedTableSQLUtils2();
        }
        return instance;
    }


    private FailedVisitedTableSQLUtils2(){
        super();
        tableName="QuoraFailedVisitedTableUrl22";
        logFM= LogFM.getInstance(FailedVisitedTableSQLUtils2.class);
        logFM.stopFlag=true;
    }


}
