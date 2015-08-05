/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.restaurant.client;

import com.dhenton9000.client.RestClientBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dhenton
 */
public class RestaurantClient extends RestClientBase {
    private Logger LOG = LoggerFactory.getLogger(RestClientBase.class);

    public RestaurantClient(String baseURL) {
        super(baseURL);
    }
}
