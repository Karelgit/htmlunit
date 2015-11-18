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

    //刚触发的页面，便签队列均未执行
    public static boolean noTagsExecuted(HtmlPage hp)    {
        Boolean flag = true;
        for(int i=0; i<hp.getTagList().size(); i++) {
            flag = flag &&(hp.getTagList().get(i).getStatus() == 0);
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
    public static HtmlPage executeTag(HtmlPage hp,int t) throws IOException  {
        DomElement element = (DomElement) hp.getByXPath(hp.getTagList().get(t).getXpath()).get(0);
        HtmlPage page = element.click();
        return page;
    }

    //打印HtmlPage的页码
    public static void printPageNo(HtmlPage hp) {
        String pNoXpath = "//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[6]/input";
        DomElement pageNo1 = (DomElement) hp.getByXPath(pNoXpath).get(0);
        System.out.println("第" + pageNo1.getAttribute("value").toString()+"页");
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

    public static void setTagStatus(HtmlPage hp,List<Tag> currentTagList,int current_j,int j)   {
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

            setTagStatus(indexPage, currentTagList, current_j, j);
            if (!allTagsExecuted(indexPage)) {
                indexPage.getTagList().get(j).setStatus(1);
                currentTagList = indexPage.getTagList();
                HtmlPage hp1 = executeTag(indexPage, j);
                printPageNo(hp1);

                setTagStatus(hp1, currentTagList1, current_k, k);

                if(current_j == j)  {
                    hp1.setTagList(currentTagList1);
                    if(noTagsExecuted(hp1)) {
                        arrayStack.push(hp1);
                    }
                }else {
                    arrayStack.push(hp1);
                    hp1.setTagList(initTagList());
                    k = 0;
                }

                if (!allTagsExecuted(hp1)) {
                    hp1.getTagList().get(k).setStatus(1);
                    currentTagList1 = hp1.getTagList();
                    HtmlPage hp2 = executeTag(hp1, k);
                    printPageNo(hp2);

                    if (current_k == k) {
                        hp2.setTagList(currentTagList2);
                        if(noTagsExecuted(hp2)) {
                            arrayStack.push(hp2);
                        }
                    } else {
                        arrayStack.push(hp2);
                        hp2.setTagList(initTagList());
                        m = 0;
                    }

                    if (!allTagsExecuted(hp2)) {
                        hp2.getTagList().get(m).setStatus(1);
                        currentTagList2 = hp2.getTagList();
                        HtmlPage hp3 = executeTag(hp2, m);
                        printPageNo(hp3);
                        //第三层为叶子节点，把新得到的第三层的page收集
                        pageBox.add(hp3);
                        current_m = m;
                        m++;
                        hp2.getTagList().get(current_m).setStatus(2);
                        current_k = k;
                    } else {
                        HtmlPage popPage = (HtmlPage) arrayStack.pop();
                        pageBox.add(popPage);
                        if (k + 1 <= initTagList().size()) {
                            k++;
                        }
                    }
                    current_j = j;
                } else {
                    HtmlPage popPage = (HtmlPage) arrayStack.pop();
                    pageBox.add(popPage);
                    if (j + 1 == initTagList().size()) {
                            arrayStack.pop();
                            break;
                        } else {
                            j++;
                    }

                }
            }
        }
        System.out.println("pageBox页面有：" + "\n");
        for (HtmlPage htmlPage : pageBox) {
            printPageNo(htmlPage);
        }
    }

    public static void main(String[] args) throws Exception {
        TestKryo_IndexCircle.test();
    }
}


