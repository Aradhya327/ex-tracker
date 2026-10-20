package com.lab.auth;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Logs application start and stop, and keeps a live count of open sessions.
 * Demonstrates that listeners are called by the container, not by our code.
 */
@WebListener
public class AppLifecycleListener implements ServletContextListener, HttpSessionListener {

    private static final AtomicInteger ACTIVE_SESSIONS = new AtomicInteger();

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log("Application started: " + event.getServletContext().getContextPath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        log("Application stopped");
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        log("Session created " + event.getSession().getId()
                + " | active sessions: " + ACTIVE_SESSIONS.incrementAndGet());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        log("Session destroyed " + event.getSession().getId()
                + " | active sessions: " + ACTIVE_SESSIONS.decrementAndGet());
    }

    private static void log(String message) {
        System.out.println("[lab] " + message);
    }
}
