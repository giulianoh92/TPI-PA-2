package com.tpi.tpi;

import com.tpi.tpi.controller.ProductController;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.view.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        ProductView view = new ProductView();
        ProductController controller = new ProductController(productService, view);

        controller.updateView();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
