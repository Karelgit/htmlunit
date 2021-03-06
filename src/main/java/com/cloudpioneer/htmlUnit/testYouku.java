package com.cloudpioneer.htmlUnit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 */
public class testYouku implements WebWindow,KryoSerializable {

    // save the pop up window
    final static LinkedList<WebWindow> windows = new
            LinkedList<WebWindow>();

    // get the main page
    /*private static HtmlPage getMainPage(String strUrl) throws Exception
    {
        final WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.addWebWindowListener(new WebWindowListener()
        {
            public void webWindowClosed(WebWindowEvent event)
            {
            }

            public void webWindowContentChanged(WebWindowEvent
                                                        event)
            {
            }

            public void webWindowOpened(WebWindowEvent event)
            {
                windows.add(event.getWebWindow());
            }
        });

        final URL url = new URL(strUrl);
        return (HtmlPage) webClient.getPage(url);
    }*/

    // get the popup page
    private static HtmlPage getPopupPage() {
        WebWindow latestWindow = windows.getLast();
        return (HtmlPage) latestWindow.getEnclosedPage();
    }

    private static HtmlPage getHtmlPage(String html, String url, WebWindow webWindow) {

        try {
            URL url1 = new URL(url);
            StringWebResponse stringWebResponse = new StringWebResponse(html, url1);
            return HTMLParser.parseHtml(stringWebResponse, webWindow);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void testYouku() throws Exception {
        Kryo kryo = new Kryo();
      //  Registration registration = kryo.register(HtmlPage.class);

        String zsUrl = "http://www.gzzs.gov.cn/NewOpen/NewOpenMList.aspx?cid=0&pid=62";
        String url = "http://gz.hrss.gov.cn/col/col41/index.html";
        // 模拟一个浏览器
        WebClient webClient = new WebClient();

        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(3600 * 1000);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setTimeout(3600 * 1000);
        webClient.waitForBackgroundJavaScript(600 * 1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        // 模拟浏览器打开一个目标网址
        HtmlPage page = webClient.getPage(url);
//      该方法在getPage()方法之后调用才能生效
        webClient.waitForBackgroundJavaScript(1000 * 3);
        webClient.setJavaScriptTimeout(0);

        List<String> elements = new LinkedList<String>();
        List<String> elements_1 = new LinkedList<String>();

        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");


        /*elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[5]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[6]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[7]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[8]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[9]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[10]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[11]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[12]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[13]");*/

        List links = (List) page.getByXPath("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");

        Map<Integer, HtmlPage> map = new HashMap<Integer, HtmlPage>();

        List<String> fistXpath = new LinkedList<String>();
        fistXpath.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");

        //取得第三页
        DomElement e2 = ((DomElement) page.getByXPath(fistXpath.get(0)).get(0));
        HtmlPage pg2 = e2.click();
        DomElement e3 = ((DomElement) page.getByXPath(fistXpath.get(0)).get(0));
        HtmlPage pg3 = e3.click();


        Output output = new Output(new ByteArrayOutputStream());
        kryo.writeObject(output,pg3);


//        System.err.println("pg3: " + pg3.asText());
        System.out.println("pg2:" +'\n' +  pg2.asText());

        HtmlPage selectedPage = pg3;
        HtmlPage currentPage = pg3;
        HtmlPage p = null;

        //page可以持久化
        //先拿第4页
        Object ps = currentPage.getEnclosingWindow().getScriptObject();
        currentPage.getEnclosingWindow().setEnclosedPage(currentPage);
        currentPage.getEnclosingWindow().setScriptObject(ps);

        DomElement element = (DomElement) currentPage.getByXPath(elements.get(1)).get(0);

        p = element.click();
        HtmlPage changedPage = p;

        //再拿第2页
        HtmlPage page1 = webClient.getPage(url);


        //反序列化
        Input input = new Input(output.getBuffer());
       HtmlPage hp123 =  kryo.readObject(input, pg3.getClass());

        System.out.println("hp123:" + hp123.asText());
        page1 = pg3;
//        System.out.println("page1: " + page1.asText());
        DomElement e2s = ((DomElement) page1.getByXPath(fistXpath.get(0)).get(0));
        HtmlPage pg2s = e2s.click();
//      System.err.println("pg2s:" + pg2s.asText());

        currentPage = pg2s;
        Object pss = currentPage.getEnclosingWindow().getScriptObject();
        currentPage.getEnclosingWindow().setEnclosedPage(currentPage);
        currentPage.getEnclosingWindow().setScriptObject(pss);

        DomElement element1 = (DomElement) currentPage.getByXPath(elements.get(0)).get(0);

        p = element1.click();
        HtmlPage changedPage1 = p;
//        System.out.println("changedPage1:" + changedPage1.asText());


        //循环点击(每次点击都回溯到父节点，继续点击下一个,只有上下页情况)
       /* for (int i=0; i<elements.size(); i++) {
            Object ps = selectedPage.getEnclosingWindow().getScriptObject();
            selectedPage.getEnclosingWindow().setEnclosedPage(selectedPage);
            selectedPage.getEnclosingWindow().setScriptObject(ps);

            DomElement element = (DomElement)selectedPage.getByXPath(elements.get(i)).get(0);
            //点击下一页得到第3页
            p = element.click();
            currentPage = p;
            map.put(i, currentPage);
            System.out.println("i=" + i + "\n\n\n" + currentPage.asText());

            if(i+1<elements.size() && elements.get(i) != elements.get(i+1)) {

                //通过点击上一页回到
                Object ps1 = currentPage.getEnclosingWindow().getScriptObject();
                currentPage.getEnclosingWindow().setEnclosedPage(currentPage);
                currentPage.getEnclosingWindow().setScriptObject(ps1);

                DomElement element1 = (DomElement)selectedPage.getByXPath(elements.get(i+1)).get(0);
                currentPage = element1.click();
                selectedPage = currentPage;
            }
//            System.out.println(i +"'s selectPage: " + selectedPage.asText());
        }

        selectedPage = currentPage;
        
        for (int i=0; i<elements_1.size(); i++) {
            Object ps = selectedPage.getEnclosingWindow().getScriptObject();
            selectedPage.getEnclosingWindow().setEnclosedPage(selectedPage);
            selectedPage.getEnclosingWindow().setScriptObject(ps);

            DomElement element = (DomElement)selectedPage.getByXPath(elements_1.get(i)).get(0);
            //点击下一页得到第3页
            p = element.click();
            currentPage = p;
            map.put(i, currentPage);
            System.out.println("i=" + i + "\n\n\n" + currentPage.asText());

            if(i+1<elements_1.size() && elements_1.get(i) != elements_1.get(i+1)) {

                //通过点击上一页回到
                Object ps1 = currentPage.getEnclosingWindow().getScriptObject();
                currentPage.getEnclosingWindow().setEnclosedPage(currentPage);
                currentPage.getEnclosingWindow().setScriptObject(ps1);

                DomElement element1 = (DomElement)selectedPage.getByXPath(elements_1.get(i+1)).get(0);
                currentPage = element1.click();
                selectedPage = currentPage;
            }
//            System.out.println(i +"'s selectPage: " + selectedPage.asText());
        }*/


        //循环点击（一直往下一页点击）
//        for (int i=0; i<elements.size();i++) {
//            Object ps = currentPage.getEnclosingWindow().getScriptObject();
//            currentPage.getEnclosingWindow().setEnclosedPage(currentPage);
//            currentPage.getEnclosingWindow().setScriptObject(ps);
//
//            DomElement element = (DomElement) currentPage.getByXPath(elements.get(i)).get(0);
//
//            p = element.click();
//            currentPage = p;
//            map.put(i, currentPage);
//            System.out.println("i=" + i + "\n\n\n" + currentPage.asText());
//        }





       /*
       // ((HtmlDivision) links.get(0)).getPage().getEnclosingWindow().getEnclosedPage();

        HtmlPage hp = map.get(0);
        System.out.println("hp0:" + hp.asText());

        Object page1Script = hp.getEnclosingWindow().getScriptObject();
        hp.getEnclosingWindow().setEnclosedPage(map.get(1));
       // System.out.println("before click:" + hp.asText());
        hp.getEnclosingWindow().setScriptObject(page1Script);
        DomElement element1 = (DomElement)hp.getByXPath(elements.get(1)).get(0);
        HtmlPage newPage = element1.click();
        System.out.println("newPage: " + newPage.asText());*/

        /*WebWindow wb =  webClient.getCurrentWindow();
        wb.setEnclosedPage(map.get(3));
        wb.setScriptObject(map.get(3).getScriptObject());
        HtmlPage hp= (HtmlPage)webClient.getCurrentWindow().getEnclosedPage();
        Object hpScript = hp.getEnclosingWindow().getScriptObject();
        hp.getEnclosingWindow().setEnclosedPage(hp);
        hp.getEnclosingWindow().setScriptObject(hpScript);
        System.out.println("\n\n\n" + "hp: " + "\n\n\n" + hp.asText());

        DomElement element1 = hp.getElementById("AdvancePages1__page_7");
        System.err.println("\n\n\n" + "element we want is : " + element1.asText() + "\n\n\n");

        HtmlPage newPage1 = element1.click();*/

//        System.out.println( "\n"+"newPage1" + "\n" + newPage1.asText());
    }


    public String getName() {
        return null;
    }

    public void setName(String s) {

    }

    public Page getEnclosedPage() {
        return null;
    }

    public void setEnclosedPage(Page page) {

    }

    public WebWindow getParentWindow() {
        return null;
    }

    public WebWindow getTopWindow() {
        return null;
    }

    public WebClient getWebClient() {
        return null;
    }

    public History getHistory() {
        return null;
    }

    public void setScriptObject(Object o) {

    }

    public Object getScriptObject() {
        return null;
    }

    public JavaScriptJobManager getJobManager() {
        return null;
    }

    public boolean isClosed() {
        return false;
    }

    public int getInnerWidth() {
        return 0;
    }

    public void setInnerWidth(int i) {

    }

    public int getOuterWidth() {
        return 0;
    }

    public void setOuterWidth(int i) {

    }

    public int getInnerHeight() {
        return 0;
    }

    public void setInnerHeight(int i) {

    }

    public int getOuterHeight() {
        return 0;
    }

    public void setOuterHeight(int i) {

    }

    public static void main(String[] args) throws Exception {
        testYouku.testYouku();
    }

    public void write(Kryo kryo, Output output) {

    }

    public void read(Kryo kryo, Input input) {

    }
}


