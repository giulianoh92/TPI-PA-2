package com.tpi.tpi;

import com.tpi.tpi.controller.ProductController;
import com.tpi.tpi.repository.ProductRepository;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.view.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ProductRepository productoRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ProductView view = new ProductView();
        for (Product producto : new ArrayList<>(productoRepository.findAll())) {
            ProductController controller = new ProductController(producto, view);
            controller.updateView();
        }
    }
}
