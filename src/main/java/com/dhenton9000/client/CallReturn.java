/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.client;

import com.fasterxml.jackson.databind.JsonNode;

/**
 *
 * @author dhenton
 */
public class CallReturn {

    public int status = 0;
    public JsonNode result = null;
    public String jsonStringResult = null;

    public CallReturn(int status, JsonNode result,String t) {
        this.status = status;
        this.result = result;
        this.jsonStringResult = t;
    }

}
