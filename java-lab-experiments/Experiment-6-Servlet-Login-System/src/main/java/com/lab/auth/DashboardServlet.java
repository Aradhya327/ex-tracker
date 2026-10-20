package com.lab.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Protected page. Reaching this without a session is impossible because
 * AuthFilter runs first and redirects to /login.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        request.setAttribute("username", session.getAttribute("username"));
        request.setAttribute("loggedInAt", session.getAttribute("loggedInAt"));
        request.setAttribute("sessionId", session.getId());

        request.setAttribute("topics", List.of(
                "Servlet lifecycle",
                "GET and POST handling",
                "HttpSession",
                "Filters",
                "Context and session listeners",
                "JSP with EL and JSTL"
        ));

        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp")
               .forward(request, response);
    }
}
