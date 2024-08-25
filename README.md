# ToDo Application

## Overview

The ToDo Application is a Java-based service designed to manage to-do items efficiently. Developed using Spring Boot, this service provides essential CRUD (Create, Read, Update, Delete) operations along with the ability to retrieve to-do items based on their status.

## Features

- **Create**: Add new to-do items.
- **Read**: Retrieve all to-do items or fetch a specific item by ID.
- **Update**: Modify existing to-do items.
- **Delete**: Remove a to-do item by ID.
- **Fetch by Status**: Get to-do items filtered by their status.

## Technologies

- **Java 17**
- **Spring Boot**
- **Lombok**: For reducing boilerplate code.
- **Jakarta Persistence (JPA)**: For database interactions.
- **Mockito**: For unit testing.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven for dependency management
- An IDE such as IntelliJ IDEA or Eclipse

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/yourusername/todo-application.git
    cd todo-application
    ```

2. Build the project:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

### Configuration

By default, the application uses an embedded H2 database. For production, you need to configure your database settings in the `src/main/resources/application.yml` file.

Example configuration for PostgreSQL:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/todo_db
    username: your_username
    password: your_password
   ```

### API Endpoints

- GET /api/todos - Retrieve all to-do items.
- GET /api/todos/{id} - Retrieve a specific to-do item by ID.
- POST /api/todos - Create a new to-do item.
- PUT /api/todos/{id} - Update an existing to-do item.
- DELETE /api/todos/{id} - Delete a to-do item by ID.
- GET /api/todos/status/{status} - Retrieve to-do items by status.
