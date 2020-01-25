# SWAPI

## Summary

- Description
- Endpoints
- Setup
    - Requirements
    - Run
- TODO


## Description

A REST API that connects to a NoSQL database to store Star Wars planets information.

This API performs regular HTTP operations, like GET, PUT, POST and DELETE. The GET endpoint can be used to query the database searching by name and/or id.

Planet information stored includes name, terrain and climate. It also returns the number of appearances in films for each planet. This information is calculated by calling the [swapi, the public Star Wars API](https://swapi.co/).

The following technologies were applied: Java, Spring Boot, MongoDB, Docker, CircleCI, Swagger.

## Endpoints

When running the application, endpoint documentation generated with Swagger 3.0 can be found in the root of the api [localhost:8090/swapi/](http://localhost:8090/swapi/).


## Setup

### Requirements

- Install Docker

### Run

After cloning the project and installing Docker, go to the project root folder and run

```shell
docker-compose up
```

This starts up 2 containers: one for the MongoDB and one for the api. 

The api container is generated first instantiating a maven image to compile the code and then copying the generated classes to a open-jdk container that is responsible to start the application at host's port 8090.

## TODO
1. - [x] HATEOAS
2. - [ ] Tests
3. - [ ] CircleCI
4. - [x] Exceptions and Exception Handler
5. - [ ] Localization
6. - [x] Fix Swagger docs
7. - [ ] Cache (?)