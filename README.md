# SWAPI
[![CircleCI](https://circleci.com/gh/pedrogonic/SWAPI/tree/master.svg?style=svg)](https://circleci.com/gh/pedrogonic/SWAPI/tree/master)
[![codecov](https://codecov.io/gh/pedrogonic/SWAPI/branch/codecov/graph/badge.svg)](https://codecov.io/gh/pedrogonic/SWAPI)
## Summary

- Description
- Setup
    - Requirements
    - Run
- Endpoints
- TODO


## Description

A REST API that connects to a NoSQL database to store Star Wars planets information.

This API performs regular HTTP operations, like GET, PUT, POST and DELETE. The GET endpoint can be used to query the database searching by name and/or id.

Planet information stored includes name, terrain and climate. It also returns the number of appearances in films for each planet. This information is calculated by calling the [swapi, the public Star Wars API](https://swapi.co/).

The following technologies were applied: Java, Spring Boot, MongoDB, Docker, CircleCI, Swagger.

## Setup

### Requirements

- Install Docker

### Run

After cloning the project and installing Docker, go to the project root folder and run

```shell
docker-compose up
```

This starts up 2 containers: one for MongoDB and one for the api. 

The api container is generated first instantiating a maven image to compile the code and then copying the generated classes to a open-jdk container that is responsible to start the application at host's port 8090.

## Endpoints

When running the application, endpoint documentation generated with Swagger 3.0 can be found in the root of the api [localhost:8090/swapi/](http://localhost:8090/swapi/), where all the endpoints can be tested.

### GET swapi/planets?page={page}&size={pageSize}&name={searchName}

**Description:** Gets a paginated list with all stored planets, if the name parameter is not specified. If it is, it returns a list with only one planet that matches the queried name.

#### Request

**Request Parameters:** 
- `page` - not required - number of the requested page - default 0;
- `size` - not required - number of planets per page - default 10;
- `name` - not required - name to be searched - default empty.

#### Response

**Response Codes:** **200** (OK), **504** (Gateway Timeout) - when original SWAPI is unreachable.

**Curl:**

E.g.:
```shell
curl -X GET "http://localhost:8090/swapi/planets" -H "accept: application/json;charset=UTF-8"
```

**Request URL:**

E.g.:
```shell
http://localhost:8090/swapi/planets
```

**Response Body:**

E.g.:
```json
 {
   "links": [
     {
       "rel": "self",
       "href": "http://localhost:8090/swapi/planets"
     }
   ],
   "content": [
     {
       "id": "5e25186e47883c4cfa37ad76",
       "name": "Alderaan",
       "climate": "temperate",
       "terrain": "grasslands, mountains",
       "filmCount": 2,
       "links": [
         {
           "rel": "self",
           "href": "http://localhost:8090/swapi/planets/5e25186e47883c4cfa37ad76"
         },
         {
           "rel": "planets",
           "href": "http://localhost:8090/swapi/planets"
         }
       ]
     },
     {
       "id": "5e26493f4237222e99761ca9",
       "name": "Hoth",
       "climate": "frozen",
       "terrain": "tundra, ice caves, mountain ranges",
       "filmCount": 1,
       "links": [
         {
           "rel": "self",
           "href": "http://localhost:8090/swapi/planets/5e26493f4237222e99761ca9"
         },
         {
           "rel": "planets",
           "href": "http://localhost:8090/swapi/planets"
         }
       ]
     },
     {
       "id": "5e2c197cd312cc0f5d5e2adb",
       "name": "Naboo",
       "climate": "temperate",
       "terrain": "grassy hills, swamps, forests, mountains",
       "filmCount": 4,
       "links": [
         {
           "rel": "self",
           "href": "http://localhost:8090/swapi/planets/5e2c197cd312cc0f5d5e2adb"
         },
         {
           "rel": "planets",
           "href": "http://localhost:8090/swapi/planets"
         }
       ]
     }
   ]
 }
```

### GET swapi/planets/{id}

**Description:** Finds a planet by its id.

#### Request

**Request Parameters:** 
- `id` - required - id of searched planet.

#### Response

**Response Codes:** **200** (OK), **504** (Gateway Timeout) - when original SWAPI is unreachable.

**Curl:**

E.g.:
```shell
curl -X GET "http://localhost:8090/swapi/planets/5e25186e47883c4cfa37ad76" -H "accept: application/json;charset=UTF-8"
```

**Request URL:**

E.g.:
```shell
http://localhost:8090/swapi/planets/5e25186e47883c4cfa37ad76
```

**Response Body:**

E.g.:
```json
{
  "id": "5e25186e47883c4cfa37ad76",
  "name": "Alderaan",
  "climate": "temperate",
  "terrain": "grasslands, mountains",
  "filmCount": 2,
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8090/swapi/planets/5e25186e47883c4cfa37ad76"
    },
    {
      "rel": "planets",
      "href": "http://localhost:8090/swapi/planets"
    }
  ]
}
```

### POST swapi/planets

**Description:** Creates a new planet.

#### Request

**Request Body:**

E.g.:
```json
{
  "name": "Tatooine",
  "climate": "arid",
  "terrain": "desert"
}
```

#### Response

**Response Codes:** **201** (Created), **500** (Internal Server Error) - when planet is already in the database `BUG`, **404** (Not Found) - when planet is not found in original SWAPI, **504** (Gateway Timeout) - when original SWAPI is unreachable.

**Curl:**

E.g.:
```shell
curl -X POST "http://localhost:8090/swapi/planets" -H "accept: application/json;charset=UTF-8" -H "Content-Type: application/json" -d "{ \"name\": \"Tatooine\", \"climate\": \"arid\", \"terrain\": \"desert\"}"
```

**Request URL:**

```shell
http://localhost:8090/swapi/planets
```

**Response Body:**

E.g.:
```json
{
  "id": "5e2e65f7c2d4272c724821ef",
  "name": "Tatooine",
  "climate": "arid",
  "terrain": "desert",
  "filmCount": 5,
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8090/swapi/planets/5e2e65f7c2d4272c724821ef"
    },
    {
      "rel": "planets",
      "href": "http://localhost:8090/swapi/planets"
    }
  ]
}
```

### PUT swapi/planets/{id}

**Description:** Updates the planet with the given id.

#### Request

**Request Parameters:** 
- `id` - required - id of planet to be updated.

**Request Body:**

E.g.:
```json
{
  "name": "Tatooine",
  "climate": "dry",
  "terrain": "desert"
}
```

#### Response

**Response Codes:** **201** (Created), **500** (Internal Server Error) - when planet is already in the database `BUG`, **404** (Not Found) - when planet is not found in original SWAPI, **504** (Gateway Timeout) - when original SWAPI is unreachable.

**Curl:**

E.g.:
```shell
curl -X PUT "http://localhost:8090/swapi/planets/5e2e65f7c2d4272c724821ef" -H "accept: application/json;charset=UTF-8" -H "Content-Type: application/json" -d "{ \"name\": \"Tatooine\", \"climate\": \"dry\", \"terrain\": \"desert\"}"
```

**Request URL:**

E.g.:
```shell
http://localhost:8090/swapi/planets/5e2e65f7c2d4272c724821ef
```

**Response Body:**

E.g.:
```json
{
  "id": "5e2e65f7c2d4272c724821ef",
  "name": "Tatooine",
  "climate": "dry",
  "terrain": "desert",
  "filmCount": 5,
  "links": [
    {
      "rel": "self",
      "href": "http://localhost:8090/swapi/planets/5e2e65f7c2d4272c724821ef"
    },
    {
      "rel": "planets",
      "href": "http://localhost:8090/swapi/planets"
    }
  ]
}
```

### DELETE swapi/planets/{id}

**Description:** Deletes the planet with the given id.

#### Request

**Request Parameters:** 
- `id` - required - id of planet to be deleted.

#### Response

**Response Codes:** **204** (No Content).

**Curl:**

E.g.:
```shell
curl -X DELETE "http://localhost:8090/swapi/planets/5e2e65f7c2d4272c724821ef" -H "accept: application/json;charset=UTF-8"
```

**Request URL:**

E.g.:
```shell
http://localhost:8090/swapi/planets/5e2e65f7c2d4272c724821ef
```

## TODO
1. - [x] HATEOAS
2. - [x] Tests
3. - [x] CircleCI
4. - [x] Codecov
5. - [x] Exceptions and Exception Handler
6. - [ ] Localization
7. - [x] Fix Swagger docs
8. Cache
    - [ ] Cache API - Cache this API responses
    - [x] Cache SWAPI - Cache original SWAPI calls