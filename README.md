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
* PostgreSQL JDBC/R2DBC Driver
* Liquibase

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
| JWT_SECRET | String  | JWT signature secret |
| JWT_ISSUER | String | JWT issuer |
| JWT_ACCESS_TOKEN_VALIDITY_MINUTES | Number | Total time in minutes for JWT access token validity |
| LOG_LEVEL | String | Log level |
| GRAPHIQL_ENABLED | Boolean | Flag to enable GraphiQL. Keep note GraphiQL should be disabled in production mode! |

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

To run liquibase migration
```
./mvnw clean liquibase:update
```

To run liquibase rollback. The rollback is using rollback tag. Change the tag in `pom.xml` accordingly.
```
./mvnw clean liquibase:rollback
```

---

## Features
- [x] Can create, update, delete user resource.
- [x] Add JWT authentication for resource handler.
- [x] Add GraphQL to query resource.

### TODO
- [ ] Add JWT refresh token to get new access token.

---

## References
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.4.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.4.5/maven-plugin/reference/html/#build-image)
* [Error Handling in Spring Webflux](https://dzone.com/articles/error-handling-in-spring-webflux)
* [Securing Spring WebFlux Reactive APIs with JWT Auth](https://www.devglan.com/spring-security/spring-security-webflux-jwt)
* [Liquibase 3.6.x data types mapping table](https://dba-presents.com/index.php/liquibase/216-liquibase-3-6-x-data-types-mapping-table)
* [Handle R2DBC in Spring Projects](https://medium.com/sipios/handle-r2dbc-in-spring-projects-fa96e65ca24d)
* [GraphQL Java Kickstart Samples](https://github.com/graphql-java-kickstart/samples)

---

## Contact
Created by [Anas Juwaidi](mailto:anas.didi95@gmail.com)
