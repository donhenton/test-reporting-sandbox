/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.tests;

import com.dhenton9000.client.CallReturn;
import com.dhenton9000.client.RestClientBase;
import com.dhenton9000.listeners.ServiceReportingListener;
import com.dhenton9000.reporting.JSONComparisonTool;
import com.dhenton9000.reporting.JSONSorter;
import com.dhenton9000.selenium.drivers.DriverFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import javax.ws.rs.core.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.annotations.Listeners;
//import com.dhenton9000.database.RestaurantDao;

/**
 *
 * @author dhenton
 */
@Listeners({ServiceReportingListener.class})
public class ServiceTestBase implements ITest {

    private final PropertiesConfiguration config;
    

    private static final Logger LOG = LoggerFactory.getLogger(ServiceTestBase.class);
    private JSONComparisonTool compareTool = null;
    private final ClassPathXmlApplicationContext context;
    //private RestaurantDao audienceDao;

    public ServiceTestBase() {
        context = new ClassPathXmlApplicationContext("spring-datasource.xml");
        try {
            config = new PropertiesConfiguration(DriverFactory.ENV_PROPERTIES_FILENAME);
        } catch (ConfigurationException ex) {
            throw new RuntimeException("configuration error: " + ex.getMessage());
        }
    }

    @Override
    public String getTestName() {
        return this.getClass().getSimpleName();
    }

    /**
     * @return the compareTool
     */
    public JSONComparisonTool getCompareTool() {
        if (compareTool == null) {
            compareTool = new JSONComparisonTool();
        }
        return compareTool;
    }

    /**
     * Get the contents of a gold file via the classpath.
     *
     * @param classpathToGoldFile classpath to file, with '/', first character
     * should be a '/'
     *
     * @return
     * @throws IOException if problems reading file
     */
    protected String readGoldFile(String classpathToGoldFile) throws IOException {
        File goldFile
                = RestClientBase
                .getFileFromClassPath(classpathToGoldFile,
                        this.getClass());

        return FileUtils.readFileToString(goldFile, "UTF-8");
    }

    protected JSONSorter getJSONSorter() {
        return new JSONSorter();
    }

    /**
     * @return the config
     */
    public PropertiesConfiguration getConfig() {
        return config;
    }

    public static CallReturn createCallReturnFromResponse(Response response) throws IOException {
        String jsonStringResponse = response.readEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode sampleTree = null;
        try {
            sampleTree = mapper.readTree(jsonStringResponse);
        } catch (com.fasterxml.jackson.core.JsonParseException ex) {
            //LOG.error("parse error:\n" + jsonStringResponse);
        }
        // LOG.debug(jsonStringResponse);
        return new CallReturn(response.getStatus(), sampleTree, jsonStringResponse);
    }
    /**
     * @return the restaurantDao
     *
     * public RestaurantDao getAudienceDao() { if (restaurantDao == null) {
     * restaurantDao = context.getBean(RestaurantDao.class); } return
     * restaurantDao; }
     */
}
