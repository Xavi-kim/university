@echo off
cd C:\jakarta\university

echo ========================================
echo University Management System - BUILD
echo ========================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-24
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java Home: %JAVA_HOME%
echo.
java -version
echo.

echo ========================================
echo Cleaning previous build...
echo ========================================
call mvnw.cmd clean

echo.
echo ========================================
echo Compiling project...
echo ========================================
call mvnw.cmd compile -DskipTests

echo.
echo ========================================
echo Packaging JAR...
echo ========================================
call mvnw.cmd package -DskipTests

echo.
echo ========================================
echo Build completed!
echo ========================================
echo.
echo JAR file: target\university-1.0-SNAPSHOT.jar
echo.

pause

