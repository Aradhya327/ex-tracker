@echo off
cd /d "%~dp0"
title Expense Tracker - Server Mode

set "CP="
if exist "target\expense-tracker-1.0.0-jar-with-dependencies.jar" set "CP=target\expense-tracker-1.0.0-jar-with-dependencies.jar"
if not defined CP if exist "out" set "CP=out;gson-2.10.1.jar"
if not defined CP (
    echo Nothing to run yet. Build first with "mvn clean package" or build.bat
    pause
    exit /b 1
)

echo Starting Expense Tracker Server on port 5000...
echo Press Ctrl+C to stop the server
echo.
java -cp "%CP%" com.expenses.server.ExpenseServer
pause
