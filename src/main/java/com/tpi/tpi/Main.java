package com.tpi.tpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Cargar todos los productos de la base de datos
        List<Producto> productos = productoRepository.findAll();

        // Convertir la lista a un ArrayList (si realmente necesitas un ArrayList)
        ArrayList<Producto> productosArrayList = new ArrayList<>(productos);

        // Imprimir la lista de productos para verificar
        for (Producto producto : productosArrayList) {
            System.out.println("Producto ID: " + producto.getIdProducto() + ", Nombre: " + producto.getNombre());
        }
    }
}
