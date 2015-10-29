package com.cloudpioneer.htmlUnit.test;

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
        String url = "http://gz.hrss.gov.cn/col/col41/index.html";
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(3600 * 1000);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
        webClient.getOptions().setTimeout(3600 * 100);
        webClient.waitForBackgroundJavaScript(600 * 1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        // 模拟浏览器打开一个目标网
        HtmlPage page1 = webClient.getPage(url);

        List<String> fistXpath = new LinkedList<String>();
        fistXpath.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        fistXpath.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");

        DomElement e1 = ((DomElement) page1.getByXPath(fistXpath.get(1)).get(0));
        HtmlPage pg2 = e1.click();

        Object ps2 = pg2.getEnclosingWindow().getScriptObject();

//        System.out.println("pg3: " +pg3.asText());
        //得到第2页
//        System.out.println("pg2: " + pg2.asText());

        webClient.waitForBackgroundJavaScript(5000);

        //得到第1页
        String html = pg2.asXml().replaceFirst("<\\?xml version=\"1.0\" encoding=\"(.+)\"\\?>", "<!DOCTYPE html>");
        HtmlPage pac1  = getHtmlPage(html, "http://gz.hrss.gov.cn/col/col41/index.html", pg2.getEnclosingWindow());

//        System.out.println("pac1" +"\n" + pac1.asText());
        //此时pg2还是第2页
//        System.out.println("pg2: " + pg2.asText());
//      System.out.println("pac1 " + pac1.asText());

        //2上一页
        pg2.getEnclosingWindow().setEnclosedPage(pg2);
        pg2.getEnclosingWindow().setScriptObject(ps2);

        DomElement e2 = ((DomElement) pg2.getByXPath(fistXpath.get(0)).get(0));
        HtmlPage pg1 = e2.click();
//        System.out.println("pg1 " + pg1.asText());

        //2下一页
//        System.out.println("pac1 " + pac1.asText());

        pac1.getEnclosingWindow().setEnclosedPage(pac1);
        pac1.getEnclosingWindow().setScriptObject(ps2);
        DomElement e3 = ((DomElement) pac1.getByXPath(fistXpath.get(1)).get(0));
        HtmlPage pg4 = e3.click();
//        System.out.println("pg4:" + "\n" + pg4.asText());
        System.out.println("pac1: " + pac1.asText());

//        System.out.println("pac1 after:" + pac1.asText());
//        Object ps1 = pac1.getEnclosingWindow().getScriptObject();
//        pac1.getEnclosingWindow().setEnclosedPage(pac1);
//        pac1.getEnclosingWindow().setScriptObject(ps1);
//
//        DomElement e3 = ((DomElement) pac1.getByXPath(fistXpath.get(0)).get(0));
//        HtmlPage pg4 = e2.click();
//        System.out.println("pg4: " + pg4.asText());
//
    }
}
