package com.feemung.quoraspider.spider.parse;

import com.feemung.quoraspider.spider.entry.Content;
import com.feemung.quoraspider.spider.entry.Data;
import com.feemung.quoraspider.spider.entry.Task;
import com.feemung.quoraspider.spider.parse.ParseContext;

import org.ccil.cowan.tagsoup.PYXScanner;
import org.ccil.cowan.tagsoup.Scanner;
import org.ccil.cowan.tagsoup.XMLWriter;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.InputSource;

import java.io.StringReader;

/**
 * Created by feemung on 16/4/24.
 */
public class HTMLParser  {
    private Content content;
    private Task task;
    private ParseContext parseContext;

    public Data parse(Content content, Task task, ParseContext parseContext) throws Exception{
        this.content=content;
        this.task=task;
        this.parseContext=parseContext;

        return null;
    }


    public void parse(Content content, Object object, Task task, ParseContext parseContext) {

    }
    public void parse(String html)throws Exception{

        StringReader xmlReader = new StringReader("");
        StringReader sr = new StringReader(html);
        InputSource src = new InputSource(sr);//构建InputSource实例
        Parser parser = new Parser();//实例化Parse
        XMLWriter writer = new XMLWriter();//实例化XMLWriter，即SAX内容处理器
        parser.setContentHandler(writer);//设置内容处理器
        parser.parse(src);//解析
        Scanner scan = new PYXScanner();
        scan.scan(xmlReader, parser);//通过xmlReader读取解析后的结果
        char[] buff = new char[1024];
        while(xmlReader.read(buff) != -1) {
            System.out.println(new String(buff));//打印解析后的结构良好的HTML文档
        }
    }
}
