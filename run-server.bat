@echo off
title Expense Tracker - Server Mode
echo Starting Expense Tracker Server on port 5000...
echo Press Ctrl+C to stop the server
echo.
java -cp "target/expense-tracker-1.0.0-jar-with-dependencies.jar" com.expenses.server.ExpenseServer
pause