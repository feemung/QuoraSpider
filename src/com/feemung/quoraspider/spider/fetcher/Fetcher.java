package com.feemung.quoraspider.spider.fetcher;

import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/24.
 */
public interface Fetcher {
    Object fetch(Task task) throws Exception;
}
