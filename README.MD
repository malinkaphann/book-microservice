# Book and its sub projects

This is the book and sub projects.

## Prerequisites

- openjdk 11
- gradle 7.5

## Scripts

|  services     |   details                      |
|---------------|-----------------------------------------------------|
|   clear.sh    |   remove all docker containers/images/volumes       |
|   start.sh    |   start all containers                              |
|   stop.sh     |   stop all containers                               |

## Roles and permissions

|  ROLE         |   details                      |
|---------------|----------------------------------------------------------------------------------------------|
|   ADMIN       |   can do all                                                                                 |
|   PROFESSOR   |   can search for books and get book detail                                                   |
|   LIBRARIAN   |   can search, get detail, create, update, delete, books, and hold/unhold a book for a user   |
|   STUDENT     |   can search, get detail books                                                               |
|   API_CLIENT  |   can search, and get book detail                                                            |

## How to test?

1. start everything

```bash
./start.sh
```

2. import the postman collection

3. signup a user in role STUDENT

4. login as that student

5. fetch book detail

Different roles can do differeng things.

## Services and Ports

|  services     |  ports  |         details                      |
|---------------|---------|--------------------------------------|
|   postgresql  |   5432  |   database server                    |
|   pgadmin     |   5433  |   postgresql admin panel             |
|   eureka      |   1111  |   eureka server                      |
|   book api    |   8080  |   book api                           |
|   caller api  |   8081  |   api that calls book api            |
|   api doc     |   8082  |   openapi specification on swagger   |


## Postman Collection

The postman collection is in here.

[postman collection](./docs/book_api.postman_collection.json)