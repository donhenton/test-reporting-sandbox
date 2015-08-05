/* global failedData, JSON3 */

///////////////////////////////////////////////////////////////
var dateInformation =
        {
            "qa": {
                "DashboardTests-->testDashboardBrandProfileIsEqualToGoldFile": "7/10/2015 11:51",
                "ExplainerTests-->testExplainerInterests": "7/10/2015 11:51",
                "UsersTest-->testUserByEmail": "7/10/2015 11:51",
                "UsersTest-->testCompanyUserByUUId": "7/10/2015 11:51",
                "UsersTest-->testGetCompany": "7/10/2015 11:51",
                "UsersTest-->testGetRole": "7/10/2015 11:51",
                "ExplanationTests-->testResultForExplanation": "7/10/2015 11:51"
            },
            "dev1": {
                "DashboardTests-->testDashboardBrandProfileIsEqualToGoldFile": "7/10/2015 11:51",
                "ExplainerTests-->testExplainerInterests": "7/10/2015 11:51",
                "UsersTest-->testUserByEmail": "7/10/2015 11:51",
                "UsersTest-->testCompanyUserByUUId": "7/10/2015 11:51",
                "UsersTest-->testGetCompany": "7/10/2015 11:51",
                "UsersTest-->testGetRole": "7/10/2015 11:51",
                "ExplanationTests-->testResultForExplanation": "7/10/2015 11:51"
            }
        }
//update this variable with dates when gold files are created
//you can generate a formatted sample in the formatter tools tab of 
//index.html, which will give the sample for failed tests
//this variable should contain an entry for each maintained
//gold file



///////////////// formatter functions ////////////////////////
function copyToFormatter()
{
    var selectIdx = -1;
    if ($('#testList').length)
    {
        //not on single
        selectIdx = $('#testList').val();
    }
    else
    {
        //on single page
         
        selectIdx = window.frameCompareIndex;
        
    }
    var actualStr =
            JSON3.stringify(failedData["comparisons"][selectIdx].actual);
    $('#formatArea').val(actualStr);
    $('#navTabs a:last').tab('show') // Select last tab

}

function prettyPrintFormatter()
{
    var json = getToolTextASJSON();
    if (json != null)
    {
        $('#formatArea').val(JSON3.stringify(json, null, 3));
        reportParseProblem(false);
    }
    else
    {
        reportParseProblem(true);
    }

}
function unFormatFormatter()
{
    var json = getToolTextASJSON();
    if (json != null)
    {
        $('#formatArea').val(JSON3.stringify(json));
        reportParseProblem(false);
    }
    else
    {
        reportParseProblem(true);
    }

}


function reportParseProblem(show)
{
    if (show == true)
    {
        $("#parseWarning").text("Problem parsing JSON");
    }
    else
    {
        $("#parseWarning").text("");
    }
}

function getToolTextASJSON()
{
    var t = $('#formatArea').val();
    if (t != null)
    {
        t = t.trim();
    }
    else
    {
        t = "";
    }
    var jsonInfo = null;
    try
    {
        jsonInfo = $.parseJSON(t);
    }
    catch (e)
    {

    }
    return jsonInfo;
}

function composeDateSample()
{
    var sample = {};
    var date = new Date();
    var strDate = date.getMonth() + 1 + "/" + date.getDate() + "/" + date.getFullYear() + " ";
    strDate = strDate + date.getHours() + ":" + date.getMinutes();
    failedData["comparisons"].forEach(function (d, i)
    {

        sample[getDescription(d)] = strDate;
    });
    var text = "var dateInformation = \n" + JSON3.stringify(sample, null, 3);
    text = text + "\n\nPaste this into comparision_code.js to place date\n" +
            "information about when gold files where generated";

    $('#formatArea').val(text);

}

/////////////////////////////////////////////////////////////


function doCompare()

{
    var selectIdx = $('#testList').val();
    compareIndexItem(selectIdx);

}

function compareIndexItem(v)
{
    var testItem = failedData["comparisons"][v];
    var expectedStr =
            JSON3.stringify(testItem.expected, null, 2);
    var actualStr =
            JSON3.stringify(testItem.actual, null, 2);
    var wikEdDiff = new WikEdDiff();
//    wikEdDiffConfig.fullDiff = true;
//    wikEdDiffConfig.showBlockMoves = false;
    $('#expected').val(expectedStr);
    $('#actual').val(actualStr);
    var diffHtml = wikEdDiff.diff(expectedStr, actualStr);
    $('#compare').empty();
    $('#compare').html(diffHtml);
    if (!$('#legend').is(":visible"))
    {
        $('#legend').show();
        $('#copy-button').css("visibility", "visible");
    }
    //handle the date information

    var dateInformationForEnv = dateInformation[failedData.env];
    var dateText = null;

    if (typeof dateInformationForEnv !== 'undefined')
    {

        dateText = dateInformationForEnv[getDescription(testItem)];


    }

    if (dateText === null) {
        dateText = "";
    }
    else
    {
        dateText = "<br/>(" + dateText + ")";
    }
    $('#dateText').html(dateText);
}




function loadSelections()
{

    var listObj = $('#testList');
    var firstChoice = false;
    failedData["comparisons"].forEach(function (d, i)
    {
        var t = "";
        if (firstChoice === false)
        {
            t = "<option selected value=\"" + i + "\">" + getDescription(d) + "</option>";
            firstChoice = true;
        }
        else
        {
            t = "<option value=\"" + i + "\">" + getDescription(d) + "</option>";
        }

        listObj.append(t);
    });
    $('#envInfo').text(failedData.date + " (" + failedData.env + ")");
}

function getDescription(testItem)
{
    return testItem.testName + "-->" + testItem.methodName;
}



/////////// singleCompareCode ////////////////////////////

/**
 * 
 * read a query string variable. used on the single page
 * viewer page
 * 
 * @param {type} variable
 * @returns {Boolean|getQueryVariable.pair}
 */
function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] == variable) {
            return pair[1];
        }
    }
    return(false);
}

function setUpSingleCompare()
{
    var testName = getQueryVariable("testName");
    var methodName = getQueryVariable("methodName");
    $("#testName").text(testName);
    $("#methodName").text(methodName);
    $('#envInfo').text(failedData.date + " (" + failedData.env + ")");
    failedData["comparisons"].forEach(function (d, i)
    {
        if (d.testName === testName && d.methodName === methodName)
        {
            compareIndexItem(i);
            window.frameCompareIndex = i;
        }

    });





}


 