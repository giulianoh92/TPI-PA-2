/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
//import java.io.Serializable;
import java.sql.Date;



/**
 *
 * @author anton
 */
public class Usuario /*implements Serializable*/{
    
     int idUsuario;
     String password; 
     String emailUsuario;
     String nombreUsuario;
     Date fecha_registro;
     int rol; //No me acuerdo para que era
     
    
     
    public Usuario(int id, String password_, String email, String nombre, Date fecha ) {
        this.idUsuario = id;
        this.password = password_;
        this.emailUsuario= email;
        this.nombreUsuario = nombre;
        this.fecha_registro = fecha;
        this.rol= 1; //ver que onda 
       
    }         
}

 