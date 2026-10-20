package com.expenses.service;

import com.expenses.model.Expense;
import com.expenses.model.Budget;
import com.expenses.model.MonthlySummary;
import com.expenses.util.JsonUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Service layer for managing expenses and budgets with JSON file persistence.
 */
public class ExpenseService {
    private static final String DATA_DIR = "data";
    private static final String EXPENSES_FILE = DATA_DIR + File.separator + "expenses.json";
    private static final String BUDGETS_FILE = DATA_DIR + File.separator + "budgets.json";

    private Map<Integer, Expense> expenses = new ConcurrentHashMap<>();
    private Map<Integer, Budget> budgets = new ConcurrentHashMap<>();
    private int expenseIdCounter = 0;
    private int budgetIdCounter = 0;

    public ExpenseService() {
        loadData();
    }

    // ========== Expense Operations ==========
    
    public Expense addExpense(Expense expense) {
        if (expense.getId() <= 0) {
            // Client-supplied entries may not have an ID yet (or may reuse an ID
            // from another client), so the service owns ID assignment.
            expense.setId(++expenseIdCounter);
        } else if (expense.getId() > expenseIdCounter) {
            expenseIdCounter = expense.getId();
            Expense.resetIdCounter(expenseIdCounter);
        }
        expenses.put(expense.getId(), expense);
        saveExpenses();
        return expense;
    }

    public Expense getExpense(int id) {
        return expenses.get(id);
    }

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(expenses.values());
    }

    public List<Expense> getExpensesByCategory(Expense.Category category) {
        return expenses.values().stream()
                .filter(e -> e.getCategory() == category)
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByDateRange(LocalDate startDate, LocalDate endDate) {
        return expenses.values().stream()
                .filter(e -> !e.getDate().isBefore(startDate) && !e.getDate().isAfter(endDate))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByMonth(YearMonth month) {
        return expenses.values().stream()
                .filter(e -> YearMonth.from(e.getDate()).equals(month))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByPaymentMethod(String paymentMethod) {
        return expenses.values().stream()
                .filter(e -> e.getPaymentMethod().equalsIgnoreCase(paymentMethod))
                .sorted(Comparator.comparing(Expense::getDate).reversed())
                .collect(Collectors.toList());
    }

    public Expense updateExpense(int id, Expense updatedExpense) {
        if (expenses.containsKey(id)) {
            updatedExpense.setId(id);
            expenses.put(id, updatedExpense);
            saveExpenses();
            return updatedExpense;
        }
        return null;
    }

    public boolean deleteExpense(int id) {
        Expense removed = expenses.remove(id);
        if (removed != null) {
            saveExpenses();
            return true;
        }
        return false;
    }

    // ========== Budget Operations ==========
    
    public Budget addBudget(Budget budget) {
        if (budget.getId() <= 0) {
            budget.setId(++budgetIdCounter);
        } else if (budget.getId() > budgetIdCounter) {
            budgetIdCounter = budget.getId();
        }
        refreshBudgetSpent(budget);
        budgets.put(budget.getId(), budget);
        saveBudgets();
        return budget;
    }

    public Budget getBudget(int id) {
        Budget budget = budgets.get(id);
        refreshBudgetSpent(budget);
        return budget;
    }

    public Budget getBudgetByCategoryAndMonth(Expense.Category category, YearMonth month) {
        Budget budget = budgets.values().stream()
                .filter(b -> b.getCategory() == category && b.getMonth().equals(month))
                .findFirst()
                .orElse(null);
        refreshBudgetSpent(budget);
        return budget;
    }

    public List<Budget> getAllBudgets() {
        List<Budget> all = new ArrayList<>(budgets.values());
        all.forEach(this::refreshBudgetSpent);
        return all;
    }

    public List<Budget> getBudgetsByMonth(YearMonth month) {
        List<Budget> budgetsForMonth = budgets.values().stream()
                .filter(b -> b.getMonth().equals(month))
                .collect(Collectors.toList());
        budgetsForMonth.forEach(this::refreshBudgetSpent);
        return budgetsForMonth;
    }

    public Budget updateBudget(int id, Budget updatedBudget) {
        if (budgets.containsKey(id)) {
            updatedBudget.setId(id);
            refreshBudgetSpent(updatedBudget);
            budgets.put(id, updatedBudget);
            saveBudgets();
            return updatedBudget;
        }
        return null;
    }

    public boolean deleteBudget(int id) {
        Budget removed = budgets.remove(id);
        if (removed != null) {
            saveBudgets();
            return true;
        }
        return false;
    }

    // ========== Report Operations ==========
    
    public Map<Expense.Category, Double> getCategorySummary(YearMonth month) {
        List<Expense> monthlyExpenses = getExpensesByMonth(month);
        return monthlyExpenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    public double getTotalExpenses(YearMonth month) {
        return getExpensesByMonth(month).stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public Map<String, Double> getPaymentMethodSummary(YearMonth month) {
        List<Expense> monthlyExpenses = getExpensesByMonth(month);
        return monthlyExpenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getPaymentMethod,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    public MonthlySummary getMonthlySummary(YearMonth month) {
        List<Expense> expenses = getExpensesByMonth(month);
        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();
        Map<Expense.Category, Double> byCategory = getCategorySummary(month);
        Map<String, Double> byPaymentMethod = getPaymentMethodSummary(month);
        List<Budget> budgets = getBudgetsByMonth(month);
        
        return new MonthlySummary(month, total, expenses.size(), byCategory, byPaymentMethod, budgets);
    }

    // ========== Export Operations ==========
    
    public String exportToCsv() {
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Title,Amount,Category,Date,Payment Method,Description\n");
        List<Expense> allExpenses = getAllExpenses();
        allExpenses.sort(Comparator.comparing(Expense::getDate).reversed());
        for (Expense e : allExpenses) {
            csv.append(e.toCsvString()).append("\n");
        }
        return csv.toString();
    }

    public String exportToJson() {
        return JsonUtil.toJson(getAllExpenses());
    }

    // ========== Budget tracking ==========

    /**
     * Recomputes the spent amount of a budget from the actual expenses for its
     * category and month, so edits/deletes can never leave the total out of sync.
     */
    private void refreshBudgetSpent(Budget budget) {
        if (budget == null || budget.getCategory() == null || budget.getMonth() == null) {
            return;
        }
        double spent = expenses.values().stream()
                .filter(e -> e.getCategory() == budget.getCategory())
                .filter(e -> e.getDate() != null && YearMonth.from(e.getDate()).equals(budget.getMonth()))
                .mapToDouble(Expense::getAmount)
                .sum();
        budget.setSpentAmount(spent);
    }

    // ========== Persistence ==========
    
    private void loadData() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Load expenses
        List<Expense> loadedExpenses = JsonUtil.fromJsonFile(EXPENSES_FILE, Expense[].class);
        if (loadedExpenses != null) {
            for (Expense e : loadedExpenses) {
                expenses.put(e.getId(), e);
                if (e.getId() > expenseIdCounter) {
                    expenseIdCounter = e.getId();
                }
            }
        }
        Expense.resetIdCounter(expenseIdCounter);

        // Load budgets
        List<Budget> loadedBudgets = JsonUtil.fromJsonFile(BUDGETS_FILE, Budget[].class);
        if (loadedBudgets != null) {
            for (Budget b : loadedBudgets) {
                budgets.put(b.getId(), b);
                if (b.getId() > budgetIdCounter) {
                    budgetIdCounter = b.getId();
                }
            }
        }
    }

    private void saveExpenses() {
        JsonUtil.toJsonFile(new ArrayList<>(expenses.values()), EXPENSES_FILE);
    }

    private void saveBudgets() {
        JsonUtil.toJsonFile(new ArrayList<>(budgets.values()), BUDGETS_FILE);
    }

    public void syncData() {
        saveExpenses();
        saveBudgets();
    }
}