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

    //点击标签，t为HtmlPage页面待选标签中马上执行的标签
    public static HtmlPage excuteTag(List<Tag> currentTagList, HtmlPage hp,int t) throws IOException  {
        hp.getTagList().get(t).setStatus(1);
        currentTagList = hp.getTagList();
        DomElement element = (DomElement) hp.getByXPath(hp.getTagList().get(t).getXpath()).get(0);
        HtmlPage page = element.click();
        return page;
    }

    //打印HtmlPage的页码
    public static void printPageNo(HtmlPage hp) {
        String pNoXpath = "//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[6]/input";
        DomElement pageNo1 = (DomElement) hp.getByXPath(pNoXpath).get(0);
        System.out.println("第" + pageNo1.getAttribute("value").toString()+"页" + "\n");
    }

    //标签队列执行完毕，当前节点出栈，下一节点入栈
    public static void pushNextPage(HtmlPage hp)   {

    }

    /**
     * 非叶子节点，如果HtmlPage页面还有未执行的tag，把currentTagList状态赋值给父HtmlPage
     * 如果HtmlPage已执行完所有tag,除了把currentTagList状态赋值给父HtmlPage，还要把对应改页面的
     * 标签status设为2
     *
     */

    public static void alterTagStatus(HtmlPage hp,List<Tag> currentTagList,int current_j,int j)   {
        if(current_j == j) {
            //如果这个页面的便签队列未全部执行结束，把最近的执行情况初始化indexPage
            hp.setTagList(currentTagList);
        }else {
            //如果子节点有一个已全部执行结束，把最近的执行情况初始化indexPage，并把此标签执行status设为2
            hp.setTagList(currentTagList);
            hp.getTagList().get(current_j).setStatus(2);
        }
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
        HtmlPage index = webClient.getPage(url);
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

        int j = 0;
        int current_j = 0;
        int k = 0;
        int current_k = 0;
        int m = 0;
        int current_m = 0;

        List<Tag> currentTagList = initTagList();
        List<Tag> currentTagList1 = initTagList();
        List<Tag> currentTagList2 = initTagList();
        arrayStack.push(index);
        while (!arrayStack.isEmpty()) {
            //通过初始化webClient进行访问，得到HtmlPage对象
            HtmlPage indexPage = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(500);
            webClient.setJavaScriptTimeout(0);

            alterTagStatus(indexPage, currentTagList, current_j, j);
            if (!allTagsExecuted(indexPage)) {
                HtmlPage hp = excuteTag(currentTagList, indexPage, j);
                printPageNo(hp);


                if (!allTagsExecuted(hp)) {
                    HtmlPage hp2 = excuteTag(currentTagList2, hp, k);
                    printPageNo(hp2);

                    if (current_j == j) {
                        hp2.setTagList(currentTagList1);
                    } else {
                        hp.setTagList(initTagList());
                        k = 0;
                    }

                    if (!allTagsExecuted(hp2)) {
                        if (m < initTagList().size()) {
                            HtmlPage hp1 = excuteTag(currentTagList1, hp, m);
                            printPageNo(hp1);
                            //第三层为叶子节点，把新得到的第三层的page收集
                            pageBox.add(hp1);
                            current_m = m;
                            m++;
                            hp.getTagList().get(current_m).setStatus(2);
                        }
                        current_m = m;
                    } else {
                        HtmlPage popPage = (HtmlPage) arrayStack.pop();
                        pageBox.add(popPage);
                        if (j + 1 == initTagList().size()) {
                            break;
                        } else {
                            j++;
                            arrayStack.push(hp);
                        }
                    }
                } else {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        TestKryo_IndexCircle.test();
    }
}


