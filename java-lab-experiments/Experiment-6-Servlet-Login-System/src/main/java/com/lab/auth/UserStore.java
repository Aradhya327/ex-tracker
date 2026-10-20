package com.lab.auth;

import java.util.Map;

/**
 * Tiny in-memory user table for the lab.
 *
 * Passwords are stored in plain text on purpose so the experiment stays about
 * servlets and sessions rather than cryptography. A real application would
 * store a bcrypt or Argon2 hash and never keep the raw password anywhere.
 */
public final class UserStore {

    private static final Map<String, String> USERS = Map.of(
            "admin", "admin123",
            "student", "student123"
    );

    private UserStore() {
        // utility class, never instantiated
    }

    /**
     * @return true when the username exists and the password matches.
     */
    public static boolean isValid(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        String stored = USERS.get(username.trim());
        return stored != null && stored.equals(password);
    }
}
