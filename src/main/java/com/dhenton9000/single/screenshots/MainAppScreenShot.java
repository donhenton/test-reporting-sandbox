/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.single.screenshots;

import com.dhenton9000.screenshots.single.AbstractSingleScreenShot;
import com.dhenton9000.selenium.generic.GenericAutomationRepository;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class MainAppScreenShot extends AbstractSingleScreenShot {

    public static final String SIMPLE_IMAGE_NAME = "springmvc-mainpage";
    private static final Logger LOG = LoggerFactory.getLogger(MainAppScreenShot.class);

    public MainAppScreenShot(GenericAutomationRepository g) {
        super(g);
    }

    @Override
    public void setUpScreenshot() {
        this.getGenericAutomationRepository().maximizeWindow();
        this.getGenericAutomationRepository().getDriver().get("http://donhenton-springmvc3.herokuapp.com/app/home.html");
        this.getGenericAutomationRepository().getWaitMethods().waitForDuration(3);
    }

    @Override
    public void cleanUp() {
        
        try {
            this.getGenericAutomationRepository().getDriver().quit();
        } catch (Exception err) {
            LOG.error("problems closing in MainAppScreenShot "+err.getMessage());
        }
    }

    @Override
    public String getSimpleImageName() {
        return SIMPLE_IMAGE_NAME;
    }

}
