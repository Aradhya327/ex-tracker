package com.expenses.client;

import com.expenses.model.Expense;
import com.expenses.model.Budget;
import com.expenses.model.MonthlySummary;
import com.expenses.model.Request;
import com.expenses.model.Response;
import com.expenses.util.ConsoleUtil;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

/**
 * Client that connects to the Expense Tracker Server.
 */
public class ExpenseClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5000;

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean connected = false;

    public ExpenseClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ExpenseClient() {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            connected = true;
            ConsoleUtil.printSuccess("Connected to server at " + host + ":" + port);
            return true;
        } catch (IOException e) {
            ConsoleUtil.printError("Failed to connect to server: " + e.getMessage());
            return false;
        }
    }

    public void disconnect() {
        try {
            if (output != null) output.close();
            if (input != null) input.close();
            if (socket != null && !socket.isClosed()) socket.close();
            connected = false;
            ConsoleUtil.printInfo("Disconnected from server");
        } catch (IOException e) {
            ConsoleUtil.printError("Error disconnecting: " + e.getMessage());
        }
    }

    public boolean isConnected() {
        return connected && socket != null && !socket.isClosed();
    }

    private Response sendRequest(Request request) {
        if (!isConnected()) {
            ConsoleUtil.printError("Not connected to server");
            return Response.error("Not connected to server");
        }

        try {
            output.writeObject(request);
            output.flush();
            return (Response) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            ConsoleUtil.printError("Communication error: " + e.getMessage());
            connected = false;
            return Response.error("Communication error: " + e.getMessage());
        }
    }

    // ========== Expense Operations ==========

    public Expense addExpense(Expense expense) {
        Response response = sendRequest(new Request(Request.Type.ADD_EXPENSE, expense));
        if (response.isSuccess()) {
            return (Expense) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    public Expense getExpense(int id) {
        Response response = sendRequest(new Request(Request.Type.GET_EXPENSE, id));
        if (response.isSuccess()) {
            return (Expense) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    public List<Expense> getAllExpenses() {
        Response response = sendRequest(new Request(Request.Type.GET_ALL_EXPENSES));
        if (response.isSuccess()) {
            return (List<Expense>) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return List.of();
    }

    public Expense updateExpense(Expense expense) {
        Response response = sendRequest(new Request(Request.Type.UPDATE_EXPENSE, expense));
        if (response.isSuccess()) {
            return (Expense) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    public boolean deleteExpense(int id) {
        Response response = sendRequest(new Request(Request.Type.DELETE_EXPENSE, id));
        if (response.isSuccess()) {
            return true;
        }
        ConsoleUtil.printError(response.getMessage());
        return false;
    }

    public List<Expense> getExpensesByCategory(Expense.Category category) {
        Response response = sendRequest(new Request(Request.Type.GET_EXPENSES_BY_CATEGORY, category));
        if (response.isSuccess()) {
            return (List<Expense>) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return List.of();
    }

    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        Response response = sendRequest(new Request(Request.Type.GET_EXPENSES_BY_DATE_RANGE, new Object[]{startDate, endDate}));
        if (response.isSuccess()) {
            return (List<Expense>) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return List.of();
    }

    public List<Expense> getExpensesByMonth(YearMonth month) {
        Response response = sendRequest(new Request(Request.Type.GET_EXPENSES_BY_MONTH, month));
        if (response.isSuccess()) {
            return (List<Expense>) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return List.of();
    }

    public List<Expense> getExpensesByPaymentMethod(String paymentMethod) {
        Response response = sendRequest(new Request(Request.Type.GET_EXPENSES_BY_PAYMENT_METHOD, paymentMethod));
        if (response.isSuccess()) {
            return (List<Expense>) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return List.of();
    }

    // ========== Budget Operations ==========

    public Budget addBudget(Budget budget) {
        Response response = sendRequest(new Request(Request.Type.ADD_BUDGET, budget));
        if (response.isSuccess()) {
            return (Budget) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    public Budget getBudget(int id) {
        Response response = sendRequest(new Request(Request.Type.GET_BUDGET, id));
        if (response.isSuccess()) {
            return (Budget) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    public List<Budget> getAllBudgets() {
        Response response = sendRequest(new Request(Request.Type.GET_ALL_BUDGETS));
        if (response.isSuccess()) {
            return (List<Budget>) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return List.of();
    }

    public Budget updateBudget(Budget budget) {
        Response response = sendRequest(new Request(Request.Type.UPDATE_BUDGET, budget));
        if (response.isSuccess()) {
            return (Budget) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    public boolean deleteBudget(int id) {
        Response response = sendRequest(new Request(Request.Type.DELETE_BUDGET, id));
        if (response.isSuccess()) {
            return true;
        }
        ConsoleUtil.printError(response.getMessage());
        return false;
    }

    public List<Budget> getBudgetsByMonth(YearMonth month) {
        Response response = sendRequest(new Request(Request.Type.GET_BUDGETS_BY_MONTH, month));
        if (response.isSuccess()) {
            return (List<Budget>) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return List.of();
    }

    // ========== Report Operations ==========

    public MonthlySummary getMonthlySummary(YearMonth month) {
        Response response = sendRequest(new Request(Request.Type.GET_MONTHLY_SUMMARY, month));
        if (response.isSuccess()) {
            return (MonthlySummary) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    // ========== Export Operations ==========

    public String exportCsv() {
        Response response = sendRequest(new Request(Request.Type.EXPORT_CSV));
        if (response.isSuccess()) {
            return (String) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    public String exportJson() {
        Response response = sendRequest(new Request(Request.Type.EXPORT_JSON));
        if (response.isSuccess()) {
            return (String) response.getData();
        }
        ConsoleUtil.printError(response.getMessage());
        return null;
    }

    // ========== System ==========

    public boolean ping() {
        Response response = sendRequest(new Request(Request.Type.PING));
        return response.isSuccess();
    }

    public boolean syncData() {
        Response response = sendRequest(new Request(Request.Type.SYNC_DATA));
        if (response.isSuccess()) {
            ConsoleUtil.printSuccess("Data synchronized with server");
            return true;
        }
        ConsoleUtil.printError(response.getMessage());
        return false;
    }
}