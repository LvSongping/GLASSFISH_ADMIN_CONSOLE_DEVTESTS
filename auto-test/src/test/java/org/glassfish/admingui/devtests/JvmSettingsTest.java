/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010-2011 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import static org.junit.Assert.*;

public class JvmSettingsTest extends BaseSeleniumTestClass {
    
    public static final String ID_JVM_OPTIONS_TABLE = "propertyForm:basicTable";
    
    @Test
    public void testJvmGeneralSettings() {
        gotoDasPage();
        clickAndWait("treeForm:tree:configurations:server-config:jvmSettings:jvmSettings_link");
        if (!driver.findElement(By.id("propertyForm:propertySheet:propertSectionTextField:debugEnabledProp:debug")).isSelected())
            clickByIdAction("propertyForm:propertySheet:propertSectionTextField:debugEnabledProp:debug");
        clickAndWait("propertyForm:propertyContentPage:topButtons:saveButton");
        assertTrue(driver.findElement(By.id("propertyForm:propertySheet:propertSectionTextField:debugEnabledProp:debug")).isSelected());
    }

    @Test
    public void testJvmSettings() {
        gotoDasPage();
        String jvmOptionName = "-Dfoo"+generateRandomString();
        clickAndWait("treeForm:tree:configurations:server-config:jvmSettings:jvmSettings_link");
        clickAndWait("propertyForm:javaConfigTab:jvmOptions");

        sleep(1000);
        int count = addTableRow(ID_JVM_OPTIONS_TABLE, "propertyForm:basicTable:topActionsGroup1:addSharedTableButton", "Options");
        sleep(500);
        setFieldValue("propertyForm:basicTable:rowGroup1:0:col3:col1St", jvmOptionName);
        clickAndWait("propertyForm:propertyContentPage:topButtons:saveButton");
        isClassPresent("label_sun4");
        
        gotoDasPage();
        clickAndWait("treeForm:tree:configurations:server-config:jvmSettings:jvmSettings_link");
        clickAndWait("propertyForm:javaConfigTab:jvmOptions");
        sleep(1000);
        assertTableRowCount(ID_JVM_OPTIONS_TABLE, count);
    }

    @Test
    public void testJvmProfilerForDas() {
        gotoDasPage();
        clickAndWait("treeForm:tree:configurations:server-config:jvmSettings:jvmSettings_link");
        clickAndWait("propertyForm:javaConfigTab:profiler");
        try {
            if (driver.findElement(By.id(("propertyForm:propertyContentPage:topButtons:deleteButton"))).isEnabled()) {
                clickAndWait("propertyForm:propertyContentPage:topButtons:deleteButton");
                if (driver.findElement(By.className("label_sun4")).isDisplayed()) {
                    assertEquals("Profiler successfully deleted.", driver.findElement(By.className("label_sun4")).getText());;
                }
            }
        } catch(NoSuchElementException e){
            setFieldValue("propertyForm:propertySheet:propertSectionTextField:profilerNameProp:ProfilerName", "profiler" + generateRandomString());
            int count = addTableRow("propertyForm:basicTable", "propertyForm:basicTable:topActionsGroup1:addSharedTableButton", "Options");
            sleep(500);
            setFieldValue("propertyForm:basicTable:rowGroup1:0:col3:col1St", "-Dfoo=" + generateRandomString());
            clickAndWait("propertyForm:propertyContentPage:topButtons:newButton");
            assertTableRowCount("propertyForm:basicTable", count);

            gotoDasPage();
            clickAndWait("treeForm:tree:configurations:server-config:jvmSettings:jvmSettings_link");
            clickAndWait("propertyForm:javaConfigTab:profiler");
            clickByIdAction("propertyForm:propertyContentPage:topButtons:deleteButton");
            assertTrue(closeAlertAndGetItsText().matches("^Profiler will be deleted\\.  Continue[\\s\\S]$"));
        }
    }

    @Test
    public void testJvmProfilerForRunningInstance() {
        testProfilerForInstance (true);
    }
    
    @Test
    public void testJvmProfilerForStoppedInstance() {
        testProfilerForInstance (false);
    }


    private void testProfilerForInstance(boolean start){
        String instanceName = generateRandomString();
        String configName = instanceName+"-config";
        StandaloneTest st = new StandaloneTest();
        st.createStandAloneInstance(instanceName);
        if (start){
            st.startInstance(instanceName);
        }

        clickAndWait("treeForm:tree:configurations:"+ configName +":jvmSettings:jvmSettings_link");
        clickAndWait("propertyForm:javaConfigTab:profiler" );

        setFieldValue("propertyForm:propertySheet:propertSectionTextField:profilerNameProp:ProfilerName", "profiler" + generateRandomString());
        int count = addTableRow("propertyForm:basicTable", "propertyForm:basicTable:topActionsGroup1:addSharedTableButton", "Options");
        setFieldValue("propertyForm:basicTable:rowGroup1:0:col3:col1St", "-Dfoo=" + generateRandomString());
        clickAndWait("propertyForm:propertyContentPage:topButtons:newButton");
        assertTableRowCount("propertyForm:basicTable", count);

        gotoDasPage();
        clickAndWait("treeForm:tree:configurations:"+ configName +":jvmSettings:jvmSettings_link");
        clickAndWait("propertyForm:javaConfigTab:profiler");
        clickAndWait("propertyForm:propertyContentPage:topButtons:deleteButton");
        assertTrue(closeAlertAndGetItsText().matches("^Profiler will be deleted\\.  Continue[\\s\\S]$"));
        assertTrue(driver.findElement(By.id("propertyForm:propertyContentPage:topButtons:newButton")).isDisplayed());
        
        if (start){
            st.gotoStandaloneInstancesPage();
            st.stopInstance(instanceName);
        }
        st.deleteStandAloneInstance(instanceName);
    }

}
