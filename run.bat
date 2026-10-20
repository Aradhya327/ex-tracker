@echo off
title Expense Tracker - Launcher
echo.
echo ============================================================
echo   EXPENSE TRACKER - LAUNCHER
echo ============================================================
echo.
echo Choose application mode:
echo.
echo 1. Standalone Mode (Local only, no server needed)
echo 2. Client-Server Mode (Connects to Expense Server)
echo 3. Start Server Only
echo 0. Exit
echo.
set /p choice="Enter your choice: "

if "%choice%"=="1" (
    call run-standalone.bat
) else if "%choice%"=="2" (
    call run-client.bat
) else if "%choice%"=="3" (
    call run-server.bat
) else if "%choice%"=="0" (
    echo Goodbye!
    timeout /t 2 >nul
) else (
    echo Invalid choice
    pause
)