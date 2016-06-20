package com.feemung.quoraspider.spider.exception;

/**
 * Created by feemung on 16/5/23.
 */
public class ExceptionManager {
    private static ExceptionManager instance;

    public static ExceptionManager getInstance() {
        if(instance==null){
            instance=new ExceptionManager();
        }
        return instance;

    }
    public void add(Exception e){

    }
}
