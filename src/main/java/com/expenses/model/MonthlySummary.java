package com.expenses.model;

import java.io.Serializable;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * Data transfer object for monthly summary reports.
 * Used by both client and server for serialization.
 */
public class MonthlySummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private YearMonth month;
    private double totalExpenses;
    private int expenseCount;
    private Map<Expense.Category, Double> byCategory;
    private Map<String, Double> byPaymentMethod;
    private List<Budget> budgets;

    public MonthlySummary() {}

    public MonthlySummary(YearMonth month, double totalExpenses, int expenseCount,
                          Map<Expense.Category, Double> byCategory,
                          Map<String, Double> byPaymentMethod,
                          List<Budget> budgets) {
        this.month = month;
        this.totalExpenses = totalExpenses;
        this.expenseCount = expenseCount;
        this.byCategory = byCategory;
        this.byPaymentMethod = byPaymentMethod;
        this.budgets = budgets;
    }

    // Getters and Setters
    public YearMonth getMonth() { return month; }
    public void setMonth(YearMonth month) { this.month = month; }

    public double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; }

    public int getExpenseCount() { return expenseCount; }
    public void setExpenseCount(int expenseCount) { this.expenseCount = expenseCount; }

    public Map<Expense.Category, Double> getByCategory() { return byCategory; }
    public void setByCategory(Map<Expense.Category, Double> byCategory) { this.byCategory = byCategory; }

    public Map<String, Double> getByPaymentMethod() { return byPaymentMethod; }
    public void setByPaymentMethod(Map<String, Double> byPaymentMethod) { this.byPaymentMethod = byPaymentMethod; }

    public List<Budget> getBudgets() { return budgets; }
    public void setBudgets(List<Budget> budgets) { this.budgets = budgets; }
}