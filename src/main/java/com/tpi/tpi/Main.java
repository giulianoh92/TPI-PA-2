package com.tpi.tpi;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.service.AdminService;
import com.tpi.tpi.service.ProductService;
import com.tpi.tpi.view.AdminView;
import com.tpi.tpi.view.ProductView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        String mode = context.getEnvironment().getProperty("app.mode");

        if ("desktop".equalsIgnoreCase(mode)) {
            if (System.getProperty("java.awt.headless").equals("true")) {
                System.out.println("Cannot create GUI in a headless environment.");
                return;
            }

            ProductView productView = new ProductView();
            AdminView adminView = new AdminView();
            AdminOperationsController adminOperationsController = new AdminOperationsController(adminService, productService, adminView, productView);
            adminView.setController(adminOperationsController);

            adminOperationsController.displayAdminTable();
        } else {
            System.out.println("Running in web mode");
            // Web mode is handled by Spring Boot
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}