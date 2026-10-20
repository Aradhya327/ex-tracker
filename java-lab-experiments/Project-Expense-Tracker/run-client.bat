@echo off
title Expense Tracker - Client Mode
cd /d "%~dp0"
if not exist "expense-tracker.jar" call build.bat
echo Connecting to server at localhost:5000
echo (start the server first with run-server.bat or in another window)
echo.
java -cp "expense-tracker.jar;lib/*" com.expenses.client.ExpenseTrackerClientApp
pause