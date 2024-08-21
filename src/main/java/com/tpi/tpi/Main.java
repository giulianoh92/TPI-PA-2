/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author giu
 */
public class Main {
    public static void main(String[] args) {


        // solamente pruebas de funcionamiento de los objetos

        Cliente cliente = new Cliente("calle 1", 0, "pass", "user@gmail.com", "word", new Date(0));
        System.out.println(cliente.getNombreUsuario());
        System.out.println(cliente.getPassword());
        System.out.println(cliente.getDireccion());
        System.out.println(cliente.getEmail());
        System.out.println(cliente.getfechaRegistro());

        Producto prod1 = new Producto(1, "Redmi Note 11 Pro 128GB", "6.7' 5000mAh", 200, 2, new CategoriaDeProducto(0, "Smartphone"));
        
        Item item = new Item(1, prod1);

        cliente.addToCarrito(item);

        Carrito carrito = cliente.getCarrito();
        for (Item curItem : carrito.getItems()) {
            System.out.println(curItem.getProducto().getNombre());
        }
    }
}
