/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dhenton9000.reporting.VelocityEngineFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * base class for all app specific clients for testing
 *
 * @author dhenton
 */
public class RestClientBase {

    private final URI baseURI;
    private final Client restClient;
    /**
     * single point of reference for the properties file in use everywhere
     */
    

    private Logger LOG = LoggerFactory.getLogger(RestClientBase.class);

    //    JDBCEmployeeDAO jdbcEmployeeDAO = (JDBCEmployeeDAO) context.getBean("jdbcEmployeeDAO");
    public RestClientBase(String baseURL) {
        if (baseURL == null) {
            throw new RuntimeException("baseURL was null, is it in your properties file?");
        }
        baseURI = UriBuilder.fromUri(baseURL).build();
        LOG.info("baseURI " + baseURI.toString());
        this.restClient = ClientBuilder.newClient(createConfig());
        this.restClient.property(ClientProperties.CONNECT_TIMEOUT, 15000);
        this.restClient.property(ClientProperties.READ_TIMEOUT, 15000);

    }

    public RestClientBase(String baseURL, int timeout) {
        if (baseURL == null) {
            throw new RuntimeException("baseURL was null, is it in your properties file?");
        }
        baseURI = UriBuilder.fromUri(baseURL).build();
        LOG.info("baseURI " + baseURI.toString());
        this.restClient = ClientBuilder.newClient(createConfig());
        this.restClient.property(ClientProperties.CONNECT_TIMEOUT, timeout);
        this.restClient.property(ClientProperties.READ_TIMEOUT, timeout);

    }

    protected ClientConfig createConfig() {
        return new ClientConfig();
    }

    public WebTarget getWebTarget() {
        return restClient.target(getBaseURI());
    }

    /**
     * @return the baseURL
     */
    public URI getBaseURI() {

        return baseURI;
    }

    /**
     * @return the restClient
     */
    public Client getRestClient() {

        return restClient;
    }

    /**
     * take a classpath descriptor /com/fred/ted/something.txt and translate it
     * to a file reference
     *
     * @param path
     * @param clazz
     * @return the file reference or throw if not found
     */
    public static File getFileFromClassPath(String path, Class clazz) {
        URL u = clazz.getResource(path);
        File t = new File(FileUtils.toFile(u).getAbsolutePath());

        if (t == null || !t.exists()) {
            throw new RuntimeException("nothing at " + path);
        }
        return t;
    }

     

    /**
     * create a map suitable for use in submitting headers in a request.
     * this can be used to submit headers in the request
     *
     * @param alpha
     * @param beta
     * @return
     */
    public MultivaluedHashMap<String, Object> getHeadersMapFor(String alpha, String beta) {
        MultivaluedHashMap<String, Object> headers
                = new MultivaluedHashMap<>();
        headers.put("alpha", Arrays.asList((Object) alpha));
        headers.put("beta", Arrays.asList((Object) beta));
        return headers;
    }

   

    public int getLevenshteinDistance(String n1, String n2) {
        if (n1 == null || n2 == null) {
            return Integer.MAX_VALUE;
        }

        n1 = n1.trim();
        n2 = n2.trim();

        return StringUtils.getLevenshteinDistance(n1, n2);

    }

    /**
     * fill in a template using the given context
     *
     * @param templateName
     * @param context
     * @return
     */
    public String fillTemplate(String templateName, VelocityContext context) {
        String template = null;
        try {
            VelocityEngineFactory vFactory = new VelocityEngineFactory();
            VelocityEngine ve = vFactory.getEngine();
            StringWriter writer = new StringWriter();

            ve.mergeTemplate(templateName, "UTF-8", context, writer);
            writer.flush();
            writer.close();
            template = writer.toString();
        } catch (Exception err) {
            throw new RuntimeException("Velocity problem: " + err.getMessage());
        }

        return template;
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

}
