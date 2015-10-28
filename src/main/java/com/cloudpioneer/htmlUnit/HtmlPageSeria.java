package com.cloudpioneer.htmlUnit;

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by Administrator on 2015/10/28.
 */
public class HtmlPageSeria extends HtmlPage implements Serializable{
    public HtmlPageSeria(URL originatingUrl, WebResponse webResponse, WebWindow webWindow) {
        super(originatingUrl, webResponse, webWindow);
    }
}
