@echo off
cd /d "%~dp0"
title Expense Tracker - Build

where javac >nul 2>nul
if errorlevel 1 (
    echo javac not found. Please install a JDK 17 or newer.
    pause
    exit /b 1
)

if not exist out mkdir out
dir /s /b src\main\java\*.java > sources.txt
javac -encoding UTF-8 -cp gson-2.10.1.jar -d out @sources.txt
set BUILD_RESULT=%errorlevel%
del sources.txt

if not "%BUILD_RESULT%"=="0" (
    echo.
    echo Build FAILED.
    pause
    exit /b 1
)

echo.
echo Build complete. Run run.bat
pause
