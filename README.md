# Spring Boot Security Microservice

This application was generated using https://start.spring.io/

---

## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [Environment Variables](#environment-variables)
* [Setup](#setup)
* [Features](#features)
* [References](#references)
* [Contact](#contact)

---

## General info
Back-end service which provides security-related resources such as user and JSON Web Token(JWT) endpoints using Spring Boot Reactive.

---

## Technologies
* Spring Boot
* PostgreSQL JDBC Driver

---

## Environment Variables
Following table is a **mandatory** environment variables used in this project.

| Variable Name | Datatype | Description |
| --- | --- | --- |
| APP_HOST | String | Server host |
| APP_PORT | Number | Server port |
| DB_URL | String | Database URL |
| DB_USERNAME | String | Database username |
| DB_PASSWORD | String | Database password |
| LOG_LEVEL | String | Log level |

---

## Setup
To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean spring-boot:run
```

To build application into native image:
```
./mvnw clean spring-boot:build-image -DskipTests
```

---

## Features

### TODO
* Can create, update, delete user resource.
* Add JWT authentication for resource handler.
* Add GraphQL to query resource.
* Add JWT refresh token to get new access token.

---

## References
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.5/maven-plugin/reference/html/#build-image)
* [Error Handling in Spring Webflux](https://dzone.com/articles/error-handling-in-spring-webflux)
* [Securing Spring WebFlux Reactive APIs with JWT Auth](https://www.devglan.com/spring-security/spring-security-webflux-jwt)

---

## Contact
Created by [Anas Juwaidi](mailto:anas.didi95@gmail.com)
