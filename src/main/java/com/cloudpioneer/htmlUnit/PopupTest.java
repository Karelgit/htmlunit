package com.cloudpioneer.htmlUnit;


import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowEvent;
import com.gargoylesoftware.htmlunit.WebWindowListener;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;



public class PopupTest
{
    // save the pop up window
    final static LinkedList<WebWindow> windows   = new
            LinkedList<WebWindow>();

    // get the main page
    private static HtmlPage getMainPage(String strUrl) throws Exception
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
    }

    // get the popup page
    private static  HtmlPage getPopupPage()
    {
        WebWindow latestWindow = windows.getLast();
        return (HtmlPage) latestWindow.getEnclosedPage();
    }

    public static void main (String [] args) throws Exception
    {
        String strUrl =
                "http://www.tizag.com/javascriptT/javascriptpopups.php";

        // get the page and click POP2  button
        HtmlPage mainPage = getMainPage(strUrl);
        System.out.println(mainPage.getTitleText());
        List popupList = (List)
                mainPage.getByXPath("//input[@value='POP2!']");
        if (popupList.size() == 1)
        {
            HtmlButtonInput popup = (HtmlButtonInput)
                    popupList.get(0);
            popup.click();
        }

        // get the popup window contents
        if ( windows.size() > 0)
        {
            HtmlPage popupPage = getPopupPage();

            System.out.println(popupPage.getTitleText());
        }
    }
}