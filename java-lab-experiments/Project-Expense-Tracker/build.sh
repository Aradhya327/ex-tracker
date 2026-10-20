#!/bin/bash
# Build the Expense Tracker without Maven (uses the bundled Gson jar in lib/)
set -e
cd "$(dirname "$0")"

echo "Building Expense Tracker..."
rm -rf out
mkdir -p out

javac -encoding UTF-8 -d out -sourcepath src/main/java -cp "lib/*" \
    src/main/java/com/expenses/client/ExpenseTrackerLauncher.java

echo
echo "Build complete. Classes are in out/"
echo "Run the app with:  ./run.sh"