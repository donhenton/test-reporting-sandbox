/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.reporting;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.ni.test.generic.automation.GenericAutomationRepository.ENV;
//import com.ni.utils.VelocityEngineFactory;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.testng.Assert.fail;
import org.testng.Reporter;
import static com.dhenton9000.reporting.JSONComparisonViewerLink.JSON_COMPARISON_FOLDER;
import com.dhenton9000.selenium.drivers.DriverFactory;
import com.dhenton9000.selenium.drivers.DriverFactory.ENV;

/**
 * This class will write out to a JSON file, that is used to analyze failed
 * tests. The specific tests are those which use gold files to compare results.
 * The JSON records the expected and actual JSON and writes to both a JSON and
 * javascript file. The javascript file can then be loaded into an html page for
 * viewing via a javascript program.
 *
 * See @{link com.ni.rest.comparison.tools.JSONComparisonToolTests} for example
 * use.
 *
 * @author dhenton
 */
public class JSONComparisonTool {

    private Logger LOG = LoggerFactory.getLogger(JSONComparisonTool.class);
    /**
     * must have ending slash, the location for the json diff viewer app its set
     * in the profile section of the pom file
     */
    private final static String ROOT_FOLDER_LOCATION = "target/classes/service_public_html/";
    private final static String JSON_FOLDER_LOCATION = ROOT_FOLDER_LOCATION + "json/";
    public static final String JSON_TEST_DATA_FIELD = "comparisons";

    /**
     * pretty print json
     *
     * @param jsonObject
     * @return
     * @throws IOException
     */
    public static final String prettyPrint(String jsonObject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object json = mapper.readValue(jsonObject, Object.class);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

    }

    /**
     * Get the name of the current running method.
     *
     * @param e, you can get this with Thread.currentThread().getStackTrace(),
     * however, it must originate from the method being called so it can be
     * determined here.
     * @return method name that called this function
     *
     */
    public static String getMethodName(StackTraceElement e[]) {

        boolean doNext = false;
        for (StackTraceElement s : e) {

            if (doNext) {
                return s.getMethodName();

            }
            doNext = s.getMethodName().equals("getStackTrace");
        }
        return null;
    }

    public int
            compareJson(String testName, String methodName,
                    String expectedString, String actualString, int threshold)
            throws IOException, Exception {

        return compareJson(testName, methodName, expectedString, actualString, threshold, true);
    }

    /**
     * pretty print two json strings and compare them.Write out to a json file
     * for comparison in a diff tool should the comparison exceed or equal the
     * threshold value for the Levenshtein Distance
     *
     * @param testName the test name generally the test class toString
     * @param methodName the specific method of the test
     * @param expectedString expected results
     * @param actualString actual results
     * @param threshold if the Levenshtein Distance distance is greater or
     * equal, then fail the test
     * @param failOnExceed if true then fail here for testing
     * @throws java.io.IOException problems with pretty print
     * @return the int
     */
    public int compareJson(String testName, String methodName,
            String expectedString, String actualString,
            int threshold, boolean failOnExceed)
            throws IOException, Exception {

        int distanceValue = -9;
        expectedString = prettyPrint(expectedString);
        actualString = prettyPrint(actualString);
        distanceValue = StringUtils.getLevenshteinDistance(expectedString, actualString);
        LOG.debug(String.format("beginning write of "
                + "comparison json dist %d "
                + "threshold %d", distanceValue, threshold));
        if (distanceValue >= threshold) {
            //write out to the json  and js file
            LOG.debug("got greater than threshold");
            String templateName = "velocity_templates/comparisons/singleTestForServices.vm";


            VelocityContext context = new VelocityContext();
            context.put("expected", expectedString);
            context.put("actual", actualString);
            context.put("testName", testName);
            context.put("methodName", methodName);
            try {
                VelocityEngine ve = (new VelocityEngineFactory()).getEngine();
                StringWriter writer = new StringWriter();
                ve.mergeTemplate(templateName, "UTF-8", context, writer);
                writer.flush();
                writer.close();
                String mergeResults = writer.toString();
                LOG.debug("did the single velocity merge");
                writeToAccumulationFile(mergeResults);
                String info = "json comparison failed for " + testName + "-->" + methodName;
                LOG.warn(info);
                String paramTemplate = "<a href='"
                        + JSON_COMPARISON_FOLDER
                        + "singleCompare.html?testName=%s&methodName=%s'>"
                        + "JSON Comparison</a>";
                String linkReport = String.format(paramTemplate, testName, methodName);
                Reporter.log(linkReport);
                if (failOnExceed) {
                    fail(info);
                }
            } catch (Exception err) {
                String error = "Unable to perform velocity compare write\n";
                LOG.error(error, err);
                fail(error);
            }
        }
        return distanceValue;
    }

    private void writeJSFile(String data) throws IOException {
        final String jsFileStr = JSON_FOLDER_LOCATION + "failed_comparisons.js";

        final File jsFile = new File(jsFileStr);
        if (data == null) {
            data = "null;";
        }
        FileUtils.writeStringToFile(jsFile, "var failedData = \n" + data);
    }

    private void writeToAccumulationFile(String mergeResults) throws IOException {

        final String jsonFileStr = JSON_FOLDER_LOCATION + "failed_comparisons.json";

        final File jsonFile2 = new File(jsonFileStr);
        String jsonData;
        ENV env = DriverFactory.getENV();
        String envStr = "not found";
        if (env != null) {
            envStr = env.toString();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateStr = sdf.format(new Date());
        ObjectMapper m = new ObjectMapper();
        ObjectNode json = (ObjectNode) m.readTree(jsonFile2);
        json.put("env", "" + envStr);
        json.put("date", dateStr);
        ArrayNode arrayData = (ArrayNode) json.get(JSON_TEST_DATA_FIELD);

        JsonNode newThing = m.readValue(mergeResults, JsonNode.class);

        arrayData.add(newThing);

        jsonData
                = m.writerWithDefaultPrettyPrinter()
                .writeValueAsString(json);
        LOG.info("writing comparison " + jsonFile2.getAbsolutePath());
        FileUtils.writeStringToFile(jsonFile2, jsonData);

        writeJSFile(jsonData);
    }

}
