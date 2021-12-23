# Medication Service 

[![Test](https://github.com/Thulana/Medication-Service/actions/workflows/gradle.yml/badge.svg)](https://github.com/Thulana/Medication-Service/actions/workflows/gradle.yml)
[![codecov](https://codecov.io/gh/Thulana/Medication-Service/branch/master/graph/badge.svg?token=LOH0DT0XJN)](https://codecov.io/gh/Thulana/Medication-Service)

Medication Service is a full-fledged SpringBoot microservice for maintaining a set of medications and related diseases

|                      	    |                             	                                  |
|---------------------------|-----------------------------------------------------------------|
| **Type**                	| Spring REST microservice 	                                  |
| **Java Version**          | 11                           	                                  |
| **Spring Boot**         	| 2.5.6.RELEASE                	                                  |
| **Authentication**        | Basic Authentication                                            |
| **Persistence**           | SQLite                                                          |

## Table Of Contents

1. [Motivation](#motivation)
2. [API Documentation](#api-documentation)
3. [Getting Started](#getting-started)
   1. [Quick Start](#quick-start)
   2. [Local](#locally)
   3. [Docker](#via-docker-manually)
4. [Developer Notes](#developer-notes)
    1. [Environment Setup](#environment-setup)
       1. [Environment Variables](#environment-variables)
       2. [Database](#database)
       3. [Java](#java)
       4. [Build Tools](#build-tools)
       5. [API Documentation Standard](#api-documenting)
    2. [Testing](#testing)
    3. [Continuous Integration](#continuous-integration)
5. [Developer Checklist](#developer-checklist)
8. [References](#references)

## Motivation

Providing a better search capability for a set of medications and the diseases related is quite challenging. While persisting the related data,
target is to provide a robust interface to do full text searches

## API Documentation
| REST Endpoint        	                           | Description                  	                                                                |
|--------------------------------------------------|------------------------------------------------------------------------------------------------|
| `GET /medications`        	                   | For a given search term find the related medications                                           | 
| `PUT /medications`        	                   | Create / modify medications (please refer swagger documentation for comprehensive information) |

* NOTE: API will require basic authentication

## Getting Started

### Quick start
One step local orchestration script is provided for running the service locally. **Docker** is required.

`Usage: ./infrastructure/scripts/start.sh -p port -u username -e password`

> -p Port for running the service
> 
> -u Username of the authenticated user - mandatory
> 
>-e Password of the authenticated user - mandatory


### locally
Issue following commands from root folder of the project.
Needs to have environment variables exposed. refer: [Environment Variables](#environment-variables)

* use `./gradlew build` to build the project. Will require java 11 and gradle.
* use `./gradlew bootRun` to execute the program.

### Via docker manually

Issue following commands from root folder of the project

* Build : `docker build -t <image-name> -f infrastructure/docker/Dockerfile .`
* Run: `docker run -e USER_NAME=<user_name> -e PASSWORD=<password> -p 8080:8080 -td <image-name>`

## Developer Notes

### Environment setup

#### Environment Variables

Following environment variables will be used by the service.

| Variable Name        	                           | Description                  	                |
|--------------------------------------------------|------------------------------------------------|
| `USER_NAME`        	                           | Authenticated user name                        |
| `PASSWORD`                                       | Authenticated user password                    |

#### Database

* SQLite database
* Custom SQL dialect as default dialect is not supported due to SQLite limitations
* Hibernate as the ORM (persistence framework)

#### Java

* JDK 11 is required for development
* SpringBoot 2.5.6

#### Build tools

* Gradle - refer the build.gradle for configurations

#### API Documentation Standard

* Swagger - Swagger:2.0
* Available on path: <HOST_URL>/swagger-ui/index.html

### Testing

Utilize unit and functional test to provide proper test harness.

* Junit5 framework with Mockito for mock support
* Execute the tests via `./gradlew test`
* Coverage report will be generated upon successful completion 
* JaCoCo is used for code coverage generation
* Use `./gradlew jacocoTestReport` command to generate the coverage report (report can be found in `./build/coverage`)
* Rest Assured is used for functional testing

### Continuous Integration

* Project is version controlled and available in GitHub
* Automated Build pipeline for build and test the service
* CodeCov is used for coverage analysis and PR checks

## Developer Checklist

* [x] Add Swagger UI for API interactions
* [x] Migrate to undertow from tomcat server
* [x] Configure Log4j2 Async logging  
* [ ] Improve security (token based, user controller)
* [ ] Improve data persistence ( Postgres or MySQL database )
* [ ] Static code analysis integration
* [ ] Cloud deployment configuration (kube) and infrastructure versioning (Helm)

## References

* Initialization - https://start.spring.io/
* Swagger - http://springfox.github.io/springfox/docs/current/
* SQLite with JPA - https://www.baeldung.com/spring-boot-sqlite
* SQlite FTS - https://www.sqlite.org/fts3.html



