#!/bin/bash

echo "============================================"
echo "Prison-Secure - Docker Deployment"
echo "============================================"
echo ""

echo "[1/4] Stopping existing containers..."
docker-compose down

echo ""
echo "[2/4] Building Docker image..."
docker-compose build --no-cache

if [ $? -ne 0 ]; then
    echo "Docker build FAILED!"
    exit 1
fi

echo ""
echo "[3/4] Starting services (PostgreSQL + App)..."
docker-compose up -d

echo ""
echo "[4/4] Waiting for services to be ready..."
sleep 10

echo ""
echo "============================================"
echo "Services Status:"
echo "============================================"
docker-compose ps

echo ""
echo "============================================"
echo "Access Points:"
echo "============================================"
echo "Application:  http://localhost:8080"
echo "Health Check: http://localhost:8080/actuator/health"
echo "Swagger UI:   http://localhost:8080/swagger-ui.html"
echo ""
echo "Database: localhost:5432"
echo "  Database: prison_secure"
echo "  Username: postgres"
echo "  Password: postgres"
echo "============================================"
echo ""
echo "To view logs: docker-compose logs -f"
echo "To stop:      docker-compose down"
echo ""
