package com.feemung.quoraspider.Log;

import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by feemung on 16/4/13.
 */
public class LogFM {
    private static Map<String,LogFM> map=new TreeMap<>();

    public synchronized static LogFM getInstance(Class c) {
        if(map.containsKey(c.getName())){
            return map.get(c.getName());
        }else {
            LogFM logFM=new LogFM(c.getName());
             map.put(c.getName(),logFM);
            return logFM;
        }

    }
    private String className;
    public boolean stopFlag=false;
    private PrintStream printStream;
    private LogFM(String name){
        className=name;
    }
    private String tagInfo(){
        StackTraceElement ste[]=Thread.currentThread().getStackTrace();
        for(int i=0;i<ste.length;i++){

            if(ste[i].isNativeMethod()){
                // System.out.println("Native=");
                continue;
            }
            if(ste[i].getClassName().equals(Thread.class.getName())){
                // System.out.println("thread=");
                continue;
            }
            if(ste[i].getClassName().equals(this.getClass().getName())){
                continue;
            }
            //return ste[i].toString();
            return "["+Thread.currentThread().getName()+"] ."+ste[i].getMethodName() + "("+ste[i].getFileName()+":"+ste[i].getLineNumber()+")"+">>>>";
        }
        return "";
    }
    public synchronized void d(Object ...obj){
        if(stopFlag){
            return;
        }
       // System.out.print(tagInfo().replace("com.feemung.quoraspider.Main.",""));
        System.out.print(tagInfo());
        for(Object o:obj){
            System.out.print(o);
        }
        System.out.println();
    }
    public synchronized void i(Object ...obj){
        System.out.print(tagInfo());
        for(Object o:obj){
            System.out.print(o);
        }
        System.out.println();
    }
    public synchronized void e(Exception ex){
        System.out.print(tagInfo());
        ex.printStackTrace();
        System.out.println();
    }
    public synchronized void e(String ex){
        System.err.println(tagInfo()+ex);

    }

}
