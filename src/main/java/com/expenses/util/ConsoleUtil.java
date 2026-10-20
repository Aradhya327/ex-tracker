package com.expenses.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class for console input/output operations.
 */
public class ConsoleUtil {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy");

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static String readString(String prompt, String defaultValue) {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? defaultValue : input;
    }

    public static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public static int readInt(String prompt, int defaultValue) {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using default: " + defaultValue);
            return defaultValue;
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    public static double readDouble(String prompt, double defaultValue) {
        System.out.print(prompt + " [" + defaultValue + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Using default: " + defaultValue);
            return defaultValue;
        }
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (dd/MM/yyyy): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(input, DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy.");
            }
        }
    }

    public static LocalDate readDate(String prompt, LocalDate defaultValue) {
        System.out.print(prompt + " (dd/MM/yyyy) [" + defaultValue.format(DATE_FORMATTER) + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return LocalDate.parse(input, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Using default: " + defaultValue.format(DATE_FORMATTER));
            return defaultValue;
        }
    }

    public static YearMonth readMonth(String prompt) {
        while (true) {
            System.out.print(prompt + " (MM/yyyy): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return YearMonth.now();
            }
            try {
                return YearMonth.parse(input, MONTH_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid month format. Please use MM/yyyy.");
            }
        }
    }

    public static YearMonth readMonth(String prompt, YearMonth defaultValue) {
        System.out.print(prompt + " (MM/yyyy) [" + defaultValue.format(MONTH_FORMATTER) + "]: ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return defaultValue;
        try {
            return YearMonth.parse(input, MONTH_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid month format. Using default: " + defaultValue.format(MONTH_FORMATTER));
            return defaultValue;
        }
    }

    public static boolean readBoolean(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }

    public static void printHeader(String title) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  " + title);
        System.out.println("=".repeat(60));
    }

    public static void printSubHeader(String title) {
        System.out.println("\n--- " + title + " ---");
    }

    public static void printSuccess(String message) {
        System.out.println("✓ " + message);
    }

    public static void printError(String message) {
        System.out.println("✗ " + message);
    }

    public static void printInfo(String message) {
        System.out.println("ℹ " + message);
    }

    public static void printWarning(String message) {
        System.out.println("⚠ " + message);
    }

    public static void printTable(String[] headers, String[][] rows) {
        // Calculate column widths
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }
        for (String[] row : rows) {
            for (int i = 0; i < row.length && i < widths.length; i++) {
                widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        // Print header
        StringBuilder headerLine = new StringBuilder();
        StringBuilder separatorLine = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            headerLine.append(String.format(" %-" + widths[i] + "s |", headers[i]));
            separatorLine.append("-".repeat(widths[i] + 3)).append("+");
        }
        System.out.println(separatorLine);
        System.out.println(headerLine);
        System.out.println(separatorLine);

        // Print rows
        for (String[] row : rows) {
            StringBuilder rowLine = new StringBuilder();
            for (int i = 0; i < row.length && i < widths.length; i++) {
                rowLine.append(String.format(" %-" + widths[i] + "s |", row[i]));
            }
            System.out.println(rowLine);
        }
        System.out.println(separatorLine);
    }

    public static void waitForEnter() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: print newlines
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}