package com.expenses.model;

import java.io.Serializable;
import java.util.List;

/**
 * Request object for client-server communication.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Type {
        // Expense operations
        ADD_EXPENSE,
        GET_EXPENSE,
        GET_ALL_EXPENSES,
        UPDATE_EXPENSE,
        DELETE_EXPENSE,
        GET_EXPENSES_BY_CATEGORY,
        GET_EXPENSES_BY_DATE_RANGE,
        GET_EXPENSES_BY_MONTH,
        
        // Budget operations
        ADD_BUDGET,
        GET_BUDGET,
        GET_ALL_BUDGETS,
        UPDATE_BUDGET,
        DELETE_BUDGET,
        GET_BUDGETS_BY_MONTH,
        
        // Report operations
        GET_MONTHLY_SUMMARY,
        GET_CATEGORY_SUMMARY,
        GET_EXPENSES_BY_PAYMENT_METHOD,
        
        // Export operations
        EXPORT_CSV,
        EXPORT_JSON,
        
        // System
        SYNC_DATA,
        PING
    }

    private Type type;
    private Object payload;
    private String clientId;

    public Request(Type type) {
        this.type = type;
    }

    public Request(Type type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public Request(Type type, Object payload, String clientId) {
        this.type = type;
        this.payload = payload;
        this.clientId = clientId;
    }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    
    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }
    
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
}