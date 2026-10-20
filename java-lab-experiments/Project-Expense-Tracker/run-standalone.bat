@echo off
title Expense Tracker - Standalone Mode
cd /d "%~dp0"
if not exist "out\com\expenses\client\ExpenseTrackerStandaloneApp.class" call build.bat
java -cp "out;lib/*" com.expenses.client.ExpenseTrackerStandaloneApp
pause