# Experiment 3 - Student Registration Form with Database

A Swing form that collects a student's details, validates every field, and
writes the record to MySQL.

## Aim

Design a registration form with Swing and store the submitted data in a MySQL
table using JDBC.

## Concepts

- Building a form with `JLabel`, `JTextField`, `JRadioButton`, `JCheckBox`
- Grouping radio buttons with `ButtonGroup` so only one stays selected
- Validating input before touching the database
- `JOptionPane` for user feedback
- `PreparedStatement` insert with `try-with-resources`

## Files

| File | Purpose |
|------|---------|
| `src/StudentRegistration.java` | The form, its validation, and the insert |
| `lib/mysql-connector-j-26.7.0.jar` | MySQL JDBC driver |

## Fields and rules

| Field | Control | Rule |
|-------|---------|------|
| Student name | text field | Required |
| Roll number | text field | Required |
| Gender | radio buttons | One must be selected |
| Branch | text field | Required |
| Terms | check box | Must be ticked |

Reset clears every field and both selections.

## Flow of a submission

```
Submit pressed
   └── validate each field in order
         ├── failure -> JOptionPane message, stop, nothing written
         └── success -> saveToDatabase(...)
                          ├── row inserted  -> "Registration Successful" dialog
                          └── SQLException  -> "Database Error: ..." dialog
```

The success dialog is shown only after the insert returns, so a database
failure can never be reported to the user as a success.

## Database

Same schema as Experiment 2 - run its `CREATE TABLE` script first if you have
not already:

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
javac -cp "lib/*" -d out src/StudentRegistration.java
java -cp "out;lib/*" StudentRegistration
```

## What to observe

- Pressing Submit with a blank name stops at the first problem and reports it,
  rather than writing a half-filled row.
- The connection opens inside the submit handler rather than when the window
  opens, so the form still appears even if MySQL is not running - the error
  surfaces only when you try to save.
- Credentials can be overridden with `DB_URL`, `DB_USER` and `DB_PASSWORD`
  environment variables.
