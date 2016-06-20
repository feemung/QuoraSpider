package com.feemung.quoraspider.Utils;

import com.feemung.quoraspider.Log.LogFM;

import java.io.*;

/**
 * Created by feemung on 16/4/27.
 */
public class FileUtils {
    private static LogFM logFM=LogFM.getInstance(FileUtils.class);
    public static String readFile(String fileName){
        File file = new File("/Users/feemung/Desktop/question/"+fileName);
        BufferedReader reader = null;
        StringBuffer sb=new StringBuffer();
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束

            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                sb.append(tempString);
                line++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }
    public static void saveFile(String fileName,String text,boolean display)throws Exception{
        String path="/Users/feemung/Desktop/question/";
        File file=new File(path+fileName);
        FileWriter out=new FileWriter(file);
        out.write(text);
        out.flush();
        out.close();
        logFM.d(fileName,"文件已经保存在了",file.getAbsolutePath());
        if(display){
            logFM.d(text);
        }
    }
    public static Object readObject(String fileName)throws Exception{
        ObjectInputStream in=new ObjectInputStream(new FileInputStream(
                new File("/Users/feemung/Desktop/"+fileName)));
        Object obj=in.readObject();
        in.close();
        return obj;
    }
    public static void saveObject(String fileName,Object obj)throws Exception{
        ObjectOutput out=new ObjectOutputStream(new FileOutputStream(
                new File("/Users/feemung/Desktop/"+fileName)));
        out.writeObject(obj);
        out.flush();
        out.close();

    }
}
