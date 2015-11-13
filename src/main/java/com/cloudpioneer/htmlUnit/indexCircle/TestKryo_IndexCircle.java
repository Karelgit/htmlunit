package com.cloudpioneer.htmlUnit.indexCircle;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.tag.Tag;

import java.io.IOException;
import java.util.*;

/**
 * 每一次都从index进行往下触发，避免Ajax无法回复历史状态
 * Created by Karel on 2015/11/16.
 */
public class TestKryo_IndexCircle   {

    //判断一个页面中子标签是否全部已经执行
    public static boolean allTagsExecuted(HtmlPage hp)    {
        Boolean flag = true;
        for(int i=0; i<hp.getTagList().size(); i++) {
            flag = flag &&(hp.getTagList().get(i).getStatus() == 2);
        }
        return flag;
    }

    //给每个HtmlPage页面设置备选标签列表
    public static List<Tag> initTagList()    {
        List<Tag> tagList = new ArrayList<Tag>();
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        tag1.setXpath("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        tag2.setXpath("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");
        tag1.setStatus(0);
        tag2.setStatus(0);
        tagList.add(tag1);
        tagList.add(tag2);

        return tagList;
    }

    //点击标签，参数1：对应HtmlPage; 参数2:对应tag
    public static HtmlPage excuteTag(HtmlPage hp,Tag tag) throws IOException  {
        DomElement element = (DomElement) hp.getByXPath(tag.getXpath()).get(0);
        HtmlPage page = element.click();
        return page;
    }

    //打印HtmlPage的页码
    public static void printPageNo(HtmlPage hp) {
        String pNoXpath = "//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[6]/input";
        DomElement pageNo1 = (DomElement) hp.getByXPath(pNoXpath).get(0);
        System.out.println("第" + pageNo1.getAttribute("value").toString()+"页" + "\n");
    }

    public static void test() throws IOException {
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
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage index= webClient.getPage(url);
        webClient.waitForBackgroundJavaScript(500);
        webClient.setJavaScriptTimeout(0);

        String pNoXpath = "//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[6]/input";
        //使用堆栈对每个HtmlPage进行存储
        ArrayStack arrayStack = new ArrayStack();

        //到每一层，如果当前节点里面的备选标签status=2,进行页面的收集到pageBox
        List<HtmlPage> pageBox = new ArrayList<HtmlPage>();

        /**
         *
         * j:用来作为index指向第2层遍历的标签的循环变量
         * k:用来作为第2层页面指向第3层遍历的标签的循环变量
         *current_j:如果index指向第二层的标签队列没有完成执行，那么j就不会增加，每次从index重新遍历时，
         * 1、应把上一次标签队列的执行情况再次初始化给index页面
         * 反之，j++之后，表示上一个页面下的标签队列状态为：已执行，从index开始遍历时，应该再次初始化index的标签队列执行情况
         * 均为未执行，并且把新页面入栈
         *
         */

        int j=0;
        int k=0;
        int current_j=0;
        int current_k=0;

        List<Tag> currentTagList = initTagList();
        List<Tag> currentTagList1 = initTagList();
        arrayStack.push(index);
        while(!arrayStack.isEmpty()){
            //通过初始化webClient进行访问，得到HtmlPage对象
            HtmlPage indexPage = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(500);
            webClient.setJavaScriptTimeout(0);

            if(current_j == j) {
                //如果这个页面的便签队列未全部执行结束，把最近的执行情况初始化indexPage
                indexPage.setTagList(currentTagList);
            }else {
                //初始化标签队列全部未执行
                indexPage.setTagList(currentTagList);
                indexPage.getTagList().get(current_j).setStatus(2);
            }

            if(!allTagsExecuted(indexPage))    {
                indexPage.getTagList().get(j).setStatus(1);
                currentTagList = indexPage.getTagList();
                HtmlPage hp = excuteTag(indexPage,indexPage.getTagList().get(j));
                printPageNo(hp);

                if(current_j == j)  {
                    hp.setTagList(currentTagList1);
                }else {
                    hp.setTagList(initTagList());
                    k=0;
                }

                if(!allTagsExecuted(hp)) {
                    if(k < initTagList().size())  {
                        hp.getTagList().get(k).setStatus(1);
                        currentTagList1 = hp.getTagList();
                        HtmlPage hp1 = excuteTag(hp,hp.getTagList().get(k));
                        printPageNo(hp1);
                        //第三层为叶子节点，把新得到的第三层的page收集
                        pageBox.add(hp1);
                        current_k =k;
                        k++;
                        hp.getTagList().get(current_k).setStatus(2);
                    }
                    current_j = j;
                }else {
                    HtmlPage popPage = (HtmlPage) arrayStack.pop();
                    pageBox.add(popPage);
                    if(j+1 == initTagList().size()) {
                        //最后index页面出栈
                        break;
                    }else {
                        j++;
                        arrayStack.push(hp);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TestKryo_IndexCircle.test();
    }
}


