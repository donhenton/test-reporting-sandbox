/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.restaurants;

import com.dhenton9000.client.CallReturn;
import com.dhenton9000.restaurant.client.RestaurantClient;
import com.dhenton9000.tests.ServiceTestBase;
import java.io.IOException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class RestaurantTestBase extends ServiceTestBase {
     
    
    private Logger LOG = LoggerFactory.getLogger(RestaurantTestBase.class);
    private static final String RESTAURANT_URL_PROPERTY = "restaurant.url";
    private RestaurantClient client;
    public enum CALL_TYPE {get,post};
    
     protected void configureClient() {
        
       
        String restURL = getConfig().getString(RESTAURANT_URL_PROPERTY);       
        LOG.info("configure Client successful " + restURL);
        client = new RestaurantClient(restURL);
    }

    /**
     * @return the client
     */
    public RestaurantClient getRestAudienceClient() {
        if (client == null) {
            configureClient();
        }

        return client;
    }
    
    
      /**
     * make a call.
     *
     * @param jsonString json as string to send
     * @param path path to call
     * @param headers null for no headers sent
     * @return
     * @throws IOException
     */
    public CallReturn doPost(String jsonString, String path,
            MultivaluedMap<String, Object> headers) throws IOException {

        return prepareCall(jsonString,path,headers,CALL_TYPE.post);
    }
    
     public CallReturn doGet(String path,
            MultivaluedMap<String, Object> headers) throws IOException {

        return prepareCall(null,path,headers,CALL_TYPE.get);
    }

    private CallReturn prepareCall(String jsonString, String path,
            MultivaluedMap<String, Object> headers, CALL_TYPE callType) throws IOException {
        Entity<String> itemToSend = Entity.json(jsonString);
        // LOG.debug("sending\n" + jsonString + "\n");
        Invocation.Builder responseBuilder = getRestAudienceClient().getWebTarget()
                .path(path)
                .request(MediaType.APPLICATION_JSON);
        if (headers != null) {
            responseBuilder.headers(headers);
        }
        
        Response response = null;
        
        switch (callType) {
            case get:
                response = responseBuilder.get();
                 
                
                break;
            case post: {
                response = responseBuilder.post(itemToSend);
                
            }

        }

        return createCallReturnFromResponse(response);

    }

    
   
    
    
}
