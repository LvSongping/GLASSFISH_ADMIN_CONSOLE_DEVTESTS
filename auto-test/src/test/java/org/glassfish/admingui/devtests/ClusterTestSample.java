package org.glassfish.admingui.devtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

/**
*
* @author Jeremy Lv
*/
public class ClusterTestSample extends BaseSeleniumTestClass {

    public static final String ID_CLUSTERS_TABLE = "propertyForm:clustersTable";
    public static final String ID_INSTANCES_TABLE = "propertyForm:instancesTable";
    public static final String ID_CLUSTERS_DELETE_BTN = "propertyForm:clustersTable:topActionsGroup1:button1";
    public static final String ID_CLUSTERS_START_BTN = "propertyForm:clustersTable:topActionsGroup1:button2";
    public static final String ID_CLUSTERS_STOP_BTN = "propertyForm:clustersTable:topActionsGroup1:button3";
    public static final String ID_INSTANCES_START_BTN = "propertyForm:instancesTable:topActionsGroup1:button2";

//    @Test
//    public void testClusterCreationAndDeletion() throws Exception {
//        String clusterName = "clusterName" + generateRandomString();
//        String instanceName = "instanceName" + generateRandomString();
//        createCluster(clusterName, instanceName);
//        try {
//            assertEquals(clusterName, driver.findElement(By.id("propertyForm:clustersTable:rowGroup1:0:col1:link")).getText());
//            assertEquals(instanceName, driver.findElement(By.id("propertyForm:clustersTable:rowGroup1:0:col3:iLink")).getText());
//        } catch (Error e) {
//            verificationErrors.append(e.toString());
//        }
//        deleteCluster(clusterName);
//    }

//    @Test
//    public void testStartAndStopClusterWithOneInstance() {
//        String clusterName = "clusterName" + generateRandomString();
//        String instanceName = "instanceName" + generateRandomString();
//        createCluster(clusterName, instanceName);
//
//        // Verify cluster information in table
//        String prefix = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1");
//        try {
//            assertEquals(clusterName, getText(prefix + "col1:link"));
//            assertEquals(clusterName + "-config", getText(prefix + "col2:configlink"));
//            assertEquals(instanceName, getText(prefix + "col3:iLink"));
//        } catch (Error e) {
//            verificationErrors.append(e.toString());
//        };
//
//        // Start the cluster and verify
//        startSpecifiedCluster(ID_CLUSTERS_START_BTN, ID_CLUSTERS_TABLE, clusterName);
//        try {
//            assertTrue((getText(prefix + "col3").endsWith("Running")));
//        } catch (Error e) {
//            verificationErrors.append(e.toString());
//        };
//
//        // Stop the cluster and verify
//        stopSpecifiedCluster(ID_CLUSTERS_START_BTN, ID_CLUSTERS_TABLE, clusterName);
//        try {
//            assertTrue((getText(prefix + "col3").endsWith("Stopped")));
//        } catch (Error e) {
//            verificationErrors.append(e.toString());
//        };
//        
//        deleteCluster(clusterName);
//    }

//    @Test
//    public void testMigrateEjbTimers() {
//        String clusterName = "clusterName" + generateRandomString();
//        String instanceName1 = "instanceName" + generateRandomString();
//        String instanceName2 = "instanceName" + generateRandomString();
//        createCluster(clusterName, instanceName1, instanceName2);
//        
//        //Start cluster instance
//        startClusterInstance(instanceName1);
//        //start to test
//        startTestMigrateEjbTimers()
//        ;
//        deleteCluster(clusterName);
//    }

//    @Test
//    public void verifyClusterGeneralInformationPage() {
//        String clusterName = "clusterName" + generateRandomString();
//        String instanceName1 = "instanceName" + generateRandomString();
//        String instanceName2 = "instanceName" + generateRandomString();
//        createCluster(clusterName, instanceName1, instanceName2);
//        
//        startVerifyClusterGeneralInformationPage(clusterName);
//        
////        deleteCluster(clusterName);
//    }
    

//    @Test
//    public void testClusterInstancesTab() {
//        String clusterName = "clusterName" + generateRandomString();
//        String instanceName1 = "instanceName" + generateRandomString();
//        String instanceName2 = "instanceName" + generateRandomString();
//        createCluster(clusterName, instanceName1);
//        
//        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
//        clickByIdAction(clickId);
//        clickByIdAction("propertyForm:clusterTabs:clusterInst");
//        //Check whether the instance is already created
//        assertEquals(instanceName1, getText(getTableRowByValue(ID_INSTANCES_TABLE, instanceName1, "col1") + "col1:link"));
//        
//        clickByIdAction("propertyForm:instancesTable:topActionsGroup1:newButton");
//        setFieldValue("propertyForm:propertySheet:propertSectionTextField:NameTextProp:NameText", instanceName2);
//        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
//        
//        //Check whether the instance is already created
//        assertEquals(instanceName2, getText(getTableRowByValue(ID_INSTANCES_TABLE, instanceName2, "col1") + "col1:link"));
//        
////        deleteCluster(clusterName);
//    }
    
//    //Todo: Need to finish all of the property tests
//    @Test
//    public void testProperties() {
//        String clusterName = "clusterName" + generateRandomString();
//        String instanceName1 = "instanceName" + generateRandomString();
//        createCluster(clusterName, instanceName1);
//        
//        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
//        assertEquals(clusterName, getText(clickId));
//        
//        
//        clickByIdAction(clickId);
//        clickByIdAction("propertyForm:clusterTabs:clusterProps");
//        
//        
//    }
    
//    //Done
//    @Test
//    public void testMultiDeleteClusters() {
//        String clusterName1 = "cluster" + generateRandomString();
//        String clusterName2 = "cluster" + generateRandomString();
//
//        createCluster(clusterName1);
//        createCluster(clusterName2);
//        deleteAllCluster();
//
//        try {
//            assertEquals("No items found.", driver.findElement(By.id("propertyForm:clustersTable:rowGroup1:_emptyDataColumn:_emptyDataText")).getText());
//        } catch (Error e) {
//            verificationErrors.append(e.toString());
//        }
//    }
    
