/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.thoughtworks.selenium.Selenium;
/**
 * 
 * @author jeremylv
 *
 */
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
    
    public String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String msg = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return msg;
        } finally {
            acceptNextAlert = true;
        }
    }
    
    public void isElementPresent(String idName){
        final WebElement element = (WebElement) driver.findElement(By.id(idName));
        while(!element.isDisplayed()){
            sleep(500);
        }
    }
    
    public void isCheckboxSelected(String checkbox){
        while(driver.findElement(By.id(checkbox)).isSelected()){
            sleep(500);
        }
    }
}
