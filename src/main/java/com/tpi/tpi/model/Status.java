package com.tpi.tpi.model;
// no se si esta clase es necesaria en realidad --por ver
public class Status {
    private int id;
    private String estado;

    public Status(int id, String estado){
        this.id = id;
        this.estado = estado;
    }
    public int getId(){
        return id;
    }
    public String getEstado(){
        return estado;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setEstado(String estado){
        this.estado = estado;
    }
}
