#!/bin/bash
# Expense Tracker - Launcher Script for Linux/Mac

JAR="target/expense-tracker-1.0.0-jar-with-dependencies.jar"

if [ ! -f "$JAR" ]; then
    echo "JAR not found. Please build the project first: mvn clean package"
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
        java -cp "$JAR" com.expenses.client.ExpenseTrackerStandaloneApp
        ;;
    2)
        read -p "Server Host [localhost]: " host
        host=${host:-localhost}
        read -p "Server Port [5000]: " port
        port=${port:-5000}
        java -cp "$JAR" com.expenses.client.ExpenseTrackerClientApp "$host" "$port"
        ;;
    3)
        read -p "Server Port [5000]: " port
        port=${port:-5000}
        echo "Starting server on port $port... Press Ctrl+C to stop"
        java -cp "$JAR" com.expenses.server.ExpenseServer "$port"
        ;;
    0)
        echo "Goodbye!"
        ;;
    *)
        echo "Invalid choice"
        ;;
esac