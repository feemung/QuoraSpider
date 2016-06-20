package com.feemung.quoraspider.spider.fetcher;

import com.feemung.quoraspider.Log.LogFM;
import com.feemung.quoraspider.spider.entry.Content;
import com.feemung.quoraspider.spider.entry.HTMLContent;
import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.spider.fetcher.wuyong.ChromeDriverFetcherUtils2;

/**
 * Created by feemung on 16/4/24.
 */
public class WebFetcher implements Fetcher{
    private LogFM logFM=LogFM.getInstance(WebFetcher.class);
    private static int count=0;
    @Override
    public Content fetch(Task task)throws Exception{
        logFM.d("开始下载网页"+String.valueOf(count));
        count++;
        HTMLContent htmlContent=new HTMLContent(task);

       // String page=HtmlunitFetcher.donwload(task.getURL());

      String page=ChromeDriverFetcher.getChromeDriverFetcher().get(task.getURL());

        htmlContent.setPage(page);

        return htmlContent;
    }

}
