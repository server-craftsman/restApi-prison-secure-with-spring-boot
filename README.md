# Prison Secure - Hệ thống Quản lý Nhà tù

## Quick Start

```bash
# Build & Run
mvn clean package -DskipTests
docker-compose up --build -d

# Access
Swagger UI: http://localhost:8080/swagger-ui/index.html
API Docs: http://localhost:8080/v3/api-docs
```

## Tech Stack

- **Backend**: Spring Boot 3.2.5, Java 17
- **Database**: PostgreSQL 15
- **ORM**: Hibernate, Flyway
- **API Docs**: Swagger/OpenAPI 3
- **Container**: Docker, Docker Compose

## Project Structure

```
src/main/java/vn/gov/prison/secure/
├── domain/              # Entities, Value Objects, Domain Services
├── application/         # Use Cases, DTOs, Mappers
├── infrastructure/      # JPA, Repositories, External Services
└── presentation/        # REST Controllers
```

## Documentation

- **[API_DOCUMENTATION.md](API_DOCUMENTATION.md)** 
- **Swagger UI**
- **Database Schema**

## License
© 2025 Copyright by Đan Huy | fb/dan.huy.servercraftsman
