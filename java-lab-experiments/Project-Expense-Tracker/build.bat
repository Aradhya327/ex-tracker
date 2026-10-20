@echo off
rem Build the Expense Tracker without Maven (uses the bundled Gson jar in lib\)
setlocal
cd /d "%~dp0"

echo Building Expense Tracker...
if exist out rmdir /s /q out
mkdir out

javac -encoding UTF-8 -d out -sourcepath "src\main\java" -cp "lib\*" "src\main\java\com\expenses\client\ExpenseTrackerLauncher.java"
if errorlevel 1 (
    echo.
    echo BUILD FAILED. Make sure JDK 17 or newer is installed and on PATH.
    pause
    exit /b 1
)

echo.
echo Build complete. Classes are in out\
echo Run the app with:  run.bat
endlocal