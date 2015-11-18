package com.cloudpioneer.htmlUnit.indexCircle;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.tag.Tag;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;
import com.nylqd.*;



public class Recursive_width {
    /**
     * k,控制访问层数
     * m,叶子节点TagList循环控制
     */
    public static int m = 0;
    public static int k = 3;
    public static int layers = 3;

    //判断一个页面中子标签是否全部已经执行
    public static boolean allTagsExecuted(List<Tag> tagList)    {
        Boolean flag = true;
        for(int i=0; i<tagList.size(); i++) {
            flag = flag &&(tagList.get(i).getStatus() == 2);
        }
        return flag;
    }

    //刚触发的页面，便签队列均未执行
    public static boolean noTagsExecuted(List<Tag> childTagList)    {
        Boolean flag = true;
        for(int i=0; i<childTagList.size(); i++) {
            flag = flag &&(childTagList.get(i).getStatus() == 0);
        }
        return flag;
    }
    //给每个HtmlPage页面设置备选标签列表
    public static List<Tag> initTagList()    {
        List<Tag> tagList = new ArrayList<Tag>();
        Tag tag1 = new Tag();
        Tag tag2 = new Tag();
        tag1.setXpath("//*[@id=\"11627\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div|");
        tag2.setXpath("//*[@id=\"11627\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div|");
        tag1.setStatus(0);
        tag2.setStatus(0);
        tagList.add(tag1);
        tagList.add(tag2);

        return tagList;
    }

    //点击标签，t为HtmlPage页面待选标签中即将执行的标签
    public static HtmlPage executeTag(HtmlPage hp,List<Tag> tagList,int t) throws IOException {
        String [] xpathArray = tagList.get(t).getXpath().split("\\|");
        HtmlPage page = null;
        for(int i=0; i<xpathArray.length; i++)  {
           // System.out.println(xpathArray[i].toString());
            if(!xpathArray[i].isEmpty())    {
                DomElement element = (DomElement) hp.getByXPath(xpathArray[i]).get(0);
                page = element.click();
            }
        }
        return page;
    }

    //打印HtmlPage的页码
    public static void printPageNo(HtmlPage hp) {
        String pNoXpath = "//*[@id=\"11627\"]/table/tbody/tr/td/table/tbody/tr/td[6]/input";
        DomElement pageNo1 = (DomElement) hp.getByXPath(pNoXpath).get(0);
        System.out.println("第" + pageNo1.getAttribute("value").toString() + "页");
    }



    /**
     *设置tagList状态,如果执行的标签改变了，置上一个标签指向的页面状态为已执行（Status:2）
     */

    public static void setTagStatus(List<Tag> tagList,Param param)   {
        if(param.getCurrentParam() != param.getParam()) {
            //如果子节点有一个已全部执行结束，把最近的执行情况初始化indexPage，并把此标签执行status设为2
            tagList.get(param.getCurrentParam()).setStatus(2);
        }
    }

    public static void Traverse_width(int k,int m,int layers,Map<Integer,List<Tag>> tagListBlock,WebClient webClient) throws IOException{
        HashSet<String> pageHashCell = new HashSet<String>();
        List<String> pagesList = new ArrayList<String>();
        while (k >1)   {
            String url = "http://www.gygov.gov.cn/col/col10682/index.html";
            if(k+1 <= layers)  {
                for(int i=0; i < tagListBlock.get(k+1).size(); i++) {
                    for(int j=0; j < initTagList().size(); j++) {
                        Tag tag = new Tag();
                        tag.setXpath(tagListBlock.get(k + 1).get(i).getXpath() + initTagList().get(j).getXpath() + "|");
                        tag.setStatus(1);
                        tagListBlock.get(k).add(tag);
                    }
                }
            }else {
                tagListBlock.put(k,initTagList());
            }

            for(;m < tagListBlock.get(k).size();m++)  {
                HtmlPage indexPage = webClient.getPage(url);
                HtmlPage htmlPage = executeTag(indexPage, tagListBlock.get(k), m);
                printPageNo(htmlPage);
                org.jsoup.nodes.Document doc = Jsoup.parse(htmlPage.asText());
                if(pagesList.isEmpty()) {
                    pagesList.add(htmlPage.asText());
                }else {
                    boolean uniqueFlag = false;
                    for(int i=0; i<pagesList.size(); i++)   {
//                        uniqueFlag = uniqueFlag &&
                    }
                }
                pageHashCell.add(htmlPage.asText());
            }
            m = 0;// next layer loop count
            k--;//layers param decrease
        }
        System.out.println("page size: " + pageHashCell.size());
        for (String s : pageHashCell) {
            System.out.println(s);
        }
    }


    public static void test() throws IOException {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        String url = "http://www.gygov.gov.cn/col/col10682/index.html";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setJavaScriptTimeout(3600 * 1000);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(3600 * 1000);
        webClient.waitForBackgroundJavaScript(600 * 1000);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        HtmlPage index = webClient.getPage(url);

        //使用堆栈对每个HtmlPage进行存储
        ArrayStack arrayStack = new ArrayStack();

        //到每一层，如果当前节点里面的备选标签status=2,进行页面的收集到pageBox
        HashSet<String> pageBox = new HashSet<String>();

        arrayStack.push(index);
        Map<Integer,List<Tag>> tagListBlock = new HashMap<Integer,List<Tag>>();
        Map<Integer,Param> loopBlock= new HashMap<Integer,Param>();


        //初始化标签列表
        for(int i=k; i>0; i--)  {
            tagListBlock.put(i, new ArrayList<Tag>());
        }

        //初始化循环变量
        for(int n=0; n<k-1; n++)  {
            Param param = new Param();
            param.setCurrentParam(0);
            param.setParam(0);
            loopBlock.put(n+1,param);
        }

        int m = 0;
        System.out.println("ok");
        Traverse_width(k,m,layers,tagListBlock,webClient);
        System.out.println(pageBox.size());
    }

    public static void main(String[] args) throws Exception {
        Long t1 = System.currentTimeMillis();
        Recursive_width.test();
        Long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
    }
}


