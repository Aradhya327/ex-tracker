package com.expenses.server;

import com.expenses.model.Expense;
import com.expenses.model.Budget;
import com.expenses.model.Request;
import com.expenses.model.Response;
import com.expenses.service.ExpenseService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.time.YearMonth;
import java.time.LocalDate;

/**
 * Server that handles expense tracker requests from clients via sockets.
 */
public class ExpenseServer {
    private static final int DEFAULT_PORT = 5000;
    private final int port;
    private final ExpenseService service;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private boolean running = false;

    public ExpenseServer(int port) {
        this.port = port;
        this.service = new ExpenseService();
        this.threadPool = Executors.newCachedThreadPool();
    }

    public ExpenseServer() {
        this(DEFAULT_PORT);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Expense Tracker Server started on port " + port);
            System.out.println("Waiting for client connections...");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(new ClientHandler(clientSocket));
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server on port " + port + ": " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
            System.out.println("Server stopped.");
        } catch (IOException e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String clientId;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            this.clientId = "Client-" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        }

        @Override
        public void run() {
            try {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                input = new ObjectInputStream(clientSocket.getInputStream());

                System.out.println("[" + clientId + "] Connected");

                while (running && !clientSocket.isClosed()) {
                    try {
                        Request request = (Request) input.readObject();
                        if (request != null) {
                            request.setClientId(clientId);
                            Response response = handleRequest(request);
                            output.writeObject(response);
                            output.flush();
                        }
                    } catch (ClassNotFoundException e) {
                        System.err.println("[" + clientId + "] Invalid request format: " + e.getMessage());
                        sendError("Invalid request format");
                    } catch (EOFException e) {
                        // Client disconnected
                        break;
                    } catch (IOException e) {
                        if (running) {
                            System.err.println("[" + clientId + "] Communication error: " + e.getMessage());
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("[" + clientId + "] Connection error: " + e.getMessage());
            } finally {
                cleanup();
                System.out.println("[" + clientId + "] Disconnected");
            }
        }

        private Response handleRequest(Request request) {
            try {
                switch (request.getType()) {
                    // Expense operations
                    case ADD_EXPENSE:
                        Expense newExpense = (Expense) request.getPayload();
                        service.addExpense(newExpense);
                        return Response.success("Expense added successfully", newExpense);

                    case GET_EXPENSE:
                        int expenseId = (Integer) request.getPayload();
                        Expense expense = service.getExpense(expenseId);
                        if (expense != null) {
                            return Response.success("Expense found", expense);
                        }
                        return Response.notFound("Expense not found with ID: " + expenseId);

                    case GET_ALL_EXPENSES:
                        List<Expense> allExpenses = service.getAllExpenses();
                        return Response.success("All expenses retrieved", allExpenses);

                    case UPDATE_EXPENSE:
                        Expense updatedExpense = (Expense) request.getPayload();
                        Expense result = service.updateExpense(updatedExpense.getId(), updatedExpense);
                        if (result != null) {
                            return Response.success("Expense updated successfully", result);
                        }
                        return Response.notFound("Expense not found with ID: " + updatedExpense.getId());

                    case DELETE_EXPENSE:
                        int deleteId = (Integer) request.getPayload();
                        boolean deleted = service.deleteExpense(deleteId);
                        if (deleted) {
                            return Response.success("Expense deleted successfully");
                        }
                        return Response.notFound("Expense not found with ID: " + deleteId);

                    case GET_EXPENSES_BY_CATEGORY:
                        Expense.Category category = (Expense.Category) request.getPayload();
                        List<Expense> byCategory = service.getExpensesByCategory(category);
                        return Response.success("Expenses by category retrieved", byCategory);

                    case GET_EXPENSES_BY_DATE_RANGE:
                        Object[] dateRange = (Object[]) request.getPayload();
                        LocalDate startDate = (LocalDate) dateRange[0];
                        LocalDate endDate = (LocalDate) dateRange[1];
                        List<Expense> byDateRange = service.getExpensesByDateRange(startDate, endDate);
                        return Response.success("Expenses by date range retrieved", byDateRange);

                    case GET_EXPENSES_BY_MONTH:
                        YearMonth month = (YearMonth) request.getPayload();
                        List<Expense> byMonth = service.getExpensesByMonth(month);
                        return Response.success("Expenses by month retrieved", byMonth);

                    case GET_EXPENSES_BY_PAYMENT_METHOD:
                        String paymentMethod = (String) request.getPayload();
                        List<Expense> byPayment = service.getExpensesByPaymentMethod(paymentMethod);
                        return Response.success("Expenses by payment method retrieved", byPayment);

                    // Budget operations
                    case ADD_BUDGET:
                        Budget newBudget = (Budget) request.getPayload();
                        service.addBudget(newBudget);
                        return Response.success("Budget added successfully", newBudget);

                    case GET_BUDGET:
                        int budgetId = (Integer) request.getPayload();
                        Budget budget = service.getBudget(budgetId);
                        if (budget != null) {
                            return Response.success("Budget found", budget);
                        }
                        return Response.notFound("Budget not found with ID: " + budgetId);

                    case GET_ALL_BUDGETS:
                        List<Budget> allBudgets = service.getAllBudgets();
                        return Response.success("All budgets retrieved", allBudgets);

                    case UPDATE_BUDGET:
                        Budget updatedBudget = (Budget) request.getPayload();
                        Budget budgetResult = service.updateBudget(updatedBudget.getId(), updatedBudget);
                        if (budgetResult != null) {
                            return Response.success("Budget updated successfully", budgetResult);
                        }
                        return Response.notFound("Budget not found with ID: " + updatedBudget.getId());

                    case DELETE_BUDGET:
                        int deleteBudgetId = (Integer) request.getPayload();
                        boolean budgetDeleted = service.deleteBudget(deleteBudgetId);
                        if (budgetDeleted) {
                            return Response.success("Budget deleted successfully");
                        }
                        return Response.notFound("Budget not found with ID: " + deleteBudgetId);

                    case GET_BUDGETS_BY_MONTH:
                        YearMonth budgetMonth = (YearMonth) request.getPayload();
                        List<Budget> budgetsByMonth = service.getBudgetsByMonth(budgetMonth);
                        return Response.success("Budgets by month retrieved", budgetsByMonth);

                    // Report operations
                    case GET_MONTHLY_SUMMARY:
                        YearMonth summaryMonth = (YearMonth) request.getPayload();
                        com.expenses.model.MonthlySummary summary = service.getMonthlySummary(summaryMonth);
                        return Response.success("Monthly summary retrieved", summary);

                    case GET_CATEGORY_SUMMARY:
                        YearMonth catMonth = (YearMonth) request.getPayload();
                        return Response.success("Category summary retrieved", service.getCategorySummary(catMonth));

                    // Export operations
                    case EXPORT_CSV:
                        String csv = service.exportToCsv();
                        return Response.success("CSV export ready", csv);

                    case EXPORT_JSON:
                        String json = service.exportToJson();
                        return Response.success("JSON export ready", json);

                    // System
                    case SYNC_DATA:
                        service.syncData();
                        return Response.success("Data synchronized");

                    case PING:
                        return Response.success("PONG");

                    default:
                        return Response.error("Unknown request type: " + request.getType());
                }
            } catch (Exception e) {
                System.err.println("[" + clientId + "] Error handling request: " + e.getMessage());
                e.printStackTrace();
                return Response.error("Server error: " + e.getMessage());
            }
        }

        private void sendError(String message) {
            try {
                output.writeObject(Response.error(message));
                output.flush();
            } catch (IOException e) {
                // Ignore
            }
        }

        private void cleanup() {
            try {
                if (input != null) input.close();
                if (output != null) output.close();
                if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number. Using default: " + DEFAULT_PORT);
            }
        }

        ExpenseServer server = new ExpenseServer(port);
        
        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        
        server.start();
    }
}