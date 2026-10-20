package com.expenses.model;

import java.io.Serializable;
import java.util.List;

/**
 * Response object for client-server communication.
 */
public class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum Status {
        SUCCESS,
        ERROR,
        NOT_FOUND,
        VALIDATION_ERROR
    }

    private Status status;
    private String message;
    private Object data;
    private long timestamp;

    public Response() {
        this.timestamp = System.currentTimeMillis();
    }

    public Response(Status status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public Response(Status status, String message, Object data) {
        this(status, message);
        this.data = data;
    }

    public static Response success(String message) {
        return new Response(Status.SUCCESS, message);
    }

    public static Response success(String message, Object data) {
        return new Response(Status.SUCCESS, message, data);
    }

    public static Response error(String message) {
        return new Response(Status.ERROR, message);
    }

    public static Response notFound(String message) {
        return new Response(Status.NOT_FOUND, message);
    }

    public static Response validationError(String message) {
        return new Response(Status.VALIDATION_ERROR, message);
    }

    // Getters and Setters
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    
    public boolean isSuccess() { return status == Status.SUCCESS; }
}