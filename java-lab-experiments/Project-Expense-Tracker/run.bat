@echo off
title Expense Tracker - Launcher
cd /d "%~dp0"

if not exist "out\com\expenses\client\ExpenseTrackerLauncher.class" (
    echo First run - building the project...
    call build.bat
    if errorlevel 1 exit /b 1
)

:menu
cls
echo.
echo ============================================================
echo   EXPENSE TRACKER - LAUNCHER
echo ============================================================
echo.
echo 1. Standalone Mode (local only, no server needed)
echo 2. Client Mode (connect to a running server)
echo 3. Start Server
echo 0. Exit
echo.
set /p choice="Enter your choice: "

if "%choice%"=="1" (
    java -cp "out;lib/*" com.expenses.client.ExpenseTrackerStandaloneApp
    pause
    goto menu
)
if "%choice%"=="2" (
    java -cp "out;lib/*" com.expenses.client.ExpenseTrackerClientApp
    pause
    goto menu
)
if "%choice%"=="3" (
    java -cp "out;lib/*" com.expenses.server.ExpenseServer
    pause
    goto menu
)
if "%choice%"=="0" exit /b 0

echo Invalid choice.
timeout /t 1 >nul
goto menu