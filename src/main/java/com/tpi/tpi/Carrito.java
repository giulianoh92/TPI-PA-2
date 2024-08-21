/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpi.tpi;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author giu
 */
public class Carrito {
    private List<Item> items = new ArrayList<>();

    Carrito(){
    }

    //getters y setters

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }
}
