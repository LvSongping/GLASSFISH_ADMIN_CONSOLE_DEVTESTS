package org.glassfish.admingui.devtests;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.Selenium;

public class BaseSeleniumTestClass {

    protected static WebDriver driver;
    public String baseUrl;
    public StringBuffer verificationErrors = new StringBuffer();
    public boolean acceptNextAlert = true;
    protected static Selenium selenium;
    protected static final int TIMEOUT = 90;
    public static final int TIMEOUT_CALLBACK_LOOP = 1000;
    
    protected static final Logger logger = Logger.getLogger(BaseSeleniumTestClass.class.getName());
    public static final String TRIGGER_COMMON_TASKS = "Other Tasks";
    protected static final Map<String, String> bundles = new HashMap<String, String>();
    
    
    static {
        bundles.put("i18n", "org.glassfish.admingui.core.Strings"); // core
        bundles.put("i18nUC", "org.glassfish.updatecenter.admingui.Strings"); // update center
        bundles.put("i18n_corba", "org.glassfish.corba.admingui.Strings");
        bundles.put("i18n_ejb", "org.glassfish.ejb.admingui.Strings");
        bundles.put("i18n_ejbLite", "org.glassfish.ejb-lite.admingui.Strings");
        bundles.put("i18n_jts", "org.glassfish.jts.admingui.Strings"); // JTS
        bundles.put("i18n_web", "org.glassfish.web.admingui.Strings"); // WEB
        bundles.put("common", "org.glassfish.common.admingui.Strings");
        bundles.put("i18nc", "org.glassfish.common.admingui.Strings"); // common -- apparently we use both in the app :|
        bundles.put("i18nce", "org.glassfish.admingui.community-theme.Strings");
        bundles.put("i18ncs", "org.glassfish.cluster.admingui.Strings"); // cluster
        bundles.put("i18njca", "org.glassfish.jca.admingui.Strings"); // JCA
        bundles.put("i18njdbc", "org.glassfish.jdbc.admingui.Strings"); // JDBC
        bundles.put("i18njmail", "org.glassfish.full.admingui.Strings");
        bundles.put("i18njms", "org.glassfish.jms.admingui.Strings"); // JMS
        bundles.put("theme", "org.glassfish.admingui.community-theme.Strings");

            // TODO: These conflict with core and should probably be changed in the pages
            //put("i18n", "org.glassfish.common.admingui.Strings");
            //put("i18n", "org.glassfish.web.admingui.Strings");
            //put("i18nc", "org.glassfish.web.admingui.Strings");
    }
    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "http://localhost:4848/";
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
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
    
    public void clearByIdAction(String idName){
        driver.findElement(By.id(idName)).clear();
    }
    
    public void sendKeysByIdAction(String idName, String name){
        driver.findElement(By.id(idName)).sendKeys(name);
    }
    
    protected int addTableRow(String tableId, String buttonId) {
        return addTableRow(tableId, buttonId, "Additional Properties");
    }

    protected int addTableRow(String tableId, String buttonId, String countLabel) {
        int count = getTableRowCount(tableId);
        clickAndWait(buttonId);
        return ++count;
    }
    
    protected int getTableRowCount(String id) {
        String text = getText(id);
        int count = Integer.parseInt(text.substring(text.indexOf("(") + 1, text.indexOf(")")));
        return count;
    }
    
    protected void clickAndWait(String id) {
        insureElementIsVisible(id);
        clickByIdAction(id);
    }
    
    private void insureElementIsVisible (final String id) {
        if (!id.contains("treeForm:tree")) {
            return;
        }

        try {
            WebElement element = (WebElement) driver.findElement(By.id(id));
            //driver.findElement(By.id(id));
            if (element.isDisplayed()) {
                return;
            }
        } catch (StaleElementReferenceException sere) {
        }

        final String parentId = id.substring(0, id.lastIndexOf(":"));
        final WebElement parentElement = (WebElement) driver.findElement(By.id(parentId));
//                driver.findElement(By.id(parentId));
        if (!parentElement.isDisplayed()) {
            insureElementIsVisible(parentId);
            String grandParentId = parentId.substring(0, parentId.lastIndexOf(":"));
            String nodeId = grandParentId.substring(grandParentId.lastIndexOf(":") + 1);
            clickByIdAction(grandParentId + ":" + nodeId + "_turner");
        }
    }
    
    protected void assertTableRowCount(String tableId, int count) {
        Assert.assertEquals(count, getTableRowCount(tableId));
    }
    
    public void waitForAlertProcess(String className){
        try {
            while(true){
                if (driver.findElement(By.className(className)).getText().indexOf("A long-running") != -1){
                    break;
                }
            }
            while (driver.findElement(By.className(className)).isDisplayed()) {
                sleep(1000);
            }
        } catch (StaleElementReferenceException sere) {
        }
    }
}
