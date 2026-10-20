package com.expenses.client;

import com.expenses.util.ConsoleUtil;

/**
 * Main launcher for the Expense Tracker application.
 * Allows choosing between standalone and client-server mode.
 */
public class ExpenseTrackerLauncher {
    public static void main(String[] args) {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("EXPENSE TRACKER - LAUNCHER");
        ConsoleUtil.printInfo("Choose application mode:");
        System.out.println();
        System.out.println("1. Standalone Mode (Local only, no server needed)");
        System.out.println("2. Client-Server Mode (Connects to Expense Server)");
        System.out.println("3. Start Server Only");
        System.out.println("0. Exit");
        System.out.println();

        int choice = ConsoleUtil.readInt("Enter your choice: ");

        switch (choice) {
            case 1 -> {
                ConsoleUtil.printInfo("Starting Standalone Mode...");
                new ExpenseTrackerStandaloneApp().run();
            }
            case 2 -> {
                String host = ConsoleUtil.readString("Server Host", "localhost");
                int port = ConsoleUtil.readInt("Server Port", 5000);
                ConsoleUtil.printInfo("Starting Client Mode - Connecting to " + host + ":" + port + "...");
                new ExpenseTrackerClientApp(host, port).run();
            }
            case 3 -> {
                int port = ConsoleUtil.readInt("Server Port", 5000);
                ConsoleUtil.printInfo("Starting Server on port " + port + "...");
                ConsoleUtil.printInfo("Press Ctrl+C to stop the server");
                com.expenses.server.ExpenseServer server = new com.expenses.server.ExpenseServer(port);
                Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
                server.start();
            }
            case 0 -> ConsoleUtil.printInfo("Goodbye!");
            default -> ConsoleUtil.printError("Invalid choice");
        }
    }
}