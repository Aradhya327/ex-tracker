<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Registration Result - Experiment 7</title>
    <style>
        :root {
            --ink: #1f2933;
            --muted: #616e7c;
            --line: #d3dce6;
            --accent: #2563eb;
            --ok: #067647;
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
            max-width: 480px;
            margin: 0 auto;
            padding: 32px;
            background: #fff;
            border: 1px solid var(--line);
            border-radius: 12px;
            box-shadow: 0 1px 2px rgb(16 24 40 / 6%), 0 8px 24px rgb(16 24 40 / 6%);
        }

        .badge {
            display: inline-block;
            margin-bottom: 16px;
            padding: 5px 12px;
            font-size: 13px;
            font-weight: 600;
            color: var(--ok);
            background: #ecfdf3;
            border: 1px solid #abefc6;
            border-radius: 999px;
        }

        h1 { margin: 0 0 24px; font-size: 22px; }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 14px;
        }

        th, td {
            padding: 11px 12px;
            text-align: left;
            border-bottom: 1px solid var(--line);
        }

        th {
            width: 34%;
            color: var(--muted);
            font-weight: 500;
        }

        .again {
            display: inline-block;
            margin-top: 26px;
            padding: 10px 18px;
            font-weight: 600;
            font-size: 14px;
            color: #fff;
            background: var(--accent);
            border-radius: 8px;
            text-decoration: none;
        }

        .again:hover { background: #1d4ed8; }
    </style>
</head>
<body>

<main class="card">

    <span class="badge">Registration successful</span>
    <h1>Student details</h1>

    <table>
        <tr>
            <th scope="row">Name</th>
            <td><c:out value="${student.name}"/></td>
        </tr>
        <tr>
            <th scope="row">Email</th>
            <td><c:out value="${student.email}"/></td>
        </tr>
        <tr>
            <th scope="row">Course</th>
            <td><c:out value="${student.course}"/></td>
        </tr>
        <tr>
            <th scope="row">Age</th>
            <td><c:out value="${student.age}"/></td>
        </tr>
    </table>

    <a class="again" href="${pageContext.request.contextPath}/register">Register another</a>

</main>

</body>
</html>
