# WAES Diff Microsevices Architecture

## Main Technologies:

Java 11
Gradle
Spring Boot 2.2.5

* discovery-service
  * Eureka Server
* api-gateway
  * Eureka Discovery Client
  * OpenFeign
  * Rest Repositories
  * Spring Web
  * Hystrix
  * Lombok
* diff-service
  * Spring Boot Actuator
  * Eureka Discovery Client
  * Spring Data JPA
  * H2 Databse
  * Rest Repositories
  * Spring Web
  * Spring Boot DevTools
  * Lombok
  
## Run

* Import all three modules as a Gradle project;
* Execute Gradle task called application:bootRun in order to start each application on separate terminals;
* Access the application with following URL: 
    * service-discovery - http://localhost:8761/
    * api-gateway - http://localhost:8080/api/
    * diff-service - http://localhost:8090/
* The following endpoints will be provided by the application and they can be accessed through Postman for example:
    * GET   http://localhost:8080/api/v1/diff/{id}
    * POST  http://localhost:8080/api/v1/diff/{id}/left
    * POST  http://localhost:8080/api/v1/diff/{id}/right

## Improvements

* Spring Cloud Config to externalize configuration;
* Security Configuration - Spring Security/OAuth2; 
* Refactor logic for insights and result formatter;
* Improve exception and fallback handling;
* Enhance testing scenarios;
* Improve documentation and instructions;
