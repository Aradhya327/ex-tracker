<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Student Information - Experiment 7</title>
    <style>
        :root {
            --ink: #1f2933;
            --muted: #616e7c;
            --line: #d3dce6;
            --accent: #2563eb;
            --danger: #b42318;
            --bg: #f5f7fa;
        }

        * { box-sizing: border-box; }

        body {
            margin: 0;
            padding: 48px 16px;
            background: var(--bg);
            color: var(--ink);
            font: 15px/1.5 system-ui, -apple-system, "Segoe UI", sans-serif;
        }

        .card {
            max-width: 460px;
            margin: 0 auto;
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

        input, select {
            width: 100%;
            padding: 10px 12px;
            margin-bottom: 4px;
            font: inherit;
            color: inherit;
            background: #fff;
            border: 1px solid var(--line);
            border-radius: 8px;
        }

        input:focus, select:focus {
            outline: 2px solid var(--accent);
            outline-offset: 1px;
            border-color: transparent;
        }

        .field { margin-bottom: 16px; }

        .err {
            display: block;
            margin-top: 5px;
            color: var(--danger);
            font-size: 13px;
        }

        .err:empty { display: none; }

        .bad { border-color: #fda29b; }

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
    </style>
</head>
<body>

<main class="card">

    <h1>Student Information</h1>
    <p class="sub">Experiment 7 &mdash; JSP form, servlet validation, forward</p>

    <form action="register" method="post">

        <div class="field">
            <label for="name">Full name</label>
            <input type="text" id="name" name="name"
                   class="${not empty errors.name ? 'bad' : ''}"
                   value="<c:out value='${form.name}'/>"
                   autocomplete="name" required>
            <span class="err">${errors.name}</span>
        </div>

        <div class="field">
            <label for="email">Email</label>
            <input type="email" id="email" name="email"
                   class="${not empty errors.email ? 'bad' : ''}"
                   value="<c:out value='${form.email}'/>"
                   autocomplete="email" required>
            <span class="err">${errors.email}</span>
        </div>

        <div class="field">
            <label for="course">Course</label>
            <select id="course" name="course"
                    class="${not empty errors.course ? 'bad' : ''}" required>
                <option value="">-- select a course --</option>
                <c:forEach var="option" items="${courseOptions}">
                    <option value="<c:out value='${option}'/>"
                        ${form.course == option ? 'selected' : ''}>
                        <c:out value="${option}"/>
                    </option>
                </c:forEach>
            </select>
            <span class="err">${errors.course}</span>
        </div>

        <div class="field">
            <label for="age">Age</label>
            <input type="number" id="age" name="age" min="15" max="100"
                   class="${not empty errors.age ? 'bad' : ''}"
                   value="<c:out value='${form.age}'/>" required>
            <span class="err">${errors.age}</span>
        </div>

        <button type="submit">Register</button>

    </form>

</main>

</body>
</html>
