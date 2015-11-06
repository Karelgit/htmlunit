package com.cloudpioneer.htmlUnit.SeleniumTest;

/**
 * 测试Selenium对于Ajax的测试效果，比如贵阳的分页情况
 */
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Seleniumtest {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = new HtmlUnitDriver();


        driver.get("http://gz.hrss.gov.cn/col/col41/index.html");
        Thread.sleep(2000);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        WebElement element = driver.findElement(By.xpath("//*[@id=\"300\"]/table/tbody/tr/td/table/tbody/tr/td[8]/div"));
        Thread.sleep(10000);
        for(int i=0;i<10; i++)
        {
            jse.executeScript("arguments[0].click();", element);
        }
        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Page title is: " + driver.getTitle());

        driver.quit();
    }
}
