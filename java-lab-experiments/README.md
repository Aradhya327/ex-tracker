# Advanced Java Lab

Lab experiments for the Advanced Java course, plus the term project.


**Student:** `Prince Sharma`
**Enrollment:** `24EGJCC016`

---

## Contents

| # | Folder | Experiment | Concepts |
|---|--------|-----------|----------|
| 1 | [`Experiment-1-MVC-Calculator`](Experiment-1-MVC-Calculator) | Calculator using MVC architecture | Swing, `ActionListener`, model/view/controller separation |
| 2 | [`Experiment-2-JDBC-Database-Insert`](Experiment-2-JDBC-Database-Insert) | Insert a record into MySQL | JDBC, `DriverManager`, `PreparedStatement` |
| 3 | [`Experiment-3-JDBC-Student-Registration`](Experiment-3-JDBC-Student-Registration) | Student registration form with database | Swing forms, validation, JDBC, `PreparedStatement` |
| 4 | [`Experiment-4-Socket-Client-Server`](Experiment-4-Socket-Client-Server) | TCP client-server communication | `Socket`, `ServerSocket`, streams, try-with-resources |
| 5 | [`Experiment-5-RMI-Calculator`](Experiment-5-RMI-Calculator) | Remote method invocation | RMI, `Remote`, `UnicastRemoteObject`, `rmiregistry` |
| 6 | [`Experiment-6-Servlet-Login-System`](Experiment-6-Servlet-Login-System) | Login system with sessions | Servlets, `HttpSession`, filters, listeners, JSTL |
| 7 | [`Experiment-7-JSP-Student-Form`](Experiment-7-JSP-Student-Form) | Student form with JSP | JSP, EL, JSTL, request forwarding, server-side validation |
| P | [`Project-Expense-Tracker`](Project-Expense-Tracker) | Expense tracker (term project) | TCP sockets, object serialization, multi-threaded server, Gson, budgets, CSV/JSON export |

