
# Test Reporting Sandbox

Code illustrating testng, screenshots and REST comparisons
see also https://github.com/donhenton/selenium-sandbox

## Running the Tests

To run the service and screenshot test:

mvn clean test -Pdev -DtestNgFile=sample 

OR

mvn clean test -Pdev -DtestNgFile=sample -Dremote.server=docker


if the local docker instance is configured,  its url will be in the
env.properties file of the environment (dev,prod)


## Demonstration

Once the tests are run, the report will be in target/surefire-reports/html/index.html
The reporting code responsible is at  https://github.com/donhenton/reportng, 
and is a forked modification. The mod points to two pages that have been added
to the output.

* target/classes/public_html/index.html (screenshot comparisons)
* target/classes/service_public_html/index.html (json comparisons)
* testng reporting suite -- target/surefire-reports/html/index.html

### Screenshot Comparisons
This webpage presents a stored gold file image, compares its to the image
freshly visited, and highlights the differences. Specialized code is used
to generate the image, see MainAppScreenShot.java and ScreenshotComparisonTest.java

### JSON comparisons
This webpage illustrates comparing the output of a REST service to a gold
file of the same service. Comparisons use  Levenshtein algorithm for the
comparison. See RestaurantTests.java



## Update (6/28/2019) 

The current code base needs requires firefox. So docker image that works is

https://github.com/SeleniumHQ/docker-selenium

```
docker run -d -p 4470:4444 --shm-size 2g selenium/standalone-firefox:3.141.59-radium
```

The display page will be at http://localhost:4470/wd/hub where dockerlocal
refers to the ip of your docker-toolbox vm (docker-machine ip to find it) if you
use the dstart.sh script to launch the docker container

At this moment this code uses Selenium < 3, and would need an update if the
selenium driver were changed.

## Dependencies

This project depends on two other projects in my repository, which you will
have to install locally:

* https://github.com/donhenton/selenium-sandbox
* https://github.com/donhenton/embedded-jetty-sandbox

An XML Utils library is also required:

https://github.com/donhenton/code-attic/tree/master/utils/XmlUtils
