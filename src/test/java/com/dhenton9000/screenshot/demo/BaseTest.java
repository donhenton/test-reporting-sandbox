/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshot.demo;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.dhenton9000.selenium.drivers.DriverFactory;
import com.dhenton9000.selenium.drivers.DriverFactory.REMOTE_SERVER_VALUE;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
import com.dhenton9000.selenium.wicket.WicketBy;
import java.io.File;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;

/**
 * base class for tests that which to have the driver set via the remote.server
 * property of the pom.xml file
 *
 * @author dhenton
 */
public class BaseTest implements ITest {

    private final static Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private DriverFactory driverFactory = new DriverFactory();
    private GenericAutomationRepository automationRepository;

    public GenericAutomationRepository getAutomationRepository() {
        if (automationRepository == null) {
            WebDriver d = null;
            try {
                d = getDriver();
            } catch (IOException ex) {
                throw new RuntimeException("io problem in repository creation "
                        + ex.getMessage());

            }
            automationRepository = new GenericAutomationRepository(d, DriverFactory.getConfiguration());

        }
        return automationRepository;
    }

    protected WebDriver getDriver() throws IOException {
        return driverFactory.getDriver();
    }

    protected WebDriver getDriver(REMOTE_SERVER_VALUE driver_env) throws IOException {
        return driverFactory.getDriver(driver_env);
    }

    public void mouseOverElement(WebElement element, WebDriver driver) {

        Locatable hoverItem = (Locatable) element;
        Mouse mouse = ((HasInputDevices) driver).getMouse();
        mouse.mouseMove(hoverItem.getCoordinates());

    }

    public static String createPathToTestResources(String htmlFilename) {
        char sc = File.separatorChar;
        String currentDir = System.getProperty("user.dir");
        String resourcesPath = currentDir + sc + "src" + sc + "test" + sc + "resources";
        String htmlPath = resourcesPath + sc + htmlFilename;
        return htmlPath;
    }

    protected boolean isAlertPresent(WebDriver driver) {
        boolean isAlertPresent = true;
        String oldWindow = driver.getWindowHandle();
        try {
            driver.switchTo().alert();
        } catch (NoAlertPresentException err) {
            return false;
        }
        driver.switchTo().window(oldWindow);
        return isAlertPresent;
    }

    protected boolean isNotOnPageViaLinkText(String linkText, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(By.linkText(linkText));
        } catch (NoSuchElementException err) {
            isNotOnPage = true;
        }
        return isNotOnPage;
    }

    protected boolean isNotOnPageViaId(String Id, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(By.id(Id));
        } catch (NoSuchElementException err) {
            isNotOnPage = true;
        }
        return isNotOnPage;
    }

    protected boolean isNotOnPageViaWicketPath(String wicketPath, WebDriver driver) {
        boolean isNotOnPage = false;
        try {
            driver.findElement(WicketBy.wicketPath(wicketPath));
        } catch (NoSuchElementException err) {
            isNotOnPage = true;
        }
        return isNotOnPage;
    }

    /**
     * This only tests if the item exists, and returns the state of the driver.
     *
     * @param frameTitle
     * @param driver
     * @return true if frame is present
     */
    protected boolean isFramePresent(String frameTitle, WebDriver driver) {
        boolean framePresent = true;
        WebDriver oldDriver = driver;
        try {
            driver.switchTo().frame("ModelFrameTitle");
            driver = oldDriver;
        } catch (NoSuchFrameException err) {
            framePresent = false;
        } catch (Exception err) {
            logger.error("err " + err.getClass().getName());
        }
        return framePresent;

    }

    @Override
    public String getTestName() {
         return this.getClass().getSimpleName();
    }
}