The term project is described under [Project](#project).

---

## Requirements

| Tool | Version | Needed by |
|------|---------|-----------|
| JDK | 17 or newer | everything |
| MySQL | 8.x | experiments 2 and 3 |
| Maven | 3.8 or newer | experiments 6 and 7 |
| Apache Tomcat | 10.1 or newer | experiments 6 and 7 |

Tomcat 10.1 is the first release that uses the `jakarta.*` namespace, which is what experiments 6 and 7 are written against. Tomcat 9 will not run them.

---

## Database setup

Experiments 2 and 3 both use a database called `college` with one table, `student`. Create it once:

```sql
CREATE DATABASE IF NOT EXISTS college;
USE college;

CREATE TABLE IF NOT EXISTS student (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    roll_number VARCHAR(20)  NOT NULL,
    branch      VARCHAR(50)  NOT NULL,
    gender      VARCHAR(10)  NOT NULL
);
```

Both programs default to `jdbc:mysql://localhost:3306/college` with user `root` and password `luffy`. Override any of these with environment variables rather than editing the source:

```powershell
$env:DB_PASSWORD = "your-password"
```

The JDBC driver (`mysql-connector-j-26.7.0.jar`) is committed in each experiment's `lib/` folder, so there is nothing extra to download.

---

## Running the experiments

### Experiment 1 - MVC calculator

```powershell
cd Experiment-1-MVC-Calculator
javac -d out src/*.java
java -cp out CalculatorApp
```

Enter two numbers, then press one of the five operation buttons.

### Experiment 2 - JDBC insert

```powershell
cd Experiment-2-JDBC-Database-Insert
javac -cp "lib/*" -d out src/App.java
java -cp "out;lib/*" App
```

Prints `Rows inserted: 1`. The `id` column is auto-increment, so it can be run repeatedly without a primary-key clash.

### Experiment 3 - Student registration form

```powershell
cd Experiment-3-JDBC-Student-Registration
javac -cp "lib/*" -d out src/StudentRegistration.java
java -cp "out;lib/*" StudentRegistration
```

Fill the form and press Submit. The row is written to `student` before the success dialog appears, so a database failure shows an error instead of a false confirmation.

### Experiment 4 - Socket client and server

Two terminals, server first:

```powershell
cd Experiment-4-Socket-Client-Server
javac -d out src/*.java

# terminal 1
java -cp out server

# terminal 2
java -cp out client
```

The server prints what the client sent and replies `Hello client`. Port 5000 must be free.

### Experiment 5 - RMI calculator

Three terminals, in this order:

```powershell
cd Experiment-5-RMI-Calculator
javac -d out src/*.java

# terminal 1 - the registry, leave it running
rmiregistry

# terminal 2 - the server
java -cp out server

# terminal 3 - the client
java -cp out client
```

The service is registered as `rmi://localhost/Calculator`. The name is case-sensitive.

### Experiment 6 - Servlet login system

```powershell
cd Experiment-6-Servlet-Login-System
mvn clean package
```

Copy `target/experiment-6-servlet-login.war` into Tomcat's `webapps/` folder and start Tomcat, then open:

```
http://localhost:8080/experiment-6-servlet-login/login
```

Test accounts: `admin / admin123` and `student / student123`.

What to try:

- Visit `/dashboard` without logging in. `AuthFilter` redirects to the login form.
- Log in, and the dashboard shows your session id and login time.
- Log out, and `/dashboard` is blocked again.
- Watch the Tomcat console: `AppLifecycleListener` prints application start/stop and a live count of open sessions.

### Experiment 7 - JSP student form

```powershell
cd Experiment-7-JSP-Student-Form
mvn clean package
```

Copy `target/experiment-7-jsp-student-form.war` into Tomcat's `webapps/` folder, start Tomcat, then open:

```
http://localhost:8080/experiment-7-jsp-student-form/register
```

Submit with fields blank to see per-field validation and the form repopulating itself, then submit valid values to reach the result page.

---

## Project

The term project is the **Expense Tracker** — a console-based Java application with client-server architecture.

[`Project-Expense-Tracker/`](Project-Expense-Tracker) — see its [`README.md`](Project-Expense-Tracker/README.md) for full documentation.

**Concepts:** TCP sockets (`ServerSocket` / `Socket`), Java object serialization, multi-threaded server (thread pool), JSON persistence (Gson), MVC-style service layer, budgets with real-time alerts, CSV/JSON export.

```powershell
cd Project-Expense-Tracker
mvn clean package
java -jar target/expense-tracker-1.0.0-jar-with-dependencies.jar
```

---

## Repository layout

```
.
├── Experiment-1-MVC-Calculator/
│   └── src/                    4 files, default package
├── Experiment-2-JDBC-Database-Insert/
│   ├── src/                    App.java
│   └── lib/                    MySQL JDBC driver
├── Experiment-3-JDBC-Student-Registration/
│   ├── src/                    StudentRegistration.java
│   └── lib/                    MySQL JDBC driver
├── Experiment-4-Socket-Client-Server/
│   └── src/                    client.java, server.java
├── Experiment-5-RMI-Calculator/
│   └── src/                    interface, implementation, server, client
├── Experiment-6-Servlet-Login-System/
│   ├── pom.xml                 Maven war build
│   └── src/main/
│       ├── java/com/lab/auth/  servlets, filter, listener, user store
│       └── webapp/             index.jsp, WEB-INF/views/dashboard.jsp
├── Experiment-7-JSP-Student-Form/
│   ├── pom.xml                 Maven war build
│   └── src/main/
│       ├── java/com/lab/student/  servlet and Student bean
│       └── webapp/             student.jsp, result.jsp
├── Project-Expense-Tracker/
│   ├── pom.xml                 Maven jar build (fat jar with shade)
│   ├── run.bat / run.sh        Launchers for Windows / Linux
│   └── src/main/java/com/expenses/
│       ├── model/              Expense, Budget, MonthlySummary, Request, Response
│       ├── service/            ExpenseService (business logic + JSON persistence)
│       ├── server/             ExpenseServer (multi-threaded TCP server)
│       ├── client/             ExpenseClient, standalone app, client app, launcher
│       └── util/               JsonUtil (Gson), ConsoleUtil (UI helpers)
└── README.md
```

Experiments 1 to 5 are plain `javac` builds with no external build tool, so each one can be compiled and run from the command line exactly as shown above. Experiments 6 and 7 use Maven because servlets need a war file to deploy. The term project uses Maven to produce a self-contained executable jar.
