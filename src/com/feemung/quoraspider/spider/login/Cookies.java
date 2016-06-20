package com.feemung.quoraspider.spider.login;


        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileWriter;

        import java.text.SimpleDateFormat;
        import java.util.Iterator;
        import java.util.Locale;
        import java.util.concurrent.TimeUnit;

        import com.feemung.quoraspider.Utils.FileUtils;
        import org.openqa.selenium.*;
        import org.openqa.selenium.chrome.ChromeDriver;
        import org.openqa.selenium.interactions.Actions;
        import org.openqa.selenium.interactions.internal.Coordinates;

public class Cookies {
    /**
     * @author Young
     *
     */
    public static void addCookies(WebDriver driver) {


        //driver.get("file:///Users/feemung/Desktop/question/quoral2.html");


        // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement loginEln=driver.findElement(By.className("regular_login"));

        WebElement user = loginEln
                .findElement(By.name("email"));
                //.findElement(By.xpath("//input[@name='email']"));
        user.clear();
        user.sendKeys("feemung@163.com");
        WebElement password = loginEln.findElement(By
                .name("password"));
               // .xpath("//input[@name='password']"));
        password.clear();
        password.sendKeys("Aa123456");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Iterator<WebElement> iterator = loginEln.findElements(By.cssSelector("input")).iterator();
        //WebElement submit = loginEln.findElement(By
                //.xpath("/input[@value='login']"));
        WebElement submit=null;
        while (iterator.hasNext()){
            WebElement element=iterator.next();
            if("submit".equals(element.getAttribute("type"))){
                submit=element;
            }
        }
       int count=0;
        while (!submit.isDisplayed()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(count>10){
                System.exit(-1);
            }
            count++;
        }
        System.out.println("=="+submit.getAttribute("type"));
        Actions actions=new Actions(driver);
       // actions.click(submit);
        //submit.submit();
        submit.click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File file = new File("QuoraBroswer3.data");
        //SimpleDateFormat format=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

        try {
            // delete file if exists
            file.delete();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            for (Cookie ck : driver.manage().getCookies()) {
                String date=null;
                if(ck.getExpiry()!=null){
                   // date=format.format(ck.getExpiry());
                }
                bw.write(ck.getName() + ";" + ck.getValue() + ";"
                        + ck.getDomain() + ";" + ck.getPath() + ";"
                        + ck.getExpiry() + ";" + ck.isSecure());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("cookie write to file");
        }

    }
}
