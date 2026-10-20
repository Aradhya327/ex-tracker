@echo off
title Expense Tracker - Client Mode
echo Starting Expense Tracker Client...
echo Connecting to localhost:5000
echo.
java -cp "target/expense-tracker-1.0.0-jar-with-dependencies.jar" com.expenses.client.ExpenseTrackerClientApp
pause