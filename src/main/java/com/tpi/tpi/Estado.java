package com.tpi.tpi;
// no se si esta clase es necesaria en realidad --por ver
public class Estado {
    private int id;
    private String estado;

    public Estado(int id, String estado){
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
