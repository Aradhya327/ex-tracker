package com.lab.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Handles both the login form (GET) and the credential check (POST).
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Already signed in? Skip the form.
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!UserStore.isValid(username, password)) {

            // Forward rather than redirect so the typed username survives
            // and the error message can be rendered by the JSP.
            request.setAttribute("error", "Invalid username or password.");
            request.setAttribute("username", username == null ? "" : username);

            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // The session is created only after the password check passes.
        // changeSessionId() issues a new id so a session id captured before
        // login cannot be reused after it (session fixation).
        HttpSession session = request.getSession(true);
        request.changeSessionId();

        session.setAttribute("username", username.trim());
        session.setAttribute("loggedInAt", LocalDateTime.now().toString());

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
