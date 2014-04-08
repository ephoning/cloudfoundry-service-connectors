CloudFoundry Service Connectors
===============================
This set of (Maven) projects provides bare-bones samples of Cloud Foundry server connectors. That is: Spring framework dependent functionality that allows for a convenient approach to Spring datasource construction (auto reconfiguration) as performed by the standard Cloud Foundry Java buildpack.
For a detailed description of this functionality, please consult https://github.com/spring-projects/spring-cloud and https://github.com/spring-projects/spring-cloud/tree/master/spring-service-connector.


Usage
-------------------------------
To make use of these connectors, do the following:
- Check out these Spring service connector projects (we'll assume for the remainder of this discussion that we want to use the Hawq (*) connector)
- Build the connector project and install to your local Maven repo
- Add a dependency to the Hawq service connector and associated libs to the application project
- Make sure to define a 'bare bones' Spring datasource in the application's 'cloud' profile in its Spring application context 
- Define a "user provided" service instance for the Hawq deployment to be targeted
- Push the application and bind the service instance to the application

(*) Note that the Hawq connector allows for connecting our application to an (external) Hawq deployment. It uses / depends on a PostgreSQL JDBC driver.

####Build and Install Hawq Spring Service Connector
```
$ cd <actual-hawq-spring-connector-directory>
$ mvn clean install
```
This will result in the Hawq Spring connector deployable (jar) ending up in *~/.m2/repository/org/cloudfoundry/hawq-cloud-db-connector/1.0.0-BUILD-SNAPSHOT*
####Add dependency to Hawq Service Connector and Associated Libs
Assuming our application code is structured as a Maven project, add the following dependencies to the applications project's **pom.xml**:
```
<!-- HAWQ service connector -->
<dependency>
    <groupId>org.cloudfoundry</groupId>
    <artifactId>hawq-cloud-db-connector</artifactId>
    <version>1.0.0-BUILD-SNAPSHOT</version>
</dependency>
```
As we need a PostgreSQL driver, also add:
```
<dependency>
	<groupId>org.postgresql</groupId>
	<artifactId>postgresql</artifactId>
	<version>9.2-1003-jdbc4</version>
</dependency>
```
As we rely on the standard Pivotal Cloud Foundry Java buildpack's role in 'auto reconfiguration', make sure the following dependencies are also present:
```
<!-- Spring Cloud -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-service-connector</artifactId>
    <version>0.9.2</version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>cloudfoundry-connector</artifactId>
    <version>0.9.2</version>
</dependency>
```
####Definition of DataSource in Application's 'cloud' Profile
The datasource that "represents" the application's connection to Hawq can be defined as follows in the application's Spring ***application context*** (assuming XML):
```
<beans profile="cloud">
    <cloud:data-source id="dataSource" service-name="hawq">
        <cloud:connection properties="sessionVariables=sql_mode='ANSI';characterEncoding=UTF-8"/>
        <cloud:pool pool-size="20" max-wait-time="200"/>
    </cloud:data-source>
</beans>
```
Note that we are assuming that the name of the 'user provided' service instance - to be defined below - is 'hawq'
Also note that the listed *cloud:connection properties* are not compulsory, but provide an example of what can be specified
####Define a "user provided" service instance for the Hawq Deployment
A key aspect to take into account is the way in which the auto reconfiguration functionality triggers the use of our Hawq Spring service connector. Inspecting the code (specifically: ***HawqServiceInfoCreator.java***) will show that this is based on the (partial) match of a service instance property with the string ***"hawq"*** (note that one of those properties is the name of the service instance itself...).
The interaction with the Pivotal Cloud Foundry CLI to to define the service instance consists of:
```
$ cf cups hawq -p "host, port, user, password, name"

host> <actual-hawq-deployment-hostname-or-ip-address>

port> <actual-hawq-port>

user> <actual-hawq-user-name>

password> <actual-hawq-user-password>

name> actual-hawq-db-name
Creating user provided service hawq in org actual-org-name / space development as ehoning@gopivotal.com...
OK
$
```
####Push and Bind the Application
Finally, we can push and bind the Hawq service instance.
```
$ cd <actual-application-project-dir>
$ cf push <application-name> -p <location-of-application-deployable> --no-start
$ cf bind-service <application-name> hawq
$ cf push <application-name> -p <location-of-application-deployable>
```
At this point the application should have a a valid Hawq-connected datasource to make use of...
