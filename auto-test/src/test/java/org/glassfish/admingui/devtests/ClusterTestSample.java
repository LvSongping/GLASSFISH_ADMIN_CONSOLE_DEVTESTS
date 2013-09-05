package org.glassfish.admingui.devtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;



import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.Select;

/**
*The CLusterTest is used to test the cluster related pages
* @author Jeremy Lv
*/
public class ClusterTestSample extends BaseSeleniumTestClass {

    public static final String ID_CLUSTERS_TABLE = "propertyForm:clustersTable";
    public static final String ID_INSTANCES_TABLE = "propertyForm:instancesTable";
    public static final String ID_CLUSTERS_DELETE_BTN = "propertyForm:clustersTable:topActionsGroup1:button1";
    public static final String ID_CLUSTERS_START_BTN = "propertyForm:clustersTable:topActionsGroup1:button2";
    public static final String ID_CLUSTERS_STOP_BTN = "propertyForm:clustersTable:topActionsGroup1:button3";
    public static final String ID_INSTANCES_START_BTN = "propertyForm:instancesTable:topActionsGroup1:button2";
    public static final String ID_INSTANCES_STOP_BTN = "propertyForm:instancesTable:topActionsGroup1:button3";
    
    public static final String TRIGGER_CLUSTER_NO_RUNNING_INSTANCES = "i18ncs.cluster.migrateEjbTimersNoRunningInstance";
    public static final String TRIGGER_CONFIGURATION_TEXT = "i18n.configuration.pageTitleHelp";
    public static final String TRIGGER_MIGRATE_EJB_TIMERS = "i18ncs.cluster.migrateEjbTimersHelp";
    public static final String TRIGGER_CLUSTER_PAGE = "i18ncs.clusters.PageTitleHelp";
    public static final String TRIGGER_NEW_CLUSTER_PAGE = "i18ncs.clusterNew.PageTitleHelp";
    public static final String TRIGGER_CLUSTER_GENERAL_PAGE = "i18ncs.cluster.GeneralTitleHelp";
    public static final String TRIGGER_CLUSTER_INSTANCE_NEW_PAGE = "i18ncs.clusterInstanceNew.PageTitleHelp";
    public static final String TRIGGER_CLUSTER_INSTANCES_PAGE = "i18ncs.cluster.InstancesTitleHelp";
    public static final String TRIGGER_CLUSTER_RESOURCES_PAGE = "i18ncs.cluster.ResourcesTitleHelp";
    public static final String TRIGGER_CLUSTER_SYSTEM_PROPERTIES = "i18ncs.cluster.ClusterSystemProperties";
    
    protected static final int DELETE_WAIT_TIME = 10000;
    protected static final int START_WAIT_TIME = 30000;
    protected static final int STOP_WAIT_TIME = 20000;
    protected static final int CREATE_WAIT_TIME = 10000;

