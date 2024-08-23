# abc

## About


- Create,Read,Update and Delete(CRUD) books


This app is built using Java(Spring Boot)


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

- Java 17 sdk

- Postman

### Installing

A step by step series of examples that tell you how to get a development environment running

- Clone the project repository


HTTPS: `git clone https://github.com/BILLthebuilder/abc.git`

SSH: `git clone git@github.com:BILLthebuilder/abc.git`

- Change the directory

`cd abc`

- To compile a local build

```bash
./mvnw clean compile package
```

- To run a regular development build

```bash
java -jar /your_clone_directory/abc/target/0.0.1.jar
```

### Book endpoints


| Request | Endpoint                                        | Function          |
|---------|-------------------------------------------------|-------------------|
| POST    | `/api/v1/products/create`                       | Create a new book |
| GET     | `/api/v1/products?page=?&size=?&sort=?&order=?` | Get all book      |
| GET     | `/api/v1/products/{Id}`                         | Get a book by ID  |
| PUT     | `/api/v1/products/{Id}`                         | Update a book     |
| DELETE  | `/api/v1/products/{Id}`                         | Delete a book     |

## Basic Auth Credentials
username: user
password: user@123

## Running the tests

```bash
./mvnw test
```

## Deployment

- Coming soon
## Built With

- [Spring](https://spring.io) - The web framework used

## Versioning

- Version 1(v1) of the API

- To view the swagger docs visit the below url after loading the project locally.
- Remember to change the port accordingly

```bash
http://localhost:8080/swagger-ui/index.html
```
- The Open Api V3 spec document can be viewed and retrieved from the below url
- Remember to change the port accordingly
```bash
http://localhost:8080/v3/api-docs
```
## Authors

### Bill Kariri
