package org.glassfish.admingui.devtests;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;
import com.thoughtworks.selenium.Selenium;

public class BaseSeleniumTestClass {

    protected static WebDriver driver;
    public String baseUrl;
    public StringBuffer verificationErrors = new StringBuffer();
    public boolean acceptNextAlert = true;
    protected static Selenium selenium;
    

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:4848/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }
    
    // *************************************************************************
    // Wrappers for Selenium API
    // *************************************************************************

    protected String generateRandomString() {
        SecureRandom random = new SecureRandom();

        // prepend a letter to insure valid JSF ID, which is causing failures in some areas
        return "a" + new BigInteger(130, random).toString(16);
    }
    
    protected String getTableRowByValue(String tableId, String value, String valueColId) {
        try {
            int row = 0;
            while (true) { // iterate over any rows
                // Assume one row group for now and hope it doesn't bite us
                String text = getText(tableId + ":rowGroup1:" + row + ":" + valueColId);
                if (text.equals(value)) {
                    return tableId + ":rowGroup1:" + row + ":";
                }
                row++;
            }
        } catch (Exception e) {
            Assert.fail("The specified row was not found: " + value);
            return "";
        }
    }
    
    /**
     * Gets the text of an element.
     * @param elem
     * @return
     */
    public String getText(String elem) {
        return driver.findElement(By.id(elem)).getText();
    }
    
    /**
     * Gets the value of an element.
     * @param elem
     * @return
     */
    public String getValue(String elem, String attribute) {
        return driver.findElement(By.id(elem)).getAttribute(attribute);
    }
    
    public void sleep(int milli){
    	try {
			Thread.sleep(milli);
		} catch (InterruptedException e) {
		}
    }
    
    /**
     * Types the specified text into the requested element
     * @param elem
     * @param text
     */
    public void setFieldValue(String elem, String text) {
        driver.findElement(By.id(elem)).clear();
        driver.findElement(By.id(elem)).sendKeys(text);
    }
    
    public void clickByIdAction(String idName){
        driver.findElement(By.id(idName)).click();
    }
}