    //Case 1:
    @Test
    public void testClusterCreationAndDeletion() throws Exception {
        String clusterName = "clusterName" + generateRandomString();
        String instanceName = "instanceName"     + generateRandomString();
        createCluster(clusterName, instanceName);
        try {
            assertEquals(clusterName, getText("propertyForm:clustersTable:rowGroup1:0:col1:link"));
            assertEquals(instanceName, getText("propertyForm:clustersTable:rowGroup1:0:col3:iLink"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
        
        //start to detete the cluster and verify whether the cluster can be delete successfully
        String msg = deleteCluster(clusterName);
        assertTrue(msg.matches("^Delete the selected clusters and their instances[\\s\\S]$"));
    }

    //Case 2
    @Test
    public void testStartAndStopClusterWithOneInstance() {
        String clusterName = "clusterName" + generateRandomString();
        String instanceName = "instanceName" + generateRandomString();
        createCluster(clusterName, instanceName);

        // Verify cluster information in table
        String prefix = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1");
        try {
            assertEquals(clusterName, getText(prefix + "col1:link"));
            assertEquals(clusterName + "-config", getText(prefix + "col2:configlink"));
            assertEquals(instanceName, getText(prefix + "col3:iLink"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        };

        // Start the cluster and verify
        startSpecifiedCluster(ID_CLUSTERS_START_BTN, ID_CLUSTERS_TABLE, clusterName);
        try {
            assertTrue((getText(prefix + "col3").endsWith("Running")));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        };

        // Stop the cluster and verify
        stopSpecifiedCluster(ID_CLUSTERS_START_BTN, ID_CLUSTERS_TABLE, clusterName);
        try {
            assertTrue((getText(prefix + "col3").endsWith("Stopped")));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        };
        
        deleteCluster(clusterName);
    }

    //Case 3
    @Test
    public void testMigrateEjbTimers() {
        String clusterName = "clusterName" + generateRandomString();
        String instanceName1 = "instanceName" + generateRandomString();
        String instanceName2 = "instanceName" + generateRandomString();
        createCluster(clusterName, instanceName1, instanceName2);
        
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        clickByIdAction(clickId);
        //Start cluster instance
        startClusterInstance(instanceName1);
        //start to test
        startTestMigrateEjbTimers();
        //stop cluster instance
        stopClusterInstance(instanceName1);
        
        deleteCluster(clusterName);
    }

    //Case 4
    @Test
    public void verifyClusterGeneralInformationPage() {
        String clusterName = "clusterName" + generateRandomString();
        String instanceName1 = "instanceName" + generateRandomString();
        String instanceName2 = "instanceName" + generateRandomString();
        createCluster(clusterName, instanceName1, instanceName2);
        
        startVerifyClusterGeneralInformationPage(clusterName);
        
        deleteCluster(clusterName);
    }

    //Case 5:
    @Test
    public void testClusterInstancesTab() {
        String clusterName = "clusterName" + generateRandomString();
        String instanceName1 = "instanceName" + generateRandomString();
        String instanceName2 = "instanceName" + generateRandomString();
        createCluster(clusterName, instanceName1);
        
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        clickByIdAction(clickId);
        clickByIdAction("propertyForm:clusterTabs:clusterInst");
        //Check whether the instance is already created
        assertEquals(instanceName1, getText(getTableRowByValue(ID_INSTANCES_TABLE, instanceName1, "col1") + "col1:link"));
        
        clickByIdAction("propertyForm:instancesTable:topActionsGroup1:newButton");
        setFieldValue("propertyForm:propertySheet:propertSectionTextField:NameTextProp:NameText", instanceName2);
        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
        
        //Check whether the instance is already created
        assertEquals(instanceName2, getText(getTableRowByValue(ID_INSTANCES_TABLE, instanceName2, "col1") + "col1:link"));
        
        deleteCluster(clusterName);
    }
    
    //Case 6
    @Test
    public void testProperties() {
        String clusterName = "clusterName" + generateRandomString();
        String instanceName1 = "instanceName" + generateRandomString();
        createCluster(clusterName, instanceName1);
        
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        assertEquals(clusterName, getText(clickId));
        
        // Go to properties tab
        clickByIdAction(clickId);
        clickByIdAction("propertyForm:clusterTabs:clusterProps");
        clickByIdAction("propertyForm:clusterSysPropsPage:topButtons:saveButton");
        assertTrue(driver.findElement(By.className("middle_sun4")).getText().equals("New values successfully saved."));
        
        //Go to cluster properties
        clickByIdAction("propertyForm:clusterTabs:clusterProps:clusterInstanceProps");
        int clusterPropCount = addTableRow("propertyForm:basicTable", "propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        sleep(500);
        setFieldValue("propertyForm:basicTable:rowGroup1:0:col2:col1St", "property" + generateRandomString());
        sleep(500);
        setFieldValue("propertyForm:basicTable:rowGroup1:0:col3:col1St", "value");
        clickByIdAction("propertyForm:propertyContentPage:topButtons:saveButton");
        
        //verify the property had been saved
        assertTableRowCount("propertyForm:basicTable", clusterPropCount);
        
        deleteCluster(clusterName);
    }
    
    //Case 7
    @Test
    public void testClusterWithJmsOptions() {
        String clusterName = "cluster" + generateRandomString();
        gotoClusterPage();
        clickByIdAction("propertyForm:clustersTable:topActionsGroup1:newButton");
        sendKeysByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText", clusterName);
        clickByIdAction("propertyForm:propertySheet:propertySectionTextField:jmsConfigTypeProp:optCustom:optCustom_label");
        sleep(1000);
        clickByIdAction("propertyForm:jmsTypePropertySheet:jmsTypeSection:jmsTypeProp:optLocal");
        clickByIdAction("propertyForm:jmsPropertySheet:configureJmsClusterSection:ClusterTypeProp:optConventional");
        
        Select select = new Select(driver.findElement(By.id("propertyForm:jmsPropertySheet:configureJmsClusterSection:ConfigStoreTypeProp:configStoreType")));
        select.selectByVisibleText("Master Broker");
        
        Select select1 = new Select(driver.findElement(By.id("propertyForm:jmsPropertySheet:configureJmsClusterSection:MessageStoreTypeProp:messageStoreType")));
        select1.selectByVisibleText("File");
        
        setFieldValue("propertyForm:jmsPropertySheet:configureJmsClusterSection:PropertiesProp:properties", "prop1=value1:prop2=value2\\:with\\:colons:prop3=value3");

        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", clusterName + "in1");
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        sleep(500);
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sleep(500);
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", clusterName + "in2");
        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
        
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        clickByIdAction(clickId);
        assertEquals(clusterName, getText("propertyForm:propertySheet:propertSectionTextField:clusterNameProp:clusterName"));
        
        deleteCluster(clusterName);
    }
    
    //Case 8
    @Test
    public void testClusterWithEnhancedJmsOptions() {
        String clusterName = "cluster" + generateRandomString();
        gotoClusterPage();
        clickByIdAction("propertyForm:clustersTable:topActionsGroup1:newButton");
        sendKeysByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText", clusterName);
        clickByIdAction("propertyForm:propertySheet:propertySectionTextField:jmsConfigTypeProp:optCustom:optCustom_label");
        sleep(1000);
        clickByIdAction("propertyForm:jmsTypePropertySheet:jmsTypeSection:jmsTypeProp:optLocal");
        clickByIdAction("propertyForm:jmsPropertySheet:configureJmsClusterSection:ClusterTypeProp:optEnhanced");
        
        setFieldValue("propertyForm:jmsPropertySheet:configureJmsClusterSection:DbVendorProp:dbVendor", "mysql");
        setFieldValue("propertyForm:jmsPropertySheet:configureJmsClusterSection:DbUserProp:dbUser", "root");
        setFieldValue("propertyForm:jmsPropertySheet:configureJmsClusterSection:DbUrlProp:dbUrl", "jdbc:mysql://hostname:portno/dbname?password=xxx");
        
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", clusterName + "in1");
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        sleep(500);
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sleep(500);
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", clusterName + "in2");
        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
        
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        clickByIdAction(clickId);
        assertEquals(clusterName, getText("propertyForm:propertySheet:propertSectionTextField:clusterNameProp:clusterName"));
        
        deleteCluster(clusterName);
    }
    
    //Case 9
    @Test
    public void testClusterWithBadJmsOptions() {
        String clusterName = "cluster" + generateRandomString();
        gotoClusterPage();
        clickByIdAction("propertyForm:clustersTable:topActionsGroup1:newButton");
        sendKeysByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText", clusterName);
        clickByIdAction("propertyForm:propertySheet:propertySectionTextField:jmsConfigTypeProp:optCustom:optCustom_label");
        sleep(1000);
        clickByIdAction("propertyForm:jmsTypePropertySheet:jmsTypeSection:jmsTypeProp:optLocal");
        clickByIdAction("propertyForm:jmsPropertySheet:configureJmsClusterSection:ClusterTypeProp:optConventional");
        
        Select select = new Select(driver.findElement(By.id("propertyForm:jmsPropertySheet:configureJmsClusterSection:ConfigStoreTypeProp:configStoreType")));
        select.selectByVisibleText("Master Broker");
        
        Select select1 = new Select(driver.findElement(By.id("propertyForm:jmsPropertySheet:configureJmsClusterSection:MessageStoreTypeProp:messageStoreType")));
        select1.selectByVisibleText("JDBC");
        
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", clusterName + "in1");
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        sleep(500);
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sleep(500);
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", clusterName + "in2");
        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
        
        assertTrue((driver.findElement(By.className("header_sun4")).getText().indexOf(" An error occurred") != -1));
    }
    
//    @Test
//    public void testClusterResourcesPage() {
//        
//    }
    //Case 10
    @Test
    public void testMultiDeleteClusters() {
        String clusterName = "clusterName" + generateRandomString();
        String clusterName1 = "clusterName" + generateRandomString();
        createCluster(clusterName);
        createCluster(clusterName1);
        
        //start to delete all cluster
        deleteAllCluster();

        try {
            assertEquals("No items found.", driver.findElement(By.id("propertyForm:clustersTable:rowGroup1:_emptyDataColumn:_emptyDataText")).getText());
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }
    
    /**
     *  Cluster related methods
     */
    private void startSpecifiedCluster(String string, String idClustersTable,
            String clusterName) {
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col0:select";
        clickByIdAction(clickId);
        clickByIdAction(ID_CLUSTERS_START_BTN);
        assertTrue(closeAlertAndGetItsText().matches("^Start the selected clusters[\\s\\S]$"));
        waitForAlertProcess("modalBody");
    }

    private void stopSpecifiedCluster(String idClustersStartBtn,
            String idClustersTable, String clusterName) {
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col0:select";
        clickByIdAction(clickId);
        clickByIdAction(ID_CLUSTERS_STOP_BTN);
        assertTrue(closeAlertAndGetItsText().matches("^Stop the selected clusters[\\s\\S]$"));
        waitForAlertProcess("modalBody");
    }
    
    public void createCluster(String clusterName){
        gotoClusterPage();
        clickByIdAction("propertyForm:clustersTable:topActionsGroup1:newButton");
        clearByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText");
        sendKeysByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText", clusterName);
        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
    }
    
    public void createCluster(String clusterName, String instanceName){
        gotoClusterPage();
        clickByIdAction("propertyForm:clustersTable:topActionsGroup1:newButton");
        clearByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText");
        sendKeysByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText", clusterName);
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", instanceName);
        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
    }
    
    public void createCluster(String clusterName, String instanceName, String instanceName1){
        gotoClusterPage();
        clickByIdAction("propertyForm:clustersTable:topActionsGroup1:newButton");
        clearByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText");
        sendKeysByIdAction("propertyForm:propertySheet:propertySectionTextField:NameTextProp:NameText", clusterName);
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", instanceName);
        clickByIdAction("propertyForm:basicTable:topActionsGroup1:addSharedTableButton");
        sleep(500);
        clearByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name");
        sleep(500);
        sendKeysByIdAction("propertyForm:basicTable:rowGroup1:0:col2:name", instanceName1);
        clickByIdAction("propertyForm:propertyContentPage:topButtons:newButton");
    }
    
    private String deleteCluster(String clusterName) {
        gotoClusterPage();
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col0:select";
        clickByIdAction(clickId);
        clickByIdAction(ID_CLUSTERS_DELETE_BTN);
        String alertMsg = closeAlertAndGetItsText();
        waitForAlertProcess("modalBody");
        return alertMsg;
    }
    
    public void deleteAllCluster(){
        gotoClusterPage();
        clickByIdAction("propertyForm:clustersTable:_tableActionsTop:_selectMultipleButton:_selectMultipleButton_image");
        clickByIdAction(ID_CLUSTERS_DELETE_BTN);
        closeAlertAndGetItsText();
        waitForAlertProcess("modalBody");
    }
    
    public void gotoClusterPage(){
        driver.get(baseUrl + "/common/index.jsf");
        clickByIdAction("treeForm:tree:clusterTreeNode:clusterTreeNode_link");
    }
    
    /**
     * Instance related methods
     */
    private void startClusterInstance(String instanceName) {
        clickByIdAction("propertyForm:clusterTabs:clusterInst");
        String clickId = getTableRowByValue(ID_INSTANCES_TABLE, instanceName, "col1")+"col0:select";
        clickByIdAction(clickId);
        clickByIdAction(ID_INSTANCES_START_BTN);
        closeAlertAndGetItsText();
        waitForAlertProcess("modalBody");
    }
    
    private void stopClusterInstance(String instanceName1) {
        clickByIdAction("propertyForm:clusterTabs:clusterInst");
        String clickId = getTableRowByValue(ID_INSTANCES_TABLE, instanceName1, "col1")+"col0:select";
        clickByIdAction(clickId);
        clickByIdAction(ID_INSTANCES_STOP_BTN);
        closeAlertAndGetItsText();
        waitForAlertProcess("modalBody");
    }
    /**
     * Test related methods
     */
    
    private void startTestMigrateEjbTimers() {
        clickByIdAction("propertyForm:clusterTabs:general");
        sleep(1000);
        clickByIdAction("propertyForm:migrateTimesButton");
        sleep(1000);
        clickByIdAction("propertyForm:propertyContentPage:topButtons:saveButton");
        assertTrue(driver.findElement(By.className("header_sun4")).getText().indexOf("Migrated 0 timers") != -1);
    }
    
    private void startVerifyClusterGeneralInformationPage(String clusterName) {
        String clickId = getTableRowByValue(ID_CLUSTERS_TABLE, clusterName, "col1")+"col1:link";
        clickByIdAction(clickId);
        
        assertEquals(clusterName, getText("propertyForm:propertySheet:propertSectionTextField:clusterNameProp:clusterName"));
        assertEquals(clusterName + "-config", getText("propertyForm:propertySheet:propertSectionTextField:configNameProp:configlink"));
        
        
        clickByIdAction("propertyForm:propertySheet:propertSectionTextField:configNameProp:configlink");
        clickByIdAction("treeForm:tree:clusterTreeNode:clusterTreeNode_link");
        clickByIdAction(clickId);
        
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
