# REST API documentation

This is my first Rest API document, ver.25112020-1

# REST API

The REST API to the example app is described below.

## Get list of meals

### Request

`GET /meals/`

    curl --location --request GET 'http://localhost:8080/topjava/rest/meals'

### Response

    {
        "id": 100008,
        "dateTime": "2020-01-31T20:00:00",
        "description": "Ужин",
        "calories": 510,
        "excess": true
    },
    {
        "id": 100007,
        "dateTime": "2020-01-31T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": true
    },
    {
        "id": 100006,
        "dateTime": "2020-01-31T10:00:00",
        "description": "Завтрак",
        "calories": 500,
        "excess": true
    },
    {
        "id": 100005,
        "dateTime": "2020-01-31T00:00:00",
        "description": "Еда на граничное значение",
        "calories": 100,
        "excess": true
    },
    {
        "id": 100004,
        "dateTime": "2020-01-30T20:00:00",
        "description": "Ужин",
        "calories": 500,
        "excess": false
    },
    {
        "id": 100003,
        "dateTime": "2020-01-30T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": false
    },
    {
        "id": 100002,
        "dateTime": "2020-01-30T10:00:00",
        "description": "Завтрак",
        "calories": 500,
        "excess": false
    }

## Get list of meals filtered

### Request

`GET /meals/filter?startDate=2020-01-30&endDate=2020-01-31&startTime=12:50&endTime=20:20`

    curl --location --request GET 'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-30&endDate=2020-01-31&startTime=12:50&endTime=20:20'

### Response

        {
            "id": 100008,
            "dateTime": "2020-01-31T20:00:00",
            "description": "Ужин",
            "calories": 510,
            "excess": true
        },
        {
            "id": 100007,
            "dateTime": "2020-01-31T13:00:00",
            "description": "Обед",
            "calories": 1000,
            "excess": true
        },
        {
            "id": 100004,
            "dateTime": "2020-01-30T20:00:00",
            "description": "Ужин",
            "calories": 500,
            "excess": false
        },
        {
            "id": 100003,
            "dateTime": "2020-01-30T13:00:00",
            "description": "Обед",
            "calories": 1000,
            "excess": false
        }

## Create a new Meal

### Request

`POST /meals/`

    curl --location --request POST 'http://localhost:8080/topjava/rest/meals' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "dateTime": "2020-11-23T13:00:00",
        "description": "Новая еда",
        "calories": 1500
    }'

### Response

    {
        "id": 100011,
        "dateTime": "2020-11-23T13:00:00",
        "description": "Новая еда",
        "calories": 1500,
        "user": null
    }

## Get a meal by id

### Request

`GET /meals/id`

    curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100008'

### Response

    {
          "id": 100008,
          "dateTime": "2020-01-31T20:00:00",
          "description": "Ужин",
          "calories": 510,
          "user": null
     }

## Update a meal

### Request

`PUT /meals/:id`

    curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100005' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "id": 100005,
        "dateTime": "2020-11-23T13:10:00",
        "description": "Обновленная еда",
        "calories": 2500
    }'

### Response

    HTTP/1.1 200 OK

## Delete a meal

### Request

`DELETE /meals/id`

    curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100007'

### Response

    HTTP/1.1 204 No Content