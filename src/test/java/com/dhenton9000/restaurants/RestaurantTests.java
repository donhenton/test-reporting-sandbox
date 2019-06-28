/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.restaurants;

import com.dhenton9000.client.CallReturn;
import static com.dhenton9000.reporting.JSONComparisonTool.getMethodName;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author dhenton
 */
public class RestaurantTests extends RestaurantTestBase {
    
    
    private Logger LOG = LoggerFactory.getLogger(RestaurantTestBase.class);
    
    @BeforeClass
    public void beforeClass()
    {
        
    }
    
    @BeforeMethod
    public void beforeMethod()
    {
        
    }
    
    @Test
    public void testGetRestaurantById() throws Exception
    {
       CallReturn ret =   this.doGet("/backbone/restaurant/4", null);
       assertEquals(ret.status,200);
       String resultString = ret.jsonStringResult;
       // this will fail deliberately as the gold file is deliberately wrong
       String expectedString
                = readGoldFile("/gold_files/restaurants/getRestaurantById.json");

        getCompareTool().compareJson(this.getTestName(),
                getMethodName(
                        Thread.currentThread().getStackTrace()),
                expectedString,
                resultString, 1,true
        );
    }
    
    
}
