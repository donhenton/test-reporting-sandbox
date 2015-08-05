/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

/**
 *
 * @author dhenton
 */
public class AddHeadersRequestFilter implements ClientRequestFilter {

    private final HashMap<String, String> headers;

    public AddHeadersRequestFilter(HashMap<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {

        for (String key : headers.keySet()) {
            List<Object> items = new ArrayList<>();
            items.add(headers.get(key));
            requestContext.getHeaders().put(key, items);
        }

    }

    /**
     * @return the headers
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }

}
