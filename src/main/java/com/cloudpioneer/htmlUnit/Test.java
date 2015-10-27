package com.cloudpioneer.htmlUnit;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import org.w3c.dom.html.HTMLDOMImplementation;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2015/10/15.
 */
public class Test {
    public static void main(String[] args) throws Exception{
//        final WebClient webClient = new WebClient(BrowserVersion.CHROME);

        //设置webClient的相关参数
        // webClient.setJavaScriptEnabled(true);


//        final HtmlPage rootPage = (HtmlPage)webClient.getPage("http://www.gzzs.gov.cn/NewOpen/NewOpenMList.aspx?cid=0&pid=62");
//
//        获取第一个数据库
//        HtmlInput hi = (HtmlInput) rootPage.getElementById("AdvancePages1_lnkbtnPre");
//        HtmlInput btn = (HtmlInput)rootPage.getHtmlElementById("AdvancePages1_lnkbtnPre");
//        System.out.println(btn.getDefaultValue());
//        System.out.println("=========================正在跳转==========================");

        //创建webclient
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //htmlunit 对css和javascript的支持不好，所以请关闭之
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        HtmlPage page = (HtmlPage)webClient.getPage("http://www.gzzs.gov.cn/NewOpen/NewOpenMList.aspx?cid=0&pid=62");
        //通过id获得"百度一下"按钮
        HtmlAnchor he = page.getHtmlElementById("AdvancePages1__page_5");
        System.out.println(he.asText());
        //关闭webclient
        webClient.closeAllWindows();

    }
}
