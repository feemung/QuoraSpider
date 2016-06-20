package com.feemung.quoraspider.spider.parse;

import com.feemung.quoraspider.spider.entry.Content;
import com.feemung.quoraspider.spider.entry.Data;
import com.feemung.quoraspider.spider.entry.Task;

/**
 * Created by feemung on 16/4/24.
 */
public interface Parser {
    Data parse(Content content,Task task,ParseContext parseContext);
    void parse(Content content,Object object,Task task,ParseContext parseContext);
}
