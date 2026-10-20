package com.expenses.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single expense entry.
 */
public class Expense {
    private static int idCounter = 0;
    private int id;
    private String title;
    private double amount;
    private Category category;
    private LocalDate date;
    private String description;
    private String paymentMethod;

    public enum Category {
        FOOD("Food & Dining"),
        TRANSPORT("Transportation"),
        SHOPPING("Shopping"),
        ENTERTAINMENT("Entertainment"),
        UTILITIES("Utilities"),
        HEALTHCARE("Healthcare"),
        EDUCATION("Education"),
        OTHER("Other");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static Category fromString(String text) {
            for (Category c : Category.values()) {
                if (c.name().equalsIgnoreCase(text) || c.displayName.equalsIgnoreCase(text)) {
                    return c;
                }
            }
            return OTHER;
        }
    }

    public Expense(String title, double amount, Category category, LocalDate date, 
                   String description, String paymentMethod) {
        this.id = ++idCounter;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.paymentMethod = paymentMethod;
    }

    // Constructor for loading from JSON (preserves ID)
    public Expense(int id, String title, double amount, Category category, LocalDate date,
                   String description, String paymentMethod) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
        this.paymentMethod = paymentMethod;
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public static void resetIdCounter(int maxId) {
        idCounter = maxId;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("[%d] %s | ₹%.2f | %s | %s | %s | %s",
                id, title, amount, category.getDisplayName(), date.format(formatter),
                paymentMethod, description != null ? description : "-");
    }

    public String toCsvString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("%d,%s,%.2f,%s,%s,%s,%s",
                id, escapeCsv(title), amount, category.name(), date.format(formatter),
                escapeCsv(paymentMethod), escapeCsv(description != null ? description : ""));
    }

    private String escapeCsv(String field) {
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}