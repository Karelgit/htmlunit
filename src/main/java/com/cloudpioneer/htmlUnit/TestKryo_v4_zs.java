package com.cloudpioneer.htmlUnit;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import javax.xml.bind.SchemaOutputResolver;
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
public class TestKryo_v4_zs {

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

    //打印HtmlPage的页码
    public static void printPageNo(HtmlPage hp) {
        String pNoXpath = "html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/span[1]";
        DomElement pageNo1 = (DomElement) hp.getByXPath(pNoXpath).get(0);
        System.out.println("第" + pageNo1.getAttribute("id").toString() + "页");
    }
    public static void testYouku() throws IOException {
        String url = "http://www.gzzs.gov.cn/NewOpen/NewOpenML.aspx?pid=62";
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
        /*elements.add("/*//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        elements.add("/*//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");*/
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[5]");
        elements.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[6]");

        List<String> firstXpath = new LinkedList<String>();
        firstXpath.add("html/body/form/div[3]/div/div[2]/div[2]/div/div/table/tbody/tr/td/a[5]");

        //取得第三页
        HtmlPage pg2 = null;
//        System.out.println("size: " + page.getByXPath(firstXpath.get(0)).size());
        try {
            DomElement e2 = (DomElement) page.getByXPath(firstXpath.get(0)).get(0);
            pg2 = e2.click();
            printPageNo(pg2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        TestKryo_v4_zs.testYouku();
    }


}


