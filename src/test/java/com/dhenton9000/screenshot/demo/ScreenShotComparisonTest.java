/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.screenshot.demo;


import com.dhenton9000.single.screenshots.MainAppScreenShot;
import java.io.IOException;
import org.testng.annotations.Test;

/**
 *
 * @author dhenton
 */
public class ScreenShotComparisonTest extends ScreenshotTestBase {

    @Test
    public void testSampleScreenshot() throws IOException {
        
        System.setProperty("test.env","dev");
        
        
        MainAppScreenShot sample
                = new MainAppScreenShot(this.getAutomationRepository());

        peformSingleScreenshot(sample);

    }

}
