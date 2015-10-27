package com.cloudpioneer.htmlUnit;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/15.
 */
public class ClickZS implements WebWindow{

    // save the pop up window
    final static LinkedList<WebWindow> windows   = new
            LinkedList<WebWindow>();

    // get the main page
//    private static HtmlPage getMainPage(String strUrl) throws Exception
//    {
//        final WebClient webClient = new WebClient();
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.addWebWindowListener(new WebWindowListener()
//        {
//            public void webWindowClosed(WebWindowEvent event)
//            {
//            }
//
//            public void webWindowContentChanged(WebWindowEvent
//                                                        event)
//            {
//            }
//
//            public void webWindowOpened(WebWindowEvent event)
//            {
//                windows.add(event.getWebWindow());
//            }
//        });
//
//        final URL url = new URL(strUrl);
//        return (HtmlPage) webClient.getPage(url);
//    }

    // get the popup page
    private static  HtmlPage getPopupPage()
    {
        WebWindow latestWindow = windows.getLast();
        return (HtmlPage) latestWindow.getEnclosedPage();
    }

    public static void testYouku() throws Exception {

        String zsUrl = "http://www.gzzs.gov.cn/NewOpen/NewOpenMList.aspx?cid=0&pid=62";
        String url = "http://gz.hrss.gov.cn/col/col41/index.html";

        // String a = "<a page=\"2\">178-101</a>";
        // String url="http://www.baidu.com";
        // 模拟一个浏览器
        WebClient webClient = new WebClient();

        //LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
        //java.util.logging.Logger.getLogger("net.sourceforge.htmlunit").setLevel(java.util.logging.Level.OFF);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        // final WebClient webClient=new
        // WebClient(BrowserVersion.FIREFOX_10,"http://myproxyserver",8000);
        // //使用代理
        // final WebClient webClient2=new
        // WebClient(BrowserVersion.INTERNET_EXPLORER_10);
        // 设置webClient的相关参数
//        webClient.getOptions().setJavaScriptEnabled(true);
//        webClient.getOptions().setActiveXNative(false);
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        webClient.waitForBackgroundJavaScript(600*1000);
//        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(3600*1000);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setTimeout(3600*1000);
        webClient.waitForBackgroundJavaScript(600 * 1000);
//      webClient.waitForBackgroundJavaScript(600*1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        // 模拟浏览器打开一个目标网址
        HtmlPage page = webClient.getPage(zsUrl);
//      该方法在getPage()方法之后调用才能生效
        webClient.waitForBackgroundJavaScript(1000 * 3);
        webClient.setJavaScriptTimeout(0);

        List<String> elements = new LinkedList<String>();
//        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");
//        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[2]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[3]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[4]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[5]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[6]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[7]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[8]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[9]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[10]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[11]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[12]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[13]");

        List links = (List) page.getByXPath ("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");

        Map<Integer,HtmlPage> map = new HashMap<Integer, HtmlPage>();

        List<String> fistXpath = new LinkedList<String>();
        fistXpath.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");

        //取得第二页
//        DomElement e2 = ((DomElement) page.getByXPath(fistXpath.get(0)).get(0));
//        HtmlPage pg2 = e2.click();
//        //System.out.println("pg2:" + pg2.asText());


//        HtmlPage selectedPage = pg2;
//        HtmlPage currentPage = pg2;
        HtmlPage p = null;


        for (int i=0; i<elements.size();i++) {
            DomElement element = (DomElement)page.getByXPath(elements.get(i)).get(0);
            p = element.click();
            map.put(i, p);
           // System.out.println("i=" + i + "\n\n\n" + map.get(i).asText());
        }

//       //循环点击
//        for (int i=0; i<elements.size();i++) {
//            Object ps = selectedPage.getEnclosingWindow().getScriptObject();
//            selectedPage.getEnclosingWindow().setEnclosedPage(currentPage);
//            selectedPage.getEnclosingWindow().setScriptObject(ps);
//
//            DomElement element = (DomElement)selectedPage.getByXPath(elements.get(i)).get(0);
//
//            p = element.click();
//            currentPage = p;
//            map.put(i, currentPage);
//            System.out.println("i=" + i + "\n\n\n" + currentPage.asText());
//
//            if(i+1<elements.size()) {
//                DomElement element1 = (DomElement)selectedPage.getByXPath(elements.get(i+1)).get(0);
//                HtmlPage hp2 = element1.click();
//                selectedPage = currentPage;
//            }
//            //System.out.println(i +"'s selectPage: " + selectedPage.asText());
//        }




       // ((HtmlDivision) links.get(0)).getPage().getEnclosingWindow().getEnclosedPage();


        Object page1Script = p.getEnclosingWindow().getScriptObject();
        p.getEnclosingWindow().setEnclosedPage(p);
        System.out.println("the p we want is: " + p.asText());
        p.getEnclosingWindow().setScriptObject(page1Script);
        DomElement element1 = (DomElement)p.getByXPath(elements.get(4)).get(0);
        HtmlPage newPage = element1.click();
        System.out.println("newPage: " + newPage.asText());

        /*WebWindow wb =  webClient.getCurrentWindow();
        wb.setEnclosedPage(map.get(3));
        wb.setScriptObject(map.get(3).getScriptObject());
        HtmlPage hp= (HtmlPage)webClient.getCurrentWindow().getEnclosedPage();
        Object hpScript = hp.getEnclosingWindow().getScriptObject();
        hp.getEnclosingWindow().setEnclosedPage(hp);
        hp.getEnclosingWindow().setScriptObject(hpScript);
        System.out.println("\n\n\n" + "hp: " + "\n\n\n" + hp.asText());

        DomElement element1 = hp.getElementById("AdvancePages1__page_7");
        System.err.println("\n\n\n" + "element we want is : " + element1.asText() + "\n\n\n");*/

//        HtmlPage newPage1 = element1.click();

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

    public static void main(String[] args) throws Exception{
        ClickZS.testYouku();
    }
}


