
#Test Reporting Sandbox

Code illusrating testng, screenshots and REST comparisons
see also https://github.com/donhenton/selenium-sandbox

## Running the Tests

To run the service and screenshot test:

mvn clean test -Pdev -DtestNgFile=sample 

OR

mvn clean test -Pdev -DtestNgFile=sample -Dremote.server=docker


if the local docker instance is configured, with its url in the
env.properties file of the environment (dev,prod)


## Demonstration

Once the tests are run, the report will be in target/surefire-reports/html/index.html
The reporting code responsible is at  https://github.com/donhenton/reportng, 
and is a forked modification. The mod points to two pages that have been added
to the output.

* target/classes/public_html/index.html (screenshot comparisons)
* target/classes/service_public_html/index.html (json comparisons)

### Screenshot Comparisons
This webpage presents a stored gold file image, compares its to the image
freshly visited, and highlights the differences. Specialized code is used
to generate the image, see MainAppScreenShot.java and ScreenshotComparisonTest.java

### JSON comparisons
This webpage illustrates comparing the output of a REST service to a gold
file of the same service. Comparisons use  Levenshtein algorithm for the
comparison. See RestaurantTests.java



## Update (8/3/2017) 

The current code base needs older selenium so the docker image to use is 
selenium/standalone-firefox:2.48.2

The display page will be at http://dockerlocal:4470/wd/hub where dockerlocal
refers to the ip of your docker-toolbox vm (docker-machine ip to find it) if you
use the dstart.sh script to launch the docker container

At this moment this code uses Selenium < 3, and would need an update if the
selenium driver were changed.

