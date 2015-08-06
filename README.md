
To run the service and screenshot test:

mvn clean test -Pdev -DtestNgFile=sample 

OR

mvn clean test -Pdev -DtestNgFile=sample =Dremote.server=docker


if the local docker instance is configured, with its url in the
env.properties file of the environment (dev,prod)
