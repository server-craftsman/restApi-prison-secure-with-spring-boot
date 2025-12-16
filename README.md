# Prison-Secure System

Enterprise Prison Management System built with Clean Architecture, SOLID principles, and containerized with Docker.

## 🚀 Quick Start

### Using Docker (Recommended)

```bash
# Deploy everything (App + Database)
.\docker-deploy.bat

# Access application
http://localhost:8080/swagger-ui.html
```

### Manual Build

```bash
# Build with Maven
mvn clean package -DskipTests

# Run
java -jar target/prison-secure-0.0.1-SNAPSHOT.jar
```

## 📚 Documentation

- **[Docker Guide](C:\Users\Huy IT\.gemini\antigravity\brain\200cbe68-4c34-4c44-a562-e61d6812ddbf\DOCKER_GUIDE.md)** - Complete Docker deployment
- **[Quick Start](C:\Users\Huy IT\.gemini\antigravity\brain\200cbe68-4c34-4c44-a562-e61d6812ddbf\QUICK_START.md)** - Build and run guide
- **[Walkthrough](C:\Users\Huy IT\.gemini\antigravity\brain\200cbe68-4c34-4c44-a562-e61d6812ddbf\walkthrough.md)** - Implementation details

## 🏗️ Architecture

- **Clean Architecture** with 4 layers (Domain, Application, Infrastructure, Presentation)
- **SOLID Principles** throughout
- **10+ Design Patterns** implemented
- **Multi-modal Biometric** identification (FBI/NIST compliant)
- **RESTful API** with Swagger documentation
- **Containerized** with Docker for easy deployment

## 🔒 Security

- JWT authentication
- Method-level authorization
- BCrypt password encoding
- Role-based access control

## 🐳 Docker Deployment

```bash
# Start services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 📊 API Endpoints

- `POST /api/v1/prisoners` - Register prisoner
- `GET /api/v1/prisoners` - Search prisoners
- `POST /api/v1/prisoners/{id}/biometric` - Capture biometric
- `POST /api/v1/prisoners/{id}/verify` - Verify identity

**Default Credentials**: admin / admin123

## 🛠️ Tech Stack

- Java 17
- Spring Boot 4.0
- PostgreSQL 15
- Docker & Docker Compose
- Flyway (DB migrations)
- JWT & Spring Security
- OpenAPI/Swagger

## 📝 License

Enterprise Prison Management System
