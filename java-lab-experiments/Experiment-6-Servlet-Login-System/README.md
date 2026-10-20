# Experiment 6 - Servlet Login System with Sessions

A small web application where a user signs in, sees a protected page, and can
sign out. Access control is enforced by a filter, not by each servlet.

## Aim

Build a login system with Jakarta Servlets that keeps the user signed in across
requests using `HttpSession`, and blocks unauthenticated access with a filter.

## Concepts

- `HttpServlet` with `doGet` and `doPost`
- `@WebServlet` and `@WebFilter` annotations instead of `web.xml`
- `HttpSession` for state between requests
- `request.changeSessionId()` to prevent session fixation
- Filters - code that runs before a servlet
- Listeners - `ServletContextListener` and `HttpSessionListener`
- Forwarding to a JSP and rendering with EL and JSTL

## Files

| File | Role |
|------|------|
| `src/main/java/com/lab/auth/LoginServlet.java` | Shows the form (GET), checks credentials (POST) |
| `src/main/java/com/lab/auth/DashboardServlet.java` | The protected page |
| `src/main/java/com/lab/auth/LogoutServlet.java` | Destroys the session |
| `src/main/java/com/lab/auth/AuthFilter.java` | Blocks `/dashboard` and `/logout` without a session |
| `src/main/java/com/lab/auth/AppLifecycleListener.java` | Logs start/stop and counts open sessions |
| `src/main/java/com/lab/auth/UserStore.java` | In-memory user table |
| `src/main/webapp/index.jsp` | Login form |
| `src/main/webapp/WEB-INF/views/dashboard.jsp` | Protected view |

The dashboard JSP sits under `WEB-INF`, so it cannot be requested by URL. The
only way to reach it is through `DashboardServlet`.

## Flow

```
GET /login
   └── already signed in? -> redirect /dashboard
   └── otherwise          -> forward index.jsp

POST /login
   ├── credentials bad  -> set "error", forward index.jsp (keeps the username)
   └── credentials good -> create session, changeSessionId()
                           store username, redirect /dashboard

GET /dashboard
   └── AuthFilter: no session? -> redirect /login
   └── session present         -> DashboardServlet -> forward dashboard.jsp

GET /logout
   └── AuthFilter: no session? -> redirect /login
   └── session present         -> invalidate, redirect /login?logout=1
```

Redirect after a successful login means the browser address bar shows
`/dashboard`, and refreshing does not re-post the password.

## Build

```powershell
mvn clean package
```

Produces `target/experiment-6-servlet-login.war`.

## Deploy

Copy the war into Tomcat's `webapps` folder and start Tomcat:

```powershell
$env:CATALINA_HOME = "C:\path\to\apache-tomcat-10.1.x"
Copy-Item target\experiment-6-servlet-login.war "$env:CATALINA_HOME\webapps\"
& "$env:CATALINA_HOME\bin\startup.bat"
```

Then open:

```
http://localhost:8080/experiment-6-servlet-login/login
```

| Username | Password |
|----------|----------|
| `admin` | `admin123` |
| `student` | `student123` |

Requires Tomcat 10.1 or newer. Tomcat 9 uses `javax.servlet` and will not work.

## What to observe

- Open `/dashboard` in a fresh browser. `AuthFilter` redirects to the login
  form before `DashboardServlet` ever runs.
- The dashboard shows the session id. Log in again and it changes, because
  `changeSessionId()` issues a new one after the credentials check.
- Enter a wrong password and the username stays in the box. That is a forward,
  not a redirect - the request attributes survive.
- Watch the Tomcat console. `AppLifecycleListener` prints application start,
  and a session count that goes up on login and down on logout.
- Close the browser without logging out and the session survives until it times
  out - the cookie is what carries it, not the open tab.

## Design notes

Passwords in `UserStore` are plain text on purpose, so the experiment stays
about servlets and sessions. A real system stores a bcrypt or Argon2 hash and
never keeps the raw password. The same applies to the in-memory user table -
production would use a database.
