A Spring Boot project that provides a secure and scalable service for shortening URLs, using PostgreSQL as the database and JWT for authentication.
Transaction Management System
Project Overview
This project implements a clean-architecture-based URL shortening system that allows users to shorten long URLs, track clicks, and manage expiration. The system supports JWT-based authentication, user-specific URLs, and automatic cleanup of expired links. Built with Spring Boot, it ensures a modular, testable, and maintainable backend design.
Features
Shorten URLs: Authenticated users can shorten long URLs.
Expiration Time: Expired URLs are automatically deleted.
Click Counter: Each short URL tracks click count.
Scheduled Cleanup: Removes expired URLs every 5 minutes.
Secure REST API: With Spring Security and token validation.
Database Structure
The system uses two main tables:
users: Stores user  details (e.g., userId, username,lastname, password, createdAt,updatedAt etc.).
urls: Stores url records, id,originalUrl,shortUrl,expirationTime,clickCount,createdAt,updatedAt and 
 associated user.
CREATE TABLE users (
    user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    lastname VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE urls (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    original_url TEXT NOT NULL,
    short_url VARCHAR(255) UNIQUE NOT NULL,
    expiration_time TIMESTAMP,
    click_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    user_id BIGINT,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(user_id)
);



Project Structure
url-shortener/
├── src/
│   └── main/
│       ├── java/com/example/url-shortener
│       │   ├── controller/      # REST API endpoints and request validation
│       │   ├── service/         # Business logic and business rules
│       │   ├── repository/      # Data access layer (JPA/Hibernate)
│       │   ├── model/           # Domain models, DTOs, enums
│       │   ├── security/        # jwt authentication logic 
│       │   ├── config/          # Swagger and security configurations
│       │   ├── utils/           # Logging 
│       │   ├── exception/       # Custom exceptions and error handling
│       │   └── UrlShortenerApplication.java  # Application entry point
│       └── resources/
│           ├── application.yml  # App configuration (DB, ports, etc.)
│           └──   
├── src/test/                    # Unit and integration tests
├── pom.xml                      # Maven build file and dependencies
└── README.md                    # Project documentation

## Setup
 **Install Java 17**:
   Install Maven:
   Install PostgreSQL:
   Configure Database:
   Build and Run:

 Libraries
Spring Boot Starters: Simplifies the setup of various Spring modules, including web development, data access, and validation.
Jackson: For JSON serialization and deserialization, which is essential for REST APIs to handle data exchange.
SpringDoc OpenAPI: For auto-generating and exposing API documentation through Swagger UI.
Lombok: Reduces boilerplate code by automatically generating getters, setters, and constructors during compilation.
Logstash Logback Encoder: For structured logging compatible with Logstash, useful for centralized logging and monitoring.
PostgreSQL: The database used for storing url and users information.
