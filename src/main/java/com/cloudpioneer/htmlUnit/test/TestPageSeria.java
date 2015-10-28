package com.cloudpioneer.htmlUnit.test;

import com.cloudpioneer.htmlUnit.HtmlPageSeria;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/28.
 */
public class TestPageSeria {

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

    public static void main(String[] args) throws Exception{
        String zsUrl = "http://www.gzzs.gov.cn/NewOpen/NewOpenMList.aspx?cid=0&pid=62";
        String url = "http://gz.hrss.gov.cn/col/col41/index.html";
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
//      webClient.waitForBackgroundJavaScript(600*1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        // 模拟浏览器打开一个目标网
//        HtmlPage page = new HtmlPageSeria();
        HtmlPage page1 = webClient.getPage(url);
        String html = page1.asXml().replaceFirst("<\\?xml version=\"1.0\" encoding=\"(.+)\"\\?>", "<!DOCTYPE html>");
        HtmlPage page2  = getHtmlPage(html, "http://gz.hrss.gov.cn/col/col41/index.html", page1.getEnclosingWindow());


        List<String> fistXpath = new LinkedList<String>();
        fistXpath.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");

        DomElement e2 = ((DomElement) page2.getByXPath(fistXpath.get(0)).get(0));
        HtmlPage pg2 = e2.click();
        System.out.println("11");
        webClient.waitForBackgroundJavaScript(5000);
        System.out.println(pg2.asText());
//        System.out.println(page.asText());
    }
}
