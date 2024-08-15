/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
import java.sql.Date;

/**
 *
 * @author giu
 */
public class Pago {
    private int idPago;
    private Date fechaDePago;
    private String metodoDePago;

    public Pago(int idPago, Date fechaDePag, String metodoDePago){
        this.idPago = idPago;
        this.fechaDePago = fechaDePag;
        this.metodoDePago = metodoDePago;
    }

    //getters y setters
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
}
