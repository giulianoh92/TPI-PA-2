package com.tpi.tpi.common.model;

import java.sql.Date;

/**
 * Represents a payment.
 */
public class Payment {
    private int paymentId;
    private Date paymentDate;
    private String paymentMethod;
    private double amount;

    public Payment(int paymentId, Date paymentDate, String paymentMethod, double amount) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}