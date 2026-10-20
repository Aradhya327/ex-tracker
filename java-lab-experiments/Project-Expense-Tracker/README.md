# Expense Tracker - Console Based Java Application

A feature-rich console-based expense tracker application with **client-server architecture** built in Java 17. Supports both standalone mode (local only) and distributed client-server mode with socket communication.

## 🚀 Features

### Core Expense Management
- ✅ **Add/Edit/Delete** expenses with title, amount, category, date, description, payment method
- ✅ **8 Categories**: Food & Dining, Transportation, Shopping, Entertainment, Utilities, Healthcare, Education, Other
- ✅ **Multiple Payment Methods**: Cash, Card, UPI, Bank Transfer, etc.
- ✅ **View All Expenses** with formatted table display
- ✅ **Filter Expenses** by category, date range, month, payment method

### Budget Management
- ✅ **Set Monthly Budgets** per category
- ✅ **Budget Tracking** with real-time spending updates
- ✅ **Visual Alerts**: Near limit (80%+) and Over budget warnings
- ✅ **View Budgets** by month or all time

### Reports & Analytics
- ✅ **Monthly Summary** with totals and transaction counts
- ✅ **Category-wise Breakdown** with percentages
- ✅ **Payment Method Analysis**
- ✅ **Budget vs Actual Comparison**

### Data Export
- ✅ **Export to CSV** (spreadsheet compatible)
- ✅ **Export to JSON** (structured data)

### Architecture
- 🔌 **Client-Server Mode**: Socket-based communication (TCP)
- 💾 **Standalone Mode**: Runs completely offline
- 📁 **JSON File Persistence** (auto-save on every operation)
- 🧵 **Multi-threaded Server** handles multiple clients concurrently

---

## 📋 Requirements

- **Java 17+** (JDK 17 or higher)
- **Maven 3.6+** (for building)
- **Git** (for version control)

---

## 🛠️ Building the Project

```bash
# Clone the repository
git clone <your-github-repo-url>
cd expense-tracker

# Build with Maven
mvn clean package

# This creates:
# - target/expense-tracker-1.0.0.jar (main jar)
# - target/expense-tracker-1.0.0-jar-with-dependencies.jar (fat jar with all dependencies)
```

---

## 🏃 Running the Application

### Option 1: Using the Fat JAR (Recommended)
```bash
# Run the launcher (choose mode interactively)
java -jar target/expense-tracker-1.0.0-jar-with-dependencies.jar

# Or run specific modes directly:
# Standalone mode
java -cp target/expense-tracker-1.0.0-jar-with-dependencies.jar com.expenses.client.ExpenseTrackerStandaloneApp

# Client mode (connects to localhost:5000)
java -cp target/expense-tracker-1.0.0-jar-with-dependencies.jar com.expenses.client.ExpenseTrackerClientApp

# Client mode with custom host/port
java -cp target/expense-tracker-1.0.0-jar-with-dependencies.jar com.expenses.client.ExpenseTrackerClientApp localhost 5000

# Server mode
java -cp target/expense-tracker-1.0.0-jar-with-dependencies.jar com.expenses.server.ExpenseServer

# Server mode with custom port
java -cp target/expense-tracker-1.0.0-jar-with-dependencies.jar com.expenses.server.ExpenseServer 8080
```

### Option 2: Using Maven Exec Plugin
```bash
# Run launcher
mvn exec:java -Dexec.mainClass="com.expenses.client.ExpenseTrackerLauncher"

# Run standalone
mvn exec:java -Dexec.mainClass="com.expenses.client.ExpenseTrackerStandaloneApp"

# Run server
mvn exec:java -Dexec.mainClass="com.expenses.server.ExpenseServer"
```

---

## 📖 Usage Guide

### Main Menu (Standalone Mode)
```
============================================================
  EXPENSE TRACKER - MAIN MENU
============================================================
1.  Add Expense
2.  View All Expenses
3.  View Expense by ID
4.  Update Expense
5.  Delete Expense
6.  Filter Expenses
7.  Manage Budgets
8.  View Reports
9.  Export Data
10. Save & Sync Data
0.  Exit
```

### Adding an Expense
1. Select **Option 1** from main menu
2. Enter details:
   - **Title**: "Lunch at cafeteria"
   - **Amount**: `150.00`
   - **Category**: `FOOD` (choose from list)
   - **Date**: `20/10/2026` (dd/MM/yyyy) or press Enter for today
   - **Description**: Optional notes
   - **Payment Method**: `Cash` / `Card` / `UPI` / etc.

### Setting a Budget
1. Select **Option 7** → **Option 1** (Manage Budgets → Add Budget)
2. Choose category (e.g., `FOOD`)
3. Select month (e.g., `10/2026` for October 2026)
4. Set limit (e.g., `5000` for ₹5000/month)

### Viewing Reports
1. Select **Option 8** → **Option 1** (Monthly Summary)
2. Enter month (e.g., `10/2026`)
3. View:
   - Total expenses
   - Category breakdown with percentages
   - Payment method distribution
   - Budget status with alerts

### Exporting Data
1. Select **Option 9** → Choose **CSV** or **JSON**
2. Files saved as `expenses_export_YYYY-MM-DD.csv/.json`

---

## 🏗️ Project Structure

```
expense-tracker/
├── pom.xml                          # Maven build configuration
├── README.md                        # This file
├── data/                            # JSON data files (auto-created)
│   ├── expenses.json                # Stored expenses
│   └── budgets.json                 # Stored budgets
└── src/
    └── main/
        └── java/
            └── com/
                └── expenses/
                    ├── model/       # Data models
                    │   ├── Expense.java
                    │   ├── Budget.java
                    │   ├── Request.java
                    │   └── Response.java
                    ├── service/     # Business logic
                    │   └── ExpenseService.java
                    ├── server/      # Server implementation
                    │   └── ExpenseServer.java
                    ├── client/      # Client applications
                    │   ├── ExpenseClient.java
                    │   ├── ExpenseTrackerClientApp.java
                    │   ├── ExpenseTrackerStandaloneApp.java
                    │   └── ExpenseTrackerLauncher.java
                    └── util/        # Utilities
                        ├── JsonUtil.java
                        └── ConsoleUtil.java
```

---

## 🔧 Technical Details

### Communication Protocol
- **Transport**: TCP Sockets (Java Object Streams)
- **Serialization**: Java Native Serialization
- **Request/Response Pattern**: Synchronous request-reply
- **Thread Model**: Server uses thread pool (cached) for concurrent clients

### Data Models

#### Expense
```java
- id: int (auto-generated)
- title: String
- amount: double
- category: Enum (FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, UTILITIES, HEALTHCARE, EDUCATION, OTHER)
- date: LocalDate
- description: String (optional)
- paymentMethod: String
```

#### Budget
```java
- id: int (auto-generated)
- category: Expense.Category
- month: YearMonth
- limitAmount: double
- spentAmount: double (auto-updated)
```

### Persistence
- **Format**: JSON (Gson library)
- **Files**: `data/expenses.json`, `data/budgets.json`
- **Auto-save**: After every write operation
- **Custom Adapters**: LocalDate, YearMonth serialization

---

## 🧪 Testing

```bash
# Run unit tests
mvn test

# Run with verbose output
mvn test -X
```

---

## 📦 Creating Distribution

```bash
# Create distribution package
mvn clean package

# The fat JAR is at:
target/expense-tracker-1.0.0-jar-with-dependencies.jar

# Copy this single JAR to any machine with Java 17+ to run
```

---

## 🐛 Troubleshooting

### "Could not connect to server"
- Ensure server is running: `java -jar ... ExpenseServer`
- Check firewall allows port 5000 (or configured port)
- Verify host/port in client settings

### "ClassNotFoundException: com.google.gson.Gson"
- Use the fat JAR (`-jar-with-dependencies.jar`)
- Or ensure Gson is in classpath

### "Unsupported class file major version 61"
- Requires Java 17+ (major version 61)
- Check: `java -version`

### Data not persisting
- Check `data/` folder write permissions
- Files created automatically on first run

---

## 👨‍💻 Development

### Adding New Features
1. **Model**: Add to `model/` package
2. **Service**: Add logic to `ExpenseService.java`
3. **Protocol**: Add Request/Response types in `model/Request.java`
4. **Server**: Handle in `ExpenseServer.ClientHandler.handleRequest()`
5. **Client**: Add methods to `ExpenseClient.java`
6. **UI**: Add menu options in `ExpenseTrackerStandaloneApp.java` / `ExpenseTrackerClientApp.java`

### Code Style
- Java 17 features (records, pattern matching, text blocks)
- Proper exception handling
- Resource management (try-with-resources)
- Thread-safe collections (ConcurrentHashMap)

---

## 📄 License

This project is created for educational purposes as part of 3rd Year Java Lab Evaluation.

---

## 👤 Author

**Student Name**: [Your Name]  
**Roll Number**: [Your Roll Number]  
**Course**: [Your Course/Branch]  
**Institution**: [Your College/University]  

---

## 🙏 Acknowledgments

- **Gson Library** by Google for JSON handling
- **Java Standard Library** for networking, collections, time API
- **Maven** for build management

---

**Submission Checklist:**
- [ ] All lab experiments uploaded
- [ ] Complete Java project (Expense Tracker) uploaded
- [ ] Properly organized folders
- [ ] README.md included
- [ ] GitHub link submitted to: https://forms.gle/QPc4Ua1qK3FwrqTg9
- [ ] Ready for Wednesday Evaluation