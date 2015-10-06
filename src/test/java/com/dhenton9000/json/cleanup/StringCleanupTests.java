/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.json.cleanup;

import org.apache.commons.lang3.StringUtils;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author dhenton
 */
public class StringCleanupTests {

    private static final String itemTemplate = "{\"abc\":%s123}";
    private static final char uspace = (char) (Integer.parseInt("000b", 16)); // \u000b;
    private final String testWithNormalSpace = String.format(itemTemplate, " ");
    private final String testWithUSpaceAtTheEnd = testWithNormalSpace + uspace;
    private final String testWithUSpaceInTheMiddle = String.format(itemTemplate, uspace);

    @BeforeClass
    public void beforeClass() {

    }

    /**
     * the extra character is not considered a space
     */
    @Test
    public void test00bIsNotASpace() {

        assertEquals(testWithUSpaceAtTheEnd.length(), testWithNormalSpace.length() + 1);
        // System.out.println("'"+test+"'");
        char[] testArray = testWithUSpaceAtTheEnd.toCharArray();
        char lastChar = testArray[testArray.length - 1];
        String lastCharAsString = String.valueOf(lastChar);
        assertFalse(" ".equals(lastCharAsString));
        assertFalse(" ".equals(String.valueOf(uspace)));

    }

    /**
     * levinshtein finds the difference
     */
    @Test
    public void testLevenShteinDetection() {
        int testValue = StringUtils.getLevenshteinDistance(testWithUSpaceAtTheEnd, testWithNormalSpace);

        assertEquals(testValue, 1);

    }

    /**
     * simple trim cleans out trailing uspace and levenshtein passes
     */
    @Test
    public void simpleTrimDoesACleanUp() {
        String cleaned = testWithUSpaceAtTheEnd.trim();
        assertEquals(cleaned, testWithNormalSpace);
        int testValue = StringUtils.getLevenshteinDistance(cleaned, testWithNormalSpace);

        assertEquals(testValue, 0);
    }

    /**
     * levinshtein finds the difference when uspace is in the middle
     */
    @Test
    public void testLevenShteinDetectionWillFindUSpaceInTheMiddle() {
        int testValue = StringUtils.getLevenshteinDistance(testWithUSpaceInTheMiddle, testWithNormalSpace);

        assertEquals(testValue, 1);

    }
    
    /**
     * this regex cleans up uspace
     */
    @Test
    public void testPrintRegexRemovesOffendingItem()
    {
        String cleaned = testWithUSpaceInTheMiddle.replaceAll("\\P{Print}", " ");
        assertEquals(cleaned, testWithNormalSpace);
        int testValue = StringUtils.getLevenshteinDistance(cleaned, testWithNormalSpace);

        assertEquals(testValue, 0);
    }

}