    @Test
    public void testClusterWithJmsOptions() {
        String clusterName = "cluster" + generateRandomString();
        gotoClusterPage();
        driver.findElement(By.id("propertyForm:clustersTable:topActionsGroup1:newButton")).click();
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText")).sendKeys(clusterName);
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:jmsConfigTypeProp:optCustom:optCustom_label")).click();
        sleep(1000);
        driver.findElement(By.id("propertyForm:jmsTypePropertySheet:jmsTypeSection:jmsTypeProp:optLocal")).click();
        driver.findElement(By.id("propertyForm:jmsPropertySheet:configureJmsClusterSection:ClusterTypeProp:optConventional")).click();
        
        Select select = new Select(driver.findElement(By.id("propertyForm:jmsPropertySheet:configureJmsClusterSection:ConfigStoreTypeProp:configStoreType")));
        select.selectByVisibleText("Master Broker");
        
        Select select1 = new Select(driver.findElement(By.id("propertyForm:jmsPropertySheet:configureJmsClusterSection:MessageStoreTypeProp:messageStoreType")));
        select1.selectByVisibleText("File");
        
        setFieldValue("propertyForm:jmsPropertySheet:configureJmsClusterSection:PropertiesProp:properties", "prop1=value1:prop2=value2\\:with\\:colons:prop3=value3");

        driver.findElement(By.id("propertyForm:basicTable:topActionsGroup1:addSharedTableButton")).click();
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).clear();
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).sendKeys(clusterName + "in1");
        driver.findElement(By.id("propertyForm:basicTable:topActionsGroup1:addSharedTableButton")).click();
        sleep(500);
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).clear();
        sleep(500);
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).sendKeys(clusterName + "in2");
        driver.findElement(By.id("propertyForm:propertyContentPage:topButtons:newButton")).click();
        
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        driver.findElement(By.id(clickId)).click();
        assertEquals(clusterName, getText("propertyForm:propertySheet:propertSectionTextField:clusterNameProp:clusterName"));
    }
    
