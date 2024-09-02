package com.tpi.tpi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.tpi.tpi.controller.AdminOperationsController;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        // Get the application mode from the environment properties
        String mode = context.getEnvironment().getProperty("app.mode");

        // Check if the application is running in desktop mode
        if ("desktop".equalsIgnoreCase(mode)) {
            // Check if the environment is headless
            if (System.getProperty("java.awt.headless").equals("true")) {
                System.out.println("Cannot create GUI in a headless environment.");
                return;
            }

            // Get the AdminOperationsController bean from the application context
            AdminOperationsController adminOperationsController = context.getBean(AdminOperationsController.class);
            // Display the admin table
            adminOperationsController.displayTable("admin");
        } else {
            System.out.println("Running in web mode");
            // Web mode is handled by Spring Boot
        }
    }

    public static void main(String[] args) {
        // Set the system property to disable headless mode before the Spring application context is initialized
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(Main.class, args);
    }
}