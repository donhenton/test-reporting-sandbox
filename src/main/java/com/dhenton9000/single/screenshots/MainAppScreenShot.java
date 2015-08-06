/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.single.screenshots;

import com.dhenton9000.screenshots.single.AbstractSingleScreenShot;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;

/**
 *
 * @author dhenton
 */
public class MainAppScreenShot extends AbstractSingleScreenShot {
    
    public static final String SIMPLE_IMAGE_NAME = "springmvc-mainpage";
    
    public MainAppScreenShot(GenericAutomationRepository g) {
        super(g);
    }
    
    @Override
    public void setUpScreenshot() {
        this.getGenericAutomationRepository().maximizeWindow();
        this.getGenericAutomationRepository().getDriver().get("http://http://donhenton-springmvc3.herokuapp.com/app/home.html");
        this.getGenericAutomationRepository().getWaitMethods().waitForDuration(3);
    }
    
    @Override
    public void cleanUp() {
         this.getGenericAutomationRepository().getDriver().close();
          this.getGenericAutomationRepository().getDriver().quit();
    }

    @Override
    public String getSimpleImageName() {
       return SIMPLE_IMAGE_NAME;
    }
    
 

    
}
