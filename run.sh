#!/bin/bash
# Expense Tracker - Launcher Script for Linux/Mac

cd "$(dirname "$0")"

JAR="target/expense-tracker-1.0.0-jar-with-dependencies.jar"

# Classpath separator differs when this script runs under Git Bash / Cygwin on Windows
case "$(uname -s)" in
    MINGW*|MSYS*|CYGWIN*) SEP=';' ;;
    *) SEP=':' ;;
esac

# Prefer the Maven fat JAR, otherwise use classes built by ./build.sh
if [ -f "$JAR" ]; then
    CP="$JAR"
elif [ -d out ]; then
    CP="out${SEP}gson-2.10.1.jar"
else
    echo "Nothing to run yet. Build first with 'mvn clean package' or './build.sh'"
    exit 1
fi

echo ""
echo "============================================================"
echo "  EXPENSE TRACKER - LAUNCHER"
echo "============================================================"
echo ""
echo "Choose application mode:"
echo ""
echo "1. Standalone Mode (Local only, no server needed)"
echo "2. Client-Server Mode (Connects to Expense Server)"
echo "3. Start Server Only"
echo "0. Exit"
echo ""
read -p "Enter your choice: " choice

case $choice in
    1)
        java -cp "$CP" com.expenses.client.ExpenseTrackerStandaloneApp
        ;;
    2)
        read -p "Server Host [localhost]: " host
        host=${host:-localhost}
        read -p "Server Port [5000]: " port
        port=${port:-5000}
        java -cp "$CP" com.expenses.client.ExpenseTrackerClientApp "$host" "$port"
        ;;
    3)
        read -p "Server Port [5000]: " port
        port=${port:-5000}
        echo "Starting server on port $port... Press Ctrl+C to stop"
        java -cp "$CP" com.expenses.server.ExpenseServer "$port"
        ;;
    0)
        echo "Goodbye!"
        ;;
    *)
        echo "Invalid choice"
        ;;
esac
