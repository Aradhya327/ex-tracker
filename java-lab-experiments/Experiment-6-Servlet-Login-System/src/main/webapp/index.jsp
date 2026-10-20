<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - Experiment 6</title>
    <style>
        :root {
            --ink: #1f2933;
            --muted: #616e7c;
            --line: #d3dce6;
            --accent: #2563eb;
            --danger: #b42318;
            --ok: #067647;
            --bg: #f5f7fa;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            min-height: 100vh;
            display: grid;
            place-items: center;
            background: var(--bg);
            color: var(--ink);
            font: 15px/1.5 system-ui, -apple-system, "Segoe UI", sans-serif;
        }

        .card {
            width: min(400px, calc(100vw - 32px));
            padding: 32px;
            background: #fff;
            border: 1px solid var(--line);
            border-radius: 12px;
            box-shadow: 0 1px 2px rgb(16 24 40 / 6%), 0 8px 24px rgb(16 24 40 / 6%);
        }

        h1 { margin: 0 0 4px; font-size: 22px; }

        .sub { margin: 0 0 24px; color: var(--muted); font-size: 14px; }

        label {
            display: block;
            margin-bottom: 6px;
            font-size: 13px;
            font-weight: 600;
        }

        input {
            width: 100%;
            padding: 10px 12px;
            margin-bottom: 16px;
            font: inherit;
            color: inherit;
            background: #fff;
            border: 1px solid var(--line);
            border-radius: 8px;
        }

        input:focus {
            outline: 2px solid var(--accent);
            outline-offset: 1px;
            border-color: transparent;
        }

        button {
            width: 100%;
            padding: 11px;
            font: inherit;
            font-weight: 600;
            color: #fff;
            background: var(--accent);
            border: 0;
            border-radius: 8px;
            cursor: pointer;
        }

        button:hover { background: #1d4ed8; }

        .msg {
            margin: 0 0 16px;
            padding: 10px 12px;
            font-size: 14px;
            border-radius: 8px;
        }

        .msg:empty { display: none; }

        .msg.error {
            color: var(--danger);
            background: #fef3f2;
            border: 1px solid #fecdca;
        }

        .msg.ok {
            color: var(--ok);
            background: #ecfdf3;
            border: 1px solid #abefc6;
        }

        .hint {
            margin: 20px 0 0;
            padding-top: 16px;
            border-top: 1px solid var(--line);
            color: var(--muted);
            font-size: 13px;
        }

        code {
            padding: 1px 5px;
            background: var(--bg);
            border-radius: 4px;
            font-size: 12px;
        }
    </style>
</head>
<body>

<main class="card">

    <h1>Student Login</h1>
    <p class="sub">Experiment 6 &mdash; Servlet session management</p>

    <%-- Both paragraphs render only when they have content. --%>
    <p class="msg error">${error}</p>
    <p class="msg ok">${param.logout == '1' ? 'You have been logged out.' : ''}</p>

    <form action="login" method="post">

        <label for="username">Username</label>
        <input type="text" id="username" name="username"
               value="<c:out value='${username}'/>"
               autocomplete="username" required autofocus>

        <label for="password">Password</label>
        <input type="password" id="password" name="password"
               autocomplete="current-password" required>

        <button type="submit">Sign in</button>

    </form>

    <p class="hint">
        Test accounts: <code>admin / admin123</code> or <code>student / student123</code>
    </p>

</main>

</body>
</html>
