package com.dhenton9000.reporting;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/**
 *
 * possible properties for a file based loader resource.loader = file
 * file.resource.loader.class =
 * org.apache.velocity.runtime.resource.loader.FileResourceLoader
 * file.resource.loader.path = c:/tomcat/webapps/velocity/WEB-INF/templates
 * file.resource.loader.cache = true
 * file.resource.loader.modificationCheckInterval = 2
 *
 * @author dhenton
 */
public class VelocityEngineFactory {

    private static final String LOGGER_NAME = "com.ni.velocity";
    private static final Logger LOG = LoggerFactory.getLogger(LOGGER_NAME);

    public VelocityEngine getEngine() throws Exception {

        VelocityEngine ve = new VelocityEngine();

        ve.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, this);
        ve.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        ve.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
                "org.apache.velocity.runtime.log.Log4JLogChute");

        ve.setProperty("runtime.log.logsystem.log4j.logger", LOGGER_NAME);
        
        
        ve.init();
//        ve.setProperty("file.resource.loader.path","templates") ;
//        ve.setProperty("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.FileResourceLoader") ;
//        ve.setProperty("file.resource.loader.cache","true") ;
//        ve.setProperty("file.resource.loader.modificationCheckInterval","2") ;

        return ve;
    }
}



/*
 
 * 
 * use to read from a jar file
 * 
 * 
 * 
      Engine ve = new VelocityEngine();
            ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

            ve.init();

            final String templatePath = "templates/" + templateName + ".vm";
            InputStream input = this.getClass().getClassLoader().getResourceAsStream(templatePath);
            if (input == null) {
                throw new IOException("Template file doesn't exist");
            }

            InputStreamReader reader = new InputStreamReader(input);

            VelocityContext context = new VelocityContext();

            if (properties != null) {
                stringfyNulls(properties);
                for (Map.Entry<String, Object> property : properties.entrySet()) {
                    context.put(property.getKey(), property.getValue());
                }
            }

            Template template = ve.getTemplate(templatePath, "UTF-8");
            String outFileName = File.createTempFile("report", ".html").getAbsolutePath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFileName)));

            if (!ve.evaluate(context, writer, templatePath, reader)) {
                throw new Exception("Failed to convert the template into html.");
            }

            template.merge(context, writer);

            writer.flush();
            writer.close();

 
 */
