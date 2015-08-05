/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.reporting;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.dhenton9000.client.RestClientBase.getFileFromClassPath;
import java.io.FileReader;
import java.io.IOException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that sorts JSON objects for gold file testing to reduce problems with
 * array comparisons.
 *
 * A file called sortUtils.js will contain the javascript sorting functions.
 * These functions must take a string and return a string using JSON3 to parse
 * and stringify results.
 *
 *
 *
 * @author dhenton
 */
public class JSONSorter {

    Logger LOG = LoggerFactory.getLogger(JSONSorter.class);

    /**
     * take a JSON object as a string, and sort and/or manipulate it using
     * javascript.
     * 
     * 
     * @param sampleString the JSON as as a string
     * @param sortFunctionName the function to evoke with sampleString as
     * the single parameter
     * @return the string representation of the sorted items
     * @throws ScriptException
     * @throws IOException
     * @throws NoSuchMethodException 
     */
    public String sortJSONObject(String sampleString, String sortFunctionName) throws
            ScriptException,
            IOException, NoSuchMethodException {

        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        engine.eval(new FileReader(getFileFromClassPath("/jsfiles/json3.js", this.getClass())));
        engine.eval(new FileReader(getFileFromClassPath("/jsfiles/sortUtils.js", this.getClass())));

        Invocable inv = (Invocable) engine;
        String sortedString = (String) inv.invokeFunction(sortFunctionName, sampleString);
       // ObjectMapper mapper = new ObjectMapper();
        return sortedString;

    }

}
