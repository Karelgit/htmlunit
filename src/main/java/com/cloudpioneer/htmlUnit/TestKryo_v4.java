package com.cloudpioneer.htmlUnit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**通过String构造HtmlPage.结果是不能再次使用新生成的HtmlPage进行点击
 * Created by Administrator on 2015/10/15.
 */
public class TestKryo_v4 implements WebWindow{

    // save the pop up window
    final static LinkedList<WebWindow> windows = new LinkedList<WebWindow>();
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

    public static void testYouku() throws IOException {
        String url = "http://gz.hrss.gov.cn/col/col41/index.html";
        URL url1 = new URL("http://gz.hrss.gov.cn/col/col41/index.html");
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(3600*1000);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setTimeout(3600 * 1000);
        webClient.waitForBackgroundJavaScript(600 * 1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        webClient.waitForBackgroundJavaScript(1000 * 3);
        webClient.setJavaScriptTimeout(0);

        List<String> elements = new LinkedList<String>();
        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");

        List<String> fistXpath = new LinkedList<String>();
        fistXpath.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");

        //取得第三页
        HtmlPage pg2 = null;
        HtmlPage pg3 = null;
        try {
            DomElement e2 = ((DomElement) page.getByXPath(fistXpath.get(0)).get(0));
            pg2 = e2.click();
            DomElement e3 = ((DomElement) page.getByXPath(fistXpath.get(0)).get(0));
            pg3 = e3.click();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object ps1 = pg3.getEnclosingWindow().getScriptObject();

        //Get page as html
        String htmlBody = pg3.asXml();
        System.out.println("pg3 to xml:　" + "\n" + pg3.asXml());

        //Save the response in a file
        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("data")));
        bw.write(htmlBody);
        bw.close();

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

      System.out.println("pg4: " + changedPage.asText());

        WebClient webClient1 = new WebClient();
        webClient.waitForBackgroundJavaScript(5000);
        StringWebResponse response = new StringWebResponse(htmlBody,url1);
        HtmlPage pageNew = HTMLParser.parseHtml(response,webClient1.getCurrentWindow());
        System.out.println("pageNew: " + pageNew.asText());
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
        TestKryo_v4.testYouku();
    }

    public void write(Kryo kryo, Output output) {

    }

    public void read(Kryo kryo, Input input) {

    }
}


