@echo off
echo ============================================
echo Prison-Secure - Local Development
echo (Running outside Docker)
echo ============================================
echo.

echo Checking PostgreSQL on localhost:5432...
timeout /t 2 >NUL

echo.
echo Building application...
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Build FAILED!
    pause
    exit /b 1
)

echo.
echo Starting application with LOCAL profile...
echo.
echo Application: http://localhost:8080
echo.
echo NOTE: PostgreSQL must be running on localhost:5432
echo   Database: prison_secure  
echo   Username: postgres
echo   Password: postgres
echo.
echo Press Ctrl+C to stop
echo ============================================
echo.

java -jar target\prison-secure-0.0.1-SNAPSHOT.jar --spring.profiles.active=local

pause
