# Experiment 2 - Insert a Record into MySQL using JDBC

Connects to a MySQL database from plain Java and inserts one row using a
prepared statement.

## Aim

Write a JDBC program that connects to a MySQL database and inserts a record
into a table.

## Concepts

- JDBC driver loading and `DriverManager.getConnection`
- `PreparedStatement` with `?` placeholders
- `executeUpdate` and its return value (the number of affected rows)
- `try-with-resources` so the connection and statement always close
- Reading configuration from environment variables instead of hardcoding it

## Files

| File | Purpose |
|------|---------|
| `App.java` | Connects, inserts one student row, prints the row count |
| `lib/mysql-connector-j-26.7.0.jar` | MySQL JDBC driver |

## Database

Create the schema once before running anything:

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

## Run

```powershell
javac -cp "lib/*" -d out src/App.java
java -cp "out;lib/*" App
```

Expected output:

```
Connected to jdbc:mysql://localhost:3306/college
Rows inserted: 1
```

## What to observe

- The `id` column is left out of the `INSERT` because it is `AUTO_INCREMENT`,
  so MySQL assigns it. A second run inserts a second row instead of failing on
  a duplicate primary key.
- Values are bound with `setString`, not concatenated into the SQL string. That
  is what keeps the statement safe from SQL injection.
- If the password is different on your machine, set it once instead of editing
  the file:

  ```powershell
  $env:DB_PASSWORD = "your-password"
  ```

  `DB_URL`, `DB_USER` and `DB_PASSWORD` are all honoured.

## Common errors

| Message | Cause |
|---------|-------|
| `No suitable driver found` | The jar is missing from the `-cp` argument |
| `Unknown database 'college'` | The `CREATE DATABASE` step was skipped |
| `Access denied for user 'root'` | Wrong password - set `DB_PASSWORD` |
| `Table 'college.student' doesn't exist` | The `CREATE TABLE` step was skipped |
