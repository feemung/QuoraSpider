package com.feemung.quoraspider.spider.entry;

import java.util.Comparator;

/**
 * Created by feemung on 16/5/9.
 */
public class TaskComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        int a=((Task)o1).getPriority();
        int b=((Task)o2).getPriority();
        if(a>b){
            return -1;
        }else if(a<b){
            return 1;
        }else {
            return 0;
        }
    }
}