//    @Test
//    public void testClusterWithEnhancedJmsOptions() {
//        
//    }
//    
//    @Test
//    public void testClusterWithBadJmsOptions() {
//        
//    }
//    
    
    
    /**
     *  Cluster related methods
     */
    private void startSpecifiedCluster(String string, String idClustersTable,
            String clusterName) {
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col0:select";
        driver.findElement(By.id(clickId)).click();
        driver.findElement(By.id(ID_CLUSTERS_START_BTN)).click();
        assertTrue(closeAlertAndGetItsText().matches("^Start the selected clusters[\\s\\S]$"));
        sleep(20000);
    }

    private void stopSpecifiedCluster(String idClustersStartBtn,
            String idClustersTable, String clusterName) {
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col0:select";
        driver.findElement(By.id(clickId)).click();
        driver.findElement(By.id(ID_CLUSTERS_STOP_BTN)).click();
        assertTrue(closeAlertAndGetItsText().matches("^Stop the selected clusters[\\s\\S]$"));
        sleep(10000);
    }
    
    public void createCluster(String clusterName){
        gotoClusterPage();
        driver.findElement(By.id("propertyForm:clustersTable:topActionsGroup1:newButton")).click();
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText")).clear();
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText")).sendKeys(clusterName);
        driver.findElement(By.id("propertyForm:propertyContentPage:topButtons:newButton")).click();
    }
    
    public void createCluster(String clusterName, String instanceName){
        gotoClusterPage();
        driver.findElement(By.id("propertyForm:clustersTable:topActionsGroup1:newButton")).click();
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText")).clear();
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText")).sendKeys(clusterName);
        driver.findElement(By.id("propertyForm:basicTable:topActionsGroup1:addSharedTableButton")).click();
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).clear();
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).sendKeys(instanceName);
        driver.findElement(By.id("propertyForm:propertyContentPage:topButtons:newButton")).click();
    }
    
    public void createCluster(String clusterName, String instanceName, String instanceName1){
        gotoClusterPage();
        driver.findElement(By.id("propertyForm:clustersTable:topActionsGroup1:newButton")).click();
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText")).clear();
        driver.findElement(By.id("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText")).sendKeys(clusterName);
        driver.findElement(By.id("propertyForm:basicTable:topActionsGroup1:addSharedTableButton")).click();
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).clear();
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).sendKeys(instanceName);
        driver.findElement(By.id("propertyForm:basicTable:topActionsGroup1:addSharedTableButton")).click();
        sleep(500);
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).clear();
        sleep(500);
        driver.findElement(By.id("propertyForm:basicTable:rowGroup1:0:col2:name")).sendKeys(instanceName1);
        driver.findElement(By.id("propertyForm:propertyContentPage:topButtons:newButton")).click();
    }
    
    private void deleteCluster(String clusterName) {
        gotoClusterPage();
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col0:select";
        driver.findElement(By.id(clickId)).click();
        driver.findElement(By.id(ID_CLUSTERS_DELETE_BTN)).click();
        assertTrue(closeAlertAndGetItsText().matches("^Delete the selected clusters and their instances[\\s\\S]$"));
        sleep(15000);
    }
    
    public void deleteAllCluster(){
        driver.findElement(By.id("propertyForm:clustersTable:_tableActionsTop:_selectMultipleButton:_selectMultipleButton_image")).click();
        driver.findElement(By.id(ID_CLUSTERS_DELETE_BTN)).click();
        assertTrue(closeAlertAndGetItsText().matches("^Delete the selected clusters and their instances[\\s\\S]$"));
    }
    
    public void gotoClusterPage(){
        driver.get(baseUrl + "/common/index.jsf");
        driver.findElement(By.id("treeForm:tree:clusterTreeNode:clusterTreeNode_link")).click();
    }
    
    /**
     * Instance related methods
     */
    private void startClusterInstance(String instanceName) {
        driver.findElement(By.id("propertyForm:clustersTable:rowGroup1:0:col1:link")).click();
        driver.findElement(By.id("propertyForm:clusterTabs:clusterInst")).click();
        String clickId = getTableRowByValue(ID_INSTANCES_TABLE, instanceName, "col1")+"col0:select";
        driver.findElement(By.id(clickId)).click();
        driver.findElement(By.id(ID_INSTANCES_START_BTN)).click();
        closeAlertAndGetItsText();
        sleep(15000);
    }
    
    /**
     * Test related methods
     */
    
    private void startTestMigrateEjbTimers() {
        gotoClusterPage();
        driver.findElement(By.id("propertyForm:clustersTable:rowGroup1:0:col1:link")).click();
        sleep(10000);
        driver.findElement(By.id("propertyForm:clusterTabs:clusterInst")).click();
        sleep(1000);
        driver.findElement(By.id("propertyForm:clusterTabs:general")).click();
        sleep(1000);
        driver.findElement(By.id("propertyForm:migrateTimesButton")).click();
        sleep(1000);
        driver.findElement(By.id("propertyForm:propertyContentPage:topButtons:saveButton")).click();
        assertTrue(driver.findElement(By.className("header_sun4")).getText().indexOf("Migrated 0 timers") != -1);
    }
    
    private void startVerifyClusterGeneralInformationPage(String clusterName) {
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        driver.findElement(By.id(clickId)).click();
        
        
        assertEquals(clusterName, getText("propertyForm:propertySheet:propertSectionTextField:clusterNameProp:clusterName"));
        assertEquals(clusterName + "-config", getText("propertyForm:propertySheet:propertSectionTextField:configNameProp:configlink"));
        
        driver.findElement(By.id("propertyForm:propertySheet:propertSectionTextField:configNameProp:configlink")).click();
        driver.findElement(By.id("treeForm:tree:clusterTreeNode:clusterTreeNode_link")).click();
        driver.findElement(By.id(clickId)).click();
        
        assertEquals("2 instances are stopped", getText("propertyForm:propertySheet:propertSectionTextField:instanceStatusProp:instanceStatusStopped"));
        
        setFieldValue("propertyForm:propertySheet:propertSectionTextField:gmsMulticastPort:gmsMulticastPort", "12345");
        setFieldValue("propertyForm:propertySheet:propertSectionTextField:gmsMulticastAddress:gmsMulticastAddress", "123.234.456.88");
        setFieldValue("propertyForm:propertySheet:propertSectionTextField:GmsBindInterfaceAddress:GmsBindInterfaceAddress", "${ABCDE}");
        clickByIdAction("propertyForm:propertySheet:propertSectionTextField:gmsEnabledProp:gmscb");
        
        clickByIdAction("propertyForm:propertyContentPage:topButtons:saveButton");
        
        //ensure value is saved correctly
        assertEquals("12345", getValue("propertyForm:propertySheet:propertSectionTextField:gmsMulticastPort:gmsMulticastPort","value"));
        assertEquals("123.234.456.88", getValue("propertyForm:propertySheet:propertSectionTextField:gmsMulticastAddress:gmsMulticastAddress","value"));
        assertEquals("${ABCDE}", getValue("propertyForm:propertySheet:propertSectionTextField:GmsBindInterfaceAddress:GmsBindInterfaceAddress","value"));
        assertEquals(false, driver.findElement(By.id("propertyForm:propertySheet:propertSectionTextField:gmsEnabledProp:gmscb")).isSelected());
    }
    
    /**
     * Common util methods
     */
    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    private String closeAlertAndGetItsText() {
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
    

}
