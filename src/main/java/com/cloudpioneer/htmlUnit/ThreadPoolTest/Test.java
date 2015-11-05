package com.cloudpioneer.htmlUnit.ThreadPoolTest;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2015/11/5.
 */
public class Test {
    public static void main(String[] args) throws Exception{
        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 200, 1000*60, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
        BlockingQueue<HtmlPage> blockingQueue = new LinkedBlockingQueue<HtmlPage>();
        List<HtmlPage> pageList = new ArrayList<HtmlPage>();
        List<String> elements = new LinkedList<String>();
        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[4]/div");
        elements.add("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div");


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
        HtmlPage page = webClient.getPage(url);
        blockingQueue.offer(page);
        System.out.println("main blockingQueue: " + blockingQueue.size());

        for(int index=0; index< blockingQueue.size(); index++)  {
            System.out.println("blockingQueue:" + blockingQueue);

            HtmlPage indexPage = blockingQueue.poll();
            for(int i=0;i<elements.size();i++){
                MyTask myTask = new MyTask(i,elements.get(i),pageList,indexPage,blockingQueue);
                executor.execute(myTask);
                System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                        executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
            }

//            executor.shutdown();

            try {
                boolean loop = true;
                do {    //等待所有任务完成
                    loop = !executor.awaitTermination(2, TimeUnit.SECONDS);  //阻塞，直到线程池里所有任务结束
                } while(loop);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            System.out.println("页面数：" + pageList.size());
//            System.out.println("queue size: " + blockingQueue.size());

        }

    }

}


class MyTask implements Runnable {
    private int taskNum;
    private String element;
    private HtmlPage indexPage;
    private List<HtmlPage>  pageList;
    private BlockingQueue<HtmlPage> blockingQueue;

  /*  public BlockingQueue<HtmlPage> getBlockingQueue() {
        return blockingQueue;
    }

    public void setBlockingQueue(BlockingQueue<HtmlPage> blockingQueue) {
        this.blockingQueue = blockingQueue;
    }*/

    public MyTask(int num,String e,List<HtmlPage> pageList,HtmlPage indexPage,BlockingQueue<HtmlPage> blockingQueue) {
        this.taskNum = num;
        this.element = e;
        this.pageList = pageList;
        this.indexPage = indexPage;
        this.blockingQueue =blockingQueue;
    }

    public void run(){
//        System.out.println("正在执行标签 "+taskNum);
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

        HtmlPage page = null;
        try {
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DomElement de = ((DomElement) indexPage.getByXPath(element).get(0));
        try {
            HtmlPage newPage = de.click();
//            System.out.println("newPage: " + "\n\n" + newPage.asText());

            Thread.currentThread().sleep(4000);
            pageList.add(newPage);
            blockingQueue.offer(newPage);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        System.out.println("标签 "+taskNum+"执行完毕");
    }
}
