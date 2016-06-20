package com.feemung.quoraspider.spider.fetcher.quora;

/**
 * Created by feemung on 16/5/12.
 */
public interface WaitDriver {
    boolean isWait();
    void waitLoad();
    void executeThing();
}
