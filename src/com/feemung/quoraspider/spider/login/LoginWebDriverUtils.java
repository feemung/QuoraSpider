package com.feemung.quoraspider.spider.login;

        import java.io.*;


        import java.util.Date;
        import java.util.Iterator;
        import java.util.StringTokenizer;

        import com.feemung.quoraspider.Log.LogFM;
        import com.feemung.quoraspider.spider.exception.LoginFailedException;
        import com.feemung.quoraspider.spider.login.quora.CheckLoginStatus;
        import org.jsoup.Jsoup;
        import org.openqa.selenium.By;
        import org.openqa.selenium.Cookie;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.os.WindowsUtils;

        import javax.security.auth.login.FailedLoginException;

public class LoginWebDriverUtils {

    /**
     *
     * @param
     */
    private static LogFM logFM=LogFM.getInstance(LoginWebDriverUtils.class);
    public static void login(WebDriver driver,String loginUrl)throws Exception {

        try {
           // driver.get(loginUrl);
            useCookieLogin(driver);
        } catch (Exception e) {
            logFM.e("用cookie登陆失败");
        }
        driver.get(loginUrl);
        if (!CheckLoginStatus.isOnline(Jsoup.parse(driver.getPageSource()))) {
            logFM.e("用cookie登陆失败");
            if (!CheckLoginStatus.isFindLoginWebElement(Jsoup.parse(driver.getPageSource()))) {
                driver.get(loginUrl);
            }
            try {
                defaultFormLogin(driver);
            } catch (Exception e) {
                logFM.e("登陆失败");
                LoginFailedException loginFailedException=(LoginFailedException)e;
                throw loginFailedException;
            }
        }
        if (!CheckLoginStatus.isOnline(Jsoup.parse(driver.getPageSource()))) {
            logFM.e("登陆失败");
            throw new Exception("登陆失败");

        }
        logFM.d("登陆成功");
    }
    public static void defaultFormLogin(WebDriver driver)throws Exception{

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
        submit.click();
        Thread.sleep(15000);
        long waitStart=System.currentTimeMillis();
        while (!CheckLoginStatus.isOnline(Jsoup.parse(driver.getPageSource()))){
            Thread.sleep(1000);
           if( System.currentTimeMillis()-waitStart>20){
               throw new FailedLoginException("输入账号和密码后没有登陆成功");
           }
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
            logFM.d("保存cookie成功，文件为", file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            logFM.d("保存cookie失败");
        }


    }
    public static void useCookieLogin(WebDriver driver)throws Exception{
        WindowsUtils.getProgramFilesPath();
        //  driver.get("https://www.quora.com/");
        driver.manage().deleteAllCookies();
        logFM.d("删除全部cookie");

            File file=new File("QuoraBroswer3.data");
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            String line;
            while((line=br.readLine())!= null)
            {
                StringTokenizer str=new StringTokenizer(line,";");
                while(str.hasMoreTokens())
                {
                    String name=str.nextToken();
                    String value=str.nextToken();
                    String domain=str.nextToken();
                    String path=str.nextToken();
                    Date expiry=null;
                    String dt;
                    if(!(dt=str.nextToken()).equals(null))
                    {
                        //expiry=new Date(dt);
                        //expiry=new Date(dt);

                        //System.out.println();
                    }
                    boolean isSecure=new Boolean(str.nextToken()).booleanValue();
                    Cookie ck=new Cookie(name,value,domain,path,expiry,isSecure);
                    driver.manage().addCookie(ck);
                }
            }

            logFM.d("已经写入cookie");

    }

}
