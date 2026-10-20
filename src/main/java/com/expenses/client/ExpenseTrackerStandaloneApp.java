package com.expenses.client;

import com.expenses.model.Expense;
import com.expenses.model.Budget;
import com.expenses.model.MonthlySummary;
import com.expenses.service.ExpenseService;
import com.expenses.util.ConsoleUtil;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Standalone console application for the Expense Tracker.
 * Runs entirely locally without requiring a server.
 */
public class ExpenseTrackerStandaloneApp {
    private final ExpenseService service;
    private boolean running = true;

    public ExpenseTrackerStandaloneApp() {
        this.service = new ExpenseService();
    }

    public void run() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("EXPENSE TRACKER - STANDALONE MODE");
        ConsoleUtil.printInfo("Running in local mode (no server required)");

        while (running) {
            showMainMenu();
            int choice = ConsoleUtil.readInt("Enter your choice: ");
            handleMenuChoice(choice);
        }

        service.syncData();
        ConsoleUtil.printInfo("Goodbye!");
    }

    private void showMainMenu() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printHeader("EXPENSE TRACKER - MAIN MENU");
        System.out.println("1.  Add Expense");
        System.out.println("2.  View All Expenses");
        System.out.println("3.  View Expense by ID");
        System.out.println("4.  Update Expense");
        System.out.println("5.  Delete Expense");
        System.out.println("6.  Filter Expenses");
        System.out.println("7.  Manage Budgets");
        System.out.println("8.  View Reports");
        System.out.println("9.  Export Data");
        System.out.println("10. Save & Sync Data");
        System.out.println("0.  Exit");
    }

    private void handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> addExpense();
            case 2 -> viewAllExpenses();
            case 3 -> viewExpenseById();
            case 4 -> updateExpense();
            case 5 -> deleteExpense();
            case 6 -> filterExpenses();
            case 7 -> manageBudgets();
            case 8 -> viewReports();
            case 9 -> exportData();
            case 10 -> { service.syncData(); ConsoleUtil.printSuccess("Data saved successfully!"); }
            case 0 -> running = false;
            default -> ConsoleUtil.printError("Invalid choice. Please try again.");
        }
        if (choice != 0) ConsoleUtil.waitForEnter();
    }

    private void addExpense() {
        ConsoleUtil.printSubHeader("ADD NEW EXPENSE");
        
        String title = ConsoleUtil.readString("Title: ");
        if (title.isEmpty()) {
            ConsoleUtil.printError("Title cannot be empty");
            return;
        }

        double amount = ConsoleUtil.readDouble("Amount (₹): ");
        if (amount <= 0) {
            ConsoleUtil.printError("Amount must be positive");
            return;
        }

        ConsoleUtil.printInfo("Categories: FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, UTILITIES, HEALTHCARE, EDUCATION, OTHER");
        String categoryStr = ConsoleUtil.readString("Category: ").toUpperCase();
        Expense.Category category = Expense.Category.fromString(categoryStr);

        LocalDate date = ConsoleUtil.readDate("Date", LocalDate.now());

        String description = ConsoleUtil.readString("Description (optional): ");

        String paymentMethod = ConsoleUtil.readString("Payment Method (Cash/Card/UPI/Other): ", "Cash");

        Expense expense = new Expense(title, amount, category, date, description, paymentMethod);
        Expense result = service.addExpense(expense);
        
        if (result != null) {
            ConsoleUtil.printSuccess("Expense added successfully! (ID: " + result.getId() + ")");
            
            // Check budget
            Budget budget = service.getBudgetByCategoryAndMonth(category, YearMonth.from(date));
            if (budget != null) {
                budget.addExpense(amount);
                service.updateBudget(budget.getId(), budget);
                if (budget.isOverBudget()) {
                    ConsoleUtil.printWarning("⚠ Budget exceeded for " + category.getDisplayName() + "! Limit: ₹" + budget.getLimitAmount() + ", Spent: ₹" + budget.getSpentAmount());
                } else if (budget.isNearLimit()) {
                    ConsoleUtil.printWarning("⚠ Near budget limit for " + category.getDisplayName() + " (" + String.format("%.1f", budget.getUsagePercentage()) + "%)");
                }
            }
        }
    }

    private void viewAllExpenses() {
        ConsoleUtil.printSubHeader("ALL EXPENSES");
        List<Expense> expenses = service.getAllExpenses();
        
        if (expenses.isEmpty()) {
            ConsoleUtil.printInfo("No expenses found.");
            return;
        }

        expenses.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        String[] headers = {"ID", "Title", "Amount", "Category", "Date", "Payment", "Description"};
        String[][] rows = new String[expenses.size()][7];
        
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            rows[i][0] = String.valueOf(e.getId());
            rows[i][1] = e.getTitle();
            rows[i][2] = String.format("₹%.2f", e.getAmount());
            rows[i][3] = e.getCategory().getDisplayName();
            rows[i][4] = e.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            rows[i][5] = e.getPaymentMethod();
            rows[i][6] = e.getDescription() != null ? e.getDescription() : "-";
        }
        
        ConsoleUtil.printTable(headers, rows);
        
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        ConsoleUtil.printInfo("Total: ₹" + String.format("%.2f", total) + " (" + expenses.size() + " expenses)");
    }

    private void viewExpenseById() {
        ConsoleUtil.printSubHeader("VIEW EXPENSE BY ID");
        int id = ConsoleUtil.readInt("Enter Expense ID: ");
        Expense expense = service.getExpense(id);
        
        if (expense != null) {
            ConsoleUtil.printSuccess("Expense found:");
            System.out.println(expense);
        } else {
            ConsoleUtil.printError("Expense not found with ID: " + id);
        }
    }

    private void updateExpense() {
        ConsoleUtil.printSubHeader("UPDATE EXPENSE");
        int id = ConsoleUtil.readInt("Enter Expense ID to update: ");
        Expense existing = service.getExpense(id);
        
        if (existing == null) {
            ConsoleUtil.printError("Expense not found");
            return;
        }

        ConsoleUtil.printInfo("Current: " + existing);
        ConsoleUtil.printInfo("Leave blank to keep current value");

        String title = ConsoleUtil.readString("Title", existing.getTitle());
        double amount = ConsoleUtil.readDouble("Amount", existing.getAmount());
        
        ConsoleUtil.printInfo("Categories: FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, UTILITIES, HEALTHCARE, EDUCATION, OTHER");
        String categoryStr = ConsoleUtil.readString("Category", existing.getCategory().name()).toUpperCase();
        Expense.Category category = Expense.Category.fromString(categoryStr);
        
        LocalDate date = ConsoleUtil.readDate("Date", existing.getDate());
        String description = ConsoleUtil.readString("Description", existing.getDescription() != null ? existing.getDescription() : "");
        String paymentMethod = ConsoleUtil.readString("Payment Method", existing.getPaymentMethod());

        Expense updated = new Expense(id, title, amount, category, date, description, paymentMethod);
        Expense result = service.updateExpense(id, updated);
        
        if (result != null) {
            ConsoleUtil.printSuccess("Expense updated successfully!");
        }
    }

    private void deleteExpense() {
        ConsoleUtil.printSubHeader("DELETE EXPENSE");
        int id = ConsoleUtil.readInt("Enter Expense ID to delete: ");
        Expense expense = service.getExpense(id);
        
        if (expense == null) {
            ConsoleUtil.printError("Expense not found");
            return;
        }

        ConsoleUtil.printWarning("About to delete: " + expense);
        if (ConsoleUtil.readBoolean("Are you sure?")) {
            if (service.deleteExpense(id)) {
                ConsoleUtil.printSuccess("Expense deleted successfully!");
            }
        } else {
            ConsoleUtil.printInfo("Deletion cancelled");
        }
    }

    private void filterExpenses() {
        ConsoleUtil.printSubHeader("FILTER EXPENSES");
        System.out.println("1. By Category");
        System.out.println("2. By Date Range");
        System.out.println("3. By Month");
        System.out.println("4. By Payment Method");
        System.out.println("0. Back");
        
        int choice = ConsoleUtil.readInt("Enter choice: ");
        List<Expense> results = List.of();
        
        switch (choice) {
            case 1 -> {
                ConsoleUtil.printInfo("Categories: FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, UTILITIES, HEALTHCARE, EDUCATION, OTHER");
                String catStr = ConsoleUtil.readString("Category: ").toUpperCase();
                Expense.Category category = Expense.Category.fromString(catStr);
                results = service.getExpensesByCategory(category);
            }
            case 2 -> {
                LocalDate start = ConsoleUtil.readDate("Start Date");
                LocalDate end = ConsoleUtil.readDate("End Date");
                results = service.getExpensesByDateRange(start, end);
            }
            case 3 -> {
                YearMonth month = ConsoleUtil.readMonth("Month", YearMonth.now());
                results = service.getExpensesByMonth(month);
            }
            case 4 -> {
                String method = ConsoleUtil.readString("Payment Method: ");
                results = service.getExpensesByPaymentMethod(method);
            }
            case 0 -> { return; }
            default -> { ConsoleUtil.printError("Invalid choice"); return; }
        }
        
        displayExpenseList(results, "FILTER RESULTS");
    }

    private void displayExpenseList(List<Expense> expenses, String title) {
        ConsoleUtil.printSubHeader(title);
        
        if (expenses.isEmpty()) {
            ConsoleUtil.printInfo("No expenses found.");
            return;
        }

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        
        String[] headers = {"ID", "Title", "Amount", "Category", "Date", "Payment", "Description"};
        String[][] rows = new String[expenses.size()][7];
        
        for (int i = 0; i < expenses.size(); i++) {
            Expense e = expenses.get(i);
            rows[i][0] = String.valueOf(e.getId());
            rows[i][1] = e.getTitle();
            rows[i][2] = String.format("₹%.2f", e.getAmount());
            rows[i][3] = e.getCategory().getDisplayName();
            rows[i][4] = e.getDate().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            rows[i][5] = e.getPaymentMethod();
            rows[i][6] = e.getDescription() != null ? e.getDescription() : "-";
        }
        
        ConsoleUtil.printTable(headers, rows);
        ConsoleUtil.printInfo("Total: ₹" + String.format("%.2f", total) + " (" + expenses.size() + " expenses)");
    }

    private void manageBudgets() {
        ConsoleUtil.printSubHeader("BUDGET MANAGEMENT");
        System.out.println("1. Add Budget");
        System.out.println("2. View All Budgets");
        System.out.println("3. View Budgets by Month");
        System.out.println("4. Update Budget");
        System.out.println("5. Delete Budget");
        System.out.println("0. Back");
        
        int choice = ConsoleUtil.readInt("Enter choice: ");
        
        switch (choice) {
            case 1 -> addBudget();
            case 2 -> viewAllBudgets();
            case 3 -> viewBudgetsByMonth();
            case 4 -> updateBudget();
            case 5 -> deleteBudget();
            case 0 -> { return; }
            default -> ConsoleUtil.printError("Invalid choice");
        }
    }

    private void addBudget() {
        ConsoleUtil.printInfo("Categories: FOOD, TRANSPORT, SHOPPING, ENTERTAINMENT, UTILITIES, HEALTHCARE, EDUCATION, OTHER");
        String catStr = ConsoleUtil.readString("Category: ").toUpperCase();
        Expense.Category category = Expense.Category.fromString(catStr);
        
        YearMonth month = ConsoleUtil.readMonth("Month", YearMonth.now());
        double limit = ConsoleUtil.readDouble("Budget Limit (₹): ");
        
        if (limit <= 0) {
            ConsoleUtil.printError("Limit must be positive");
            return;
        }

        // Check if budget already exists
        Budget existing = service.getBudgetByCategoryAndMonth(category, month);
        if (existing != null) {
            ConsoleUtil.printWarning("Budget already exists for " + category.getDisplayName() + " in " + month);
            if (!ConsoleUtil.readBoolean("Update existing budget?")) {
                return;
            }
            existing.setLimitAmount(limit);
            service.updateBudget(existing.getId(), existing);
            ConsoleUtil.printSuccess("Budget updated successfully!");
            return;
        }

        Budget budget = new Budget(category, month, limit);
        Budget result = service.addBudget(budget);
        
        if (result != null) {
            ConsoleUtil.printSuccess("Budget added successfully!");
        }
    }

    private void viewAllBudgets() {
        ConsoleUtil.printSubHeader("ALL BUDGETS");
        List<Budget> budgets = service.getAllBudgets();
        
        if (budgets.isEmpty()) {
            ConsoleUtil.printInfo("No budgets found.");
            return;
        }

        for (Budget b : budgets) {
            System.out.println(b);
            if (b.isOverBudget()) {
                ConsoleUtil.printWarning("  >>> OVER BUDGET! <<<");
            } else if (b.isNearLimit()) {
                ConsoleUtil.printWarning("  >>> Near limit (" + String.format("%.1f", b.getUsagePercentage()) + "%) <<<");
            }
        }
    }

    private void viewBudgetsByMonth() {
        YearMonth month = ConsoleUtil.readMonth("Month", YearMonth.now());
        List<Budget> budgets = service.getBudgetsByMonth(month);
        
        ConsoleUtil.printSubHeader("BUDGETS FOR " + month);
        
        if (budgets.isEmpty()) {
            ConsoleUtil.printInfo("No budgets for this month.");
            return;
        }

        for (Budget b : budgets) {
            System.out.println(b);
            if (b.isOverBudget()) {
                ConsoleUtil.printWarning("  >>> OVER BUDGET! <<<");
            } else if (b.isNearLimit()) {
                ConsoleUtil.printWarning("  >>> Near limit <<<");
            }
        }
    }

    private void updateBudget() {
        ConsoleUtil.printSubHeader("UPDATE BUDGET");
        int id = ConsoleUtil.readInt("Enter Budget ID: ");
        Budget existing = service.getBudget(id);
        
        if (existing == null) {
            ConsoleUtil.printError("Budget not found");
            return;
        }

        ConsoleUtil.printInfo("Current: " + existing);
        double limit = ConsoleUtil.readDouble("New Limit (₹): ", existing.getLimitAmount());
        
        existing.setLimitAmount(limit);
        Budget result = service.updateBudget(id, existing);
        
        if (result != null) {
            ConsoleUtil.printSuccess("Budget updated successfully!");
        }
    }

    private void deleteBudget() {
        int id = ConsoleUtil.readInt("Enter Budget ID to delete: ");
        Budget budget = service.getBudget(id);
        
        if (budget == null) {
            ConsoleUtil.printError("Budget not found");
            return;
        }

        if (ConsoleUtil.readBoolean("Are you sure?")) {
            if (service.deleteBudget(id)) {
                ConsoleUtil.printSuccess("Budget deleted successfully!");
            }
        }
    }

    private void viewReports() {
        ConsoleUtil.printSubHeader("REPORTS");
        System.out.println("1. Monthly Summary");
        System.out.println("2. Category Summary");
        System.out.println("3. Payment Method Summary");
        System.out.println("0. Back");
        
        int choice = ConsoleUtil.readInt("Enter choice: ");
        
        switch (choice) {
            case 1 -> viewMonthlySummary();
            case 2 -> viewCategorySummary();
            case 3 -> viewPaymentMethodSummary();
            case 0 -> { return; }
            default -> ConsoleUtil.printError("Invalid choice");
        }
    }

    private void viewMonthlySummary() {
        YearMonth month = ConsoleUtil.readMonth("Month", YearMonth.now());
        MonthlySummary summary = service.getMonthlySummary(month);
        
        ConsoleUtil.printSubHeader("MONTHLY SUMMARY - " + month);
        ConsoleUtil.printInfo("Total Expenses: ₹" + String.format("%.2f", summary.getTotalExpenses()));
        ConsoleUtil.printInfo("Number of Transactions: " + summary.getExpenseCount());
        
        if (!summary.getByCategory().isEmpty()) {
            ConsoleUtil.printSubHeader("By Category");
            String[] headers = {"Category", "Amount", "Percentage"};
            String[][] rows = new String[summary.getByCategory().size()][3];
            int i = 0;
            for (Map.Entry<Expense.Category, Double> entry : summary.getByCategory().entrySet()) {
                double pct = summary.getTotalExpenses() > 0 ? (entry.getValue() / summary.getTotalExpenses()) * 100 : 0;
                rows[i][0] = entry.getKey().getDisplayName();
                rows[i][1] = String.format("₹%.2f", entry.getValue());
                rows[i][2] = String.format("%.1f%%", pct);
                i++;
            }
            ConsoleUtil.printTable(headers, rows);
        }
        
        if (!summary.getByPaymentMethod().isEmpty()) {
            ConsoleUtil.printSubHeader("By Payment Method");
            String[] headers = {"Payment Method", "Amount", "Percentage"};
            String[][] rows = new String[summary.getByPaymentMethod().size()][3];
            int i = 0;
            for (Map.Entry<String, Double> entry : summary.getByPaymentMethod().entrySet()) {
                double pct = summary.getTotalExpenses() > 0 ? (entry.getValue() / summary.getTotalExpenses()) * 100 : 0;
                rows[i][0] = entry.getKey();
                rows[i][1] = String.format("₹%.2f", entry.getValue());
                rows[i][2] = String.format("%.1f%%", pct);
                i++;
            }
            ConsoleUtil.printTable(headers, rows);
        }
        
        if (!summary.getBudgets().isEmpty()) {
            ConsoleUtil.printSubHeader("Budget Status");
            for (Budget b : summary.getBudgets()) {
                System.out.println(b);
                if (b.isOverBudget()) {
                    ConsoleUtil.printWarning("  >>> OVER BUDGET! <<<");
                } else if (b.isNearLimit()) {
                    ConsoleUtil.printWarning("  >>> Near limit (" + String.format("%.1f", b.getUsagePercentage()) + "%) <<<");
                }
            }
        }
    }

    private void viewCategorySummary() {
        YearMonth month = ConsoleUtil.readMonth("Month", YearMonth.now());
        Map<Expense.Category, Double> summary = service.getCategorySummary(month);
        
        ConsoleUtil.printSubHeader("CATEGORY SUMMARY - " + month);
        
        if (summary.isEmpty()) {
            ConsoleUtil.printInfo("No expenses for this month.");
            return;
        }

        String[] headers = {"Category", "Amount", "Percentage"};
        double total = summary.values().stream().mapToDouble(Double::doubleValue).sum();
        String[][] rows = new String[summary.size()][3];
        int i = 0;
        for (Map.Entry<Expense.Category, Double> entry : summary.entrySet()) {
            double pct = total > 0 ? (entry.getValue() / total) * 100 : 0;
            rows[i][0] = entry.getKey().getDisplayName();
            rows[i][1] = String.format("₹%.2f", entry.getValue());
            rows[i][2] = String.format("%.1f%%", pct);
            i++;
        }
        ConsoleUtil.printTable(headers, rows);
        ConsoleUtil.printInfo("Total: ₹" + String.format("%.2f", total));
    }

    private void viewPaymentMethodSummary() {
        YearMonth month = ConsoleUtil.readMonth("Month", YearMonth.now());
        Map<String, Double> summary = service.getPaymentMethodSummary(month);
        
        ConsoleUtil.printSubHeader("PAYMENT METHOD SUMMARY - " + month);
        
        if (summary.isEmpty()) {
            ConsoleUtil.printInfo("No expenses for this month.");
            return;
        }

        String[] headers = {"Payment Method", "Amount", "Percentage"};
        double total = summary.values().stream().mapToDouble(Double::doubleValue).sum();
        String[][] rows = new String[summary.size()][3];
        int i = 0;
        for (Map.Entry<String, Double> entry : summary.entrySet()) {
            double pct = total > 0 ? (entry.getValue() / total) * 100 : 0;
            rows[i][0] = entry.getKey();
            rows[i][1] = String.format("₹%.2f", entry.getValue());
            rows[i][2] = String.format("%.1f%%", pct);
            i++;
        }
        ConsoleUtil.printTable(headers, rows);
        ConsoleUtil.printInfo("Total: ₹" + String.format("%.2f", total));
    }

    private void exportData() {
        ConsoleUtil.printSubHeader("EXPORT DATA");
        System.out.println("1. Export to CSV");
        System.out.println("2. Export to JSON");
        System.out.println("0. Back");
        
        int choice = ConsoleUtil.readInt("Enter choice: ");
        
        switch (choice) {
            case 1 -> {
                String csv = service.exportToCsv();
                String filename = "expenses_export_" + LocalDate.now() + ".csv";
                saveToFile(csv, filename);
                ConsoleUtil.printSuccess("Exported to " + filename);
            }
            case 2 -> {
                String json = service.exportToJson();
                String filename = "expenses_export_" + LocalDate.now() + ".json";
                saveToFile(json, filename);
                ConsoleUtil.printSuccess("Exported to " + filename);
            }
            case 0 -> { return; }
            default -> ConsoleUtil.printError("Invalid choice");
        }
    }

    private void saveToFile(String content, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(content);
        } catch (Exception e) {
            ConsoleUtil.printError("Error saving file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ExpenseTrackerStandaloneApp().run();
    }
}