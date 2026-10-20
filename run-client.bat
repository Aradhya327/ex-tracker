@echo off
cd /d "%~dp0"
title Expense Tracker - Client Mode

set "CP="
if exist "target\expense-tracker-1.0.0-jar-with-dependencies.jar" set "CP=target\expense-tracker-1.0.0-jar-with-dependencies.jar"
if not defined CP if exist "out" set "CP=out;gson-2.10.1.jar"
if not defined CP (
    echo Nothing to run yet. Build first with "mvn clean package" or build.bat
    pause
    exit /b 1
)

echo Starting Expense Tracker Client...
echo Connecting to localhost:5000
echo.
java -cp "%CP%" com.expenses.client.ExpenseTrackerClientApp
pause
