package com.tpi.tpi.model;

import java.sql.Date;

/**
 * Represents a payment.
 */
public class Payment {
    private int idPago;
    private Date fechaDePago;
    private String metodoDePago;
    private double monto;

    public Payment(int idPago, Date fechaDePago, String metodoDePago, double monto) {
        this.idPago = idPago;
        this.fechaDePago = fechaDePago;
        this.metodoDePago = metodoDePago;
        this.monto = monto;
    }

    public int getIdPago() {
        return idPago;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public Date getFechaDePago() {
        return fechaDePago;
    }

    public void setFechaDePago(Date fechaDePago) {
        this.fechaDePago = fechaDePago;
    }

    public String getMetodoDePago() {
        return metodoDePago;
    }

    public void setMetodoDePago(String metodoDePago) {
        this.metodoDePago = metodoDePago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}