<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Dashboard - Experiment 6</title>
    <style>
        :root {
            --ink: #1f2933;
            --muted: #616e7c;
            --line: #d3dce6;
            --accent: #2563eb;
            --bg: #f5f7fa;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            padding: 48px 16px;
            background: var(--bg);
            color: var(--ink);
            font: 15px/1.6 system-ui, -apple-system, "Segoe UI", sans-serif;
        }

        .card {
            max-width: 620px;
            margin: 0 auto;
            padding: 32px;
            background: #fff;
            border: 1px solid var(--line);
            border-radius: 12px;
            box-shadow: 0 1px 2px rgb(16 24 40 / 6%), 0 8px 24px rgb(16 24 40 / 6%);
        }

        h1 { margin: 0 0 4px; font-size: 24px; }

        .sub { margin: 0 0 28px; color: var(--muted); font-size: 14px; }

        h2 {
            margin: 28px 0 12px;
            font-size: 15px;
            text-transform: uppercase;
            letter-spacing: .04em;
            color: var(--muted);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        th, td {
            padding: 10px 12px;
            text-align: left;
            border-bottom: 1px solid var(--line);
        }

        th {
            width: 38%;
            color: var(--muted);
            font-weight: 500;
        }

        td { font-family: ui-monospace, "Cascadia Code", Consolas, monospace; font-size: 13px; }

        ul { margin: 0; padding-left: 20px; }

        li { margin-bottom: 4px; }

        .out {
            display: inline-block;
            margin-top: 28px;
            padding: 10px 18px;
            font-weight: 600;
            font-size: 14px;
            color: #fff;
            background: var(--accent);
            border-radius: 8px;
            text-decoration: none;
        }

        .out:hover { background: #1d4ed8; }
    </style>
</head>
<body>

<main class="card">

    <h1>Welcome, <c:out value="${username}"/></h1>
    <p class="sub">You are signed in. This page is served from <code>WEB-INF/views</code>.</p>

    <h2>Session details</h2>

    <table>
        <tr>
            <th scope="row">User</th>
            <td><c:out value="${username}"/></td>
        </tr>
        <tr>
            <th scope="row">Session id</th>
            <td><c:out value="${sessionId}"/></td>
        </tr>
        <tr>
            <th scope="row">Logged in at</th>
            <td><c:out value="${loggedInAt}"/></td>
        </tr>
    </table>

    <h2>Concepts in this experiment</h2>

    <ul>
        <c:forEach var="topic" items="${topics}">
            <li><c:out value="${topic}"/></li>
        </c:forEach>
    </ul>

    <a class="out" href="${pageContext.request.contextPath}/logout">Log out</a>

</main>

</body>
</html>
