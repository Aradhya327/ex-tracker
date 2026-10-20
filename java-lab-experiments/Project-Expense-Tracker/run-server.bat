@echo off
title Expense Tracker - Server Mode
cd /d "%~dp0"
if not exist "expense-tracker.jar" call build.bat
echo Starting server on port 5000...
echo Press Ctrl+C to stop.
echo.
java -cp "expense-tracker.jar;lib/*" com.expenses.server.ExpenseServer
pause