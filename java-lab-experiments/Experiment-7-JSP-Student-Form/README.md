# Experiment 7 - Student Form with JSP

A student information form built with JSP. The servlet validates the submitted
values and forwards to a result page, or back to the form with per-field errors.

## Aim

Create a JSP form that accepts student details, validate the input on the
server, and display the result on a second JSP page.

## Concepts

- JSP as a view - HTML with `${}` expressions rather than Java code
- Expression Language (EL) reading request attributes
- JSTL `c:forEach` to build a dropdown from a list, `c:out` to escape output
- `RequestDispatcher.forward` to move between servlet and JSP
- Server-side validation with a per-field error map
- Forwarding back to the form so typed values are not lost

## Files

| File | Role |
|------|------|
| `src/main/java/com/lab/student/StudentServlet.java` | Shows the form, validates the POST, forwards |
| `src/main/java/com/lab/student/Student.java` | Bean with getters so EL can read `${student.name}` |
| `src/main/webapp/student.jsp` | The form, with inline error messages |
| `src/main/webapp/result.jsp` | Confirmation page |

## Flow

```
GET  /register
   └── forward student.jsp (courses list passed as a request attribute)

POST /register
   ├── invalid -> put errors map + typed values, forward student.jsp
   └── valid   -> put Student bean, forward result.jsp
```

A **forward** keeps everything on one request, which is why the error messages
and the typed values are still available to the JSP. A redirect would discard
them.

## Validation rules

| Field | Rule |
|-------|------|
| Name | Required, at least 3 characters |
| Email | Required, must match `something@something.tld` |
| Course | Must be one of the five courses offered |
| Age | Required, a whole number between 15 and 100 |

## Build

```powershell
mvn clean package
```

Produces `target/experiment-7-jsp-student-form.war`.

## Deploy

```powershell
$env:CATALINA_HOME = "C:\path\to\apache-tomcat-10.1.x"
Copy-Item target\experiment-7-jsp-student-form.war "$env:CATALINA_HOME\webapps\"
& "$env:CATALINA_HOME\bin\startup.bat"
```

Then open:

```
http://localhost:8080/experiment-7-jsp-student-form/register
```

Requires Tomcat 10.1 or newer.

## What to observe

- Submit with everything blank. All four errors show at once, each next to its
  own field, and nothing you typed is lost.
- The `age` field accepts text in the HTML, but the servlet rejects anything
  that is not a number - client-side `type="number"` is a convenience, not a
  guarantee.
- Try entering `<script>alert(1)</script>` as the name. The result page shows it
  as text, because `c:out` escapes HTML. A raw `${student.name}` would have
  executed it.
- The course dropdown is built from `COURSES` in the servlet, so adding a course
  is a one-line change and the JSP needs no editing.
- After a successful submit the URL stays `/register` - that is the forward.
  Refreshing re-posts the form.

## Note on the bean

`Student` is a plain class with getters rather than a Java `record`. EL resolves
properties through `getName()`, and a record's accessor is `name()`, which EL
does not treat as a property.
