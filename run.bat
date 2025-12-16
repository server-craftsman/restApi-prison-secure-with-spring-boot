@echo off
echo ============================================
echo Prison-Secure Application Launcher
echo ============================================
echo.

echo [1/3] Checking for running Java processes...
tasklist /FI "IMAGENAME eq java.exe" 2>NUL | find /I /N "java.exe">NUL
if "%ERRORLEVEL%"=="0" (
    echo Found running Java process. Killing...
    taskkill /F /IM java.exe >NUL 2>&1
    timeout /t 2 >NUL
)

echo [2/3] Building application...
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Build FAILED!
    pause
    exit /b 1
)

echo.
echo [3/3] Starting Prison-Secure Application...
echo.
echo Application will start on http://localhost:8080
echo Press Ctrl+C to stop the application
echo.
echo ============================================
echo.

java -jar target\prison-secure-0.0.1-SNAPSHOT.jar

pause
