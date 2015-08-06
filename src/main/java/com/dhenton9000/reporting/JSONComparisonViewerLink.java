/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.reporting;

import org.uncommons.reportng.IExternalLink;

/**
 * This class places a link to the JSON comparison viewers on the testNG
 * reports.
 *
 * @author dhenton
 */
public class JSONComparisonViewerLink implements IExternalLink {

    public static final String JSON_COMPARISON_FOLDER = "../../classes/service_public_html/";
    public static final String IMAGE_COMPARISON_FOLDER = "../../classes/public_html/";

    @Override
    public String renderLinkText() {
        return "<a target=\"_blank\" href=\"" + JSON_COMPARISON_FOLDER 
                + "index.html\">JSON Comparisons</a> "
                
                + "<a target=\"_blank\" href=\"" + IMAGE_COMPARISON_FOLDER 
                + "index.html\">Image Comparisons</a> "
                
                
                ;
    }

}