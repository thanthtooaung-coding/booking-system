# Booking System - Setup Guide

## Prerequisites

Make sure you have the following installed:

- **Java 21**
- **MySQL** (running locally on port `3306`)
- **Docker** (for Redis, unless you prefer local installation)

## 1. Redis Setup

You can set up Redis using Docker with the following commands:

```bash
docker run -d --name redis-stack-server -p 6379:6379 redis/redis-stack:latest
docker exec -it redis-stack-server redis-cli
```

Alternatively, you can install Redis locally and ensure it's running on port `6379`.

## 2. Application Configuration

The application uses the following Spring Boot configuration:

```yaml
spring:
  application:
    name: booking-system
  datasource:
    url: jdbc:mysql://localhost:3306/booking_system_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password: mysql
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 60000
springdoc:
  paths-to-match: /booking-system/api/v1/**
  api-docs:
    path: /v3/api-docs
```

> 💡 **Note:** There's no need to manually import the database schema or sample data. The application will automatically create the schema and populate it with sample data using `CommandLineRunner`.

## 3. Dependencies

Here’s a summary of key dependencies used in the project:

- Spring Boot starters (Web, Security, JPA, Redis, Quartz, Validation)
- MySQL Connector
- Lombok
- ModelMapper
- JSON Web Token (JJWT)
- JavaDotEnv
- Redisson
- SpringDoc (OpenAPI 3)

You can view the full list in `build.gradle`.

## 4. Running the Application

1. Ensure MySQL and Redis are running.
2. Clone the repository and navigate into the project directory.
3. Build and run the application using your IDE or the command line:

```bash
./gradlew bootRun
```

## 5. API Documentation

Once the application is running, access the API documentation at:

```
http://localhost:8080/swagger-ui.html
```
