/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi.model;
import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Entity;
import java.sql.Date;

/**
 *
 * @author anton
 */
//@Entity
public class Administrator extends User implements Serializable {
    public Administrator(int idUsuario, String password, String nombreUsuario, Date fechaRegistro) {
        super(idUsuario, password, nombreUsuario, fechaRegistro);
    }
}
