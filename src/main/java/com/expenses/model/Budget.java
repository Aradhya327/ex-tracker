package com.expenses.model;

import java.time.YearMonth;

/**
 * Represents a budget limit for a category in a specific month.
 */
public class Budget {
    private int id;
    private Expense.Category category;
    private YearMonth month;
    private double limitAmount;
    private double spentAmount;

    public Budget(Expense.Category category, YearMonth month, double limitAmount) {
        this.category = category;
        this.month = month;
        this.limitAmount = limitAmount;
        this.spentAmount = 0.0;
    }

    public Budget(int id, Expense.Category category, YearMonth month, double limitAmount, double spentAmount) {
        this.id = id;
        this.category = category;
        this.month = month;
        this.limitAmount = limitAmount;
        this.spentAmount = spentAmount;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Expense.Category getCategory() { return category; }
    public void setCategory(Expense.Category category) { this.category = category; }
    
    public YearMonth getMonth() { return month; }
    public void setMonth(YearMonth month) { this.month = month; }
    
    public double getLimitAmount() { return limitAmount; }
    public void setLimitAmount(double limitAmount) { this.limitAmount = limitAmount; }
    
    public double getSpentAmount() { return spentAmount; }
    public void setSpentAmount(double spentAmount) { this.spentAmount = spentAmount; }
    
    public double getRemainingAmount() { return limitAmount - spentAmount; }
    
    public double getUsagePercentage() { 
        return limitAmount > 0 ? (spentAmount / limitAmount) * 100 : 0; 
    }
    
    public boolean isOverBudget() { return spentAmount > limitAmount; }
    
    public boolean isNearLimit() { return getUsagePercentage() >= 80; }

    public void addExpense(double amount) {
        this.spentAmount += amount;
    }

    @Override
    public String toString() {
        return String.format("Budget: %s | Month: %s | Limit: ₹%.2f | Spent: ₹%.2f | Remaining: ₹%.2f (%.1f%%)",
                category.getDisplayName(), month, limitAmount, spentAmount, getRemainingAmount(), getUsagePercentage());
    }
}