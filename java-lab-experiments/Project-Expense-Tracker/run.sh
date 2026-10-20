#!/bin/bash
# Expense Tracker - Launcher for Linux/Mac
cd "$(dirname "$0")"

if [ ! -f "expense-tracker.jar" ]; then
    echo "First run - building the project..."
    ./build.sh
fi

while true; do
    echo ""
    echo "============================================================"
    echo "  EXPENSE TRACKER - LAUNCHER"
    echo "============================================================"
    echo ""
    echo "1. Standalone Mode (local only, no server needed)"
    echo "2. Client Mode (connect to a running server)"
    echo "3. Start Server"
    echo "0. Exit"
    echo ""
    read -r -p "Enter your choice: " choice

    case $choice in
        1) java -cp "expense-tracker.jar:lib/*" com.expenses.client.ExpenseTrackerStandaloneApp ;;
        2) java -cp "expense-tracker.jar:lib/*" com.expenses.client.ExpenseTrackerClientApp ;;
        3) java -cp "expense-tracker.jar:lib/*" com.expenses.server.ExpenseServer ;;
        0) exit 0 ;;
        *) echo "Invalid choice." ;;
    esac
done