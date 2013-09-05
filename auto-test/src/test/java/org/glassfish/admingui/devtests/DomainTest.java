package org.glassfish.admingui.devtests;

import org.junit.*;

import static org.junit.Assert.*;
import org.openqa.selenium.*;

public class DomainTest extends BaseSeleniumTestClass {

    @Test
    public void testAttributeTab() throws Exception {
        driver.get(baseUrl + "/common/index.jsf");
        driver.findElement(By.id("treeForm:tree:nodes:nodes_link")).click();
        driver.findElement(By.id("propertyForm:propertySheet:propertSectionTextField:localeProp:Locale")).clear();
        driver.findElement(By.id("propertyForm:propertySheet:propertSectionTextField:localeProp:Locale")).sendKeys("en");
        driver.findElement(By.id("propertyForm:propertyContentPage:topButtons:saveButton")).click();
        sleep(5000);
        // Warning: assertTextPresent may require manual changes
        //assertTrue(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*New values successfully saved[\\s\\S]*$"));
        assertEquals("New values successfully saved.", driver.findElement(By.cssSelector("span.label_sun4")).getText());
        driver.findElement(By.id("treeForm:tree:nodes:nodes_link")).click();
        try {
            assertEquals("en", driver.findElement(By.id("propertyForm:propertySheet:propertSectionTextField:localeProp:Locale")).getAttribute("value"));
        } catch (Error e) {
            verificationErrors.append(e.toString());
        }
    }

}
