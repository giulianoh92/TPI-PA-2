package com.tpi.tpi.model;

/**
 * Represents the status of an order.
 */
public class Status {
    private int userId;
    private String status;

    public Status(int userId, String status) {
        this.userId = userId;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}