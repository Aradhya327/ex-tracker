#!/bin/bash
# Expense Tracker - build without Maven (uses the bundled Gson jar)
# Output: out/*.class, which run.sh uses when no fat JAR is present.

set -e
cd "$(dirname "$0")"

if ! command -v javac >/dev/null 2>&1; then
    echo "javac not found. Please install a JDK 17 or newer."
    exit 1
fi

mkdir -p out
find src/main/java -name '*.java' > sources.txt
javac -encoding UTF-8 -cp gson-2.10.1.jar -d out @sources.txt
rm -f sources.txt

echo "Build complete. Run ./run.sh"
