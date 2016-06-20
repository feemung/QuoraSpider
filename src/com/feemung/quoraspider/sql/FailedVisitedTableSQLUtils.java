package com.feemung.quoraspider.sql;

import com.feemung.quoraspider.Log.LogFM;

/**
 * Created by feemung on 16/4/23.
 */
public class FailedVisitedTableSQLUtils extends UrlTableSQLUtils{
    private static FailedVisitedTableSQLUtils instance;

    public static FailedVisitedTableSQLUtils getInstance() {
        if(instance==null){
            instance=new FailedVisitedTableSQLUtils();
        }
        return instance;
    }


    private FailedVisitedTableSQLUtils(){
        super();
        tableName="QuoraFailedVisitedTableUrl2";
        logFM= LogFM.getInstance(FailedVisitedTableSQLUtils.class);
        logFM.stopFlag=true;
    }


}
