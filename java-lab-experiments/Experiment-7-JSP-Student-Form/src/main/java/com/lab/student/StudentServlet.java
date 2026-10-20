package com.lab.student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Reads the JSP form, validates the fields, and forwards either back to the
 * form (with per-field errors) or on to the result page (with the Student bean).
 */
@WebServlet("/register")
public class StudentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final List<String> COURSES = List.of(
            "Computer Science",
            "Information Technology",
            "Electronics",
            "Mechanical",
            "Civil"
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("courseOptions", COURSES);
        request.getRequestDispatcher("/student.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = trim(request.getParameter("name"));
        String email = trim(request.getParameter("email"));
        String course = trim(request.getParameter("course"));
        String ageText = trim(request.getParameter("age"));

        Map<String, String> errors = new LinkedHashMap<>();

        if (name.isEmpty()) {
            errors.put("name", "Name is required.");
        } else if (name.length() < 3) {
            errors.put("name", "Name must be at least 3 characters.");
        }

        if (email.isEmpty()) {
            errors.put("email", "Email is required.");
        } else if (!email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}")) {
            errors.put("email", "Enter a valid email address.");
        }

        if (course.isEmpty() || !COURSES.contains(course)) {
            errors.put("course", "Select a course from the list.");
        }

        int age = 0;

        if (ageText.isEmpty()) {
            errors.put("age", "Age is required.");
        } else {
            try {
                age = Integer.parseInt(ageText);

                if (age < 15 || age > 100) {
                    errors.put("age", "Age must be between 15 and 100.");
                }
            } catch (NumberFormatException e) {
                errors.put("age", "Age must be a whole number.");
            }
        }

        if (!errors.isEmpty()) {
            // Send the typed values back so the user does not retype them.
            request.setAttribute("errors", errors);
            request.setAttribute("courseOptions", COURSES);
            request.setAttribute("form", echo(name, email, course, ageText));

            request.getRequestDispatcher("/student.jsp").forward(request, response);
            return;
        }

        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setCourse(course);
        student.setAge(age);

        request.setAttribute("student", student);

        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }

    /** Holds the raw input so the form can repopulate itself after an error. */
    private static Map<String, String> echo(String name, String email, String course, String age) {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("name", name);
        values.put("email", email);
        values.put("course", course);
        values.put("age", age);
        return values;
    }
}
