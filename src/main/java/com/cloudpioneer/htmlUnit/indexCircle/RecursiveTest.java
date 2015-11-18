package com.cloudpioneer.htmlUnit.indexCircle;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.tag.Tag;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.taskdefs.Javadoc;

import java.io.IOException;
import java.util.*;


/**
 * 每一次都从index进行往下触发，避免Ajax无法回复历史状态
 * Created by Karel on 2015/11/16.
 */
public class RecursiveTest   {
    /**
     * k,控制访问层数
     * m,叶子节点TagList循环控制
     */
    public static int m = 0;
    public static int k = 5;

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
        tag1.setXpath("//*[@id=\"11627\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        tag2.setXpath("//*[@id=\"11627\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");
        tag1.setStatus(0);
        tag2.setStatus(0);
        tagList.add(tag1);
        tagList.add(tag2);

        return tagList;
    }

    //点击标签，t为HtmlPage页面待选标签中即将执行的标签
    public static HtmlPage executeTag(HtmlPage hp,List<Tag> tagList,int t) throws IOException {
        DomElement element = (DomElement) hp.getByXPath(tagList.get(t).getXpath()).get(0);
        HtmlPage page = element.click();
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

    public static void recursive(HtmlPage hp,int k,int m,Map<Integer,Param> loopBlock,Map<Integer,List<Tag>> tagListBlock,HashSet<String> pageBox,ArrayStack arrayStack,WebClient webClient) throws IOException{
        if (k == 1) {
            List<Tag> tagList = tagListBlock.get(k);
            if (!allTagsExecuted(tagList)) {
                tagList.get(m).setStatus(1);
                HtmlPage seed_hp = executeTag(hp,tagList,m);
                tagList.get(m).setStatus(2);
                printPageNo(seed_hp);
                pageBox.add(seed_hp.asXml());
                loopBlock.get(k+1).setCurrentParam(loopBlock.get(k + 1).getParam());
            }else   {
                HtmlPage popPage = (HtmlPage) arrayStack.pop();
                loopBlock.get(k+1).setParam(loopBlock.get(k+1).getParam()+1);
            }
        }else {
            while(!arrayStack.isEmpty())    {
                List<Tag> tagList = tagListBlock.get(k);
                Param loopParam = loopBlock.get(k);
                setTagStatus(tagList,loopParam);
                String url = "http://www.gygov.gov.cn/col/col10682/index.html";
                HtmlPage indexPage = webClient.getPage(url);
                webClient.waitForBackgroundJavaScript(500);
                webClient.setJavaScriptTimeout(0);
                HtmlPage initPage = null;
                int layers = loopBlock.size();
                if(k<layers)    {
                    for(; k<layers; layers--)    {
                        initPage = executeTag(indexPage,tagListBlock.get(layers),loopBlock.get(layers).getParam());
                    }
                }else {
                    initPage = indexPage;
                }
                if(!allTagsExecuted(tagList))    {
                    tagList.get(loopParam.getParam()).setStatus(1);
                    HtmlPage childPage = executeTag(initPage,tagList,loopParam.getParam());
                    printPageNo(childPage);
                    setTagStatus(tagListBlock.get(k - 1), loopBlock.get(k - 1));
                    if(loopParam.getCurrentParam() == loopParam.getParam())  {
                        List<Tag> childTagList = tagListBlock.get(k-1);
                        if(noTagsExecuted(childTagList)) {
                            arrayStack.push(childPage);
                            pageBox.add(childPage.asXml());
                        }
                    }else {
                        //判断当前节点已经执行完毕，切换到兄弟节点，初始化兄弟节点下所有标签状况
                        arrayStack.push(childPage);
                        pageBox.add(childPage.asXml());
                        for(int i=k-1; i>0; i--)  {
                            tagListBlock.put(i, initTagList());
                            Param param = new Param();
                            param.setParam(0);
                            param.setCurrentParam(0);
                            loopBlock.put(i,param);
                        }
                        loopBlock.get(k).setCurrentParam(loopBlock.get(k).getParam());
                     }


                    recursive(childPage,k-1,m,loopBlock,tagListBlock,pageBox,arrayStack,webClient);
                    if(m == initTagList().size())  {
                        m = 0;
                    }else {
                        m++;
                    }
                } else   {
                    HtmlPage popPage = (HtmlPage) arrayStack.pop();
                    pageBox.add(popPage.asXml());
                    if(k+1<=loopBlock.size()) {
                        loopBlock.get(k+1).setParam(loopBlock.get(k+1).getParam()+1);
                        k++;
                    }
                }
            }
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

        List<Tag> currentList = initTagList();
        arrayStack.push(index);
        Map<Integer,List<Tag>> tagListBlock = new HashMap<Integer,List<Tag>>();
        Map<Integer,Param> loopBlock= new HashMap<Integer,Param>();


        //初始化标签列表
        for(int j=0; j<k-1; j++)  {
          tagListBlock.put(j+1, initTagList());
        }

        //初始化循环变量
        for(int n=0; n<k-1; n++)  {
            Param param = new Param();
            param.setCurrentParam(0);
            param.setParam(0);
            loopBlock.put(n+1,param);
        }

        System.out.println("ok");
        recursive(index, k-1, m, loopBlock, tagListBlock, pageBox, arrayStack,webClient);
        System.out.println(pageBox.size());
    }

    public static void main(String[] args) throws Exception {
        Long t1 = System.currentTimeMillis();
        RecursiveTest.test();
        Long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
    }
}


