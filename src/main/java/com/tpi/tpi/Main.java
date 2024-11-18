package com.tpi.tpi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.desktop.controller.ViewType;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        // obtener el valor de la propiedad app.mode del archivo application.properties
        String mode = context.getEnvironment().getProperty("app.mode", "desktop");

        // verificar si el valor es desktop
        if ("desktop".equalsIgnoreCase(mode)) {
            // verificar si el sistema está en modo headless
            if (System.getProperty("java.awt.headless").equals("true")) {
                System.out.println("Cannot create GUI in a headless environment.");
                return;
            }

            // obtener el bean AdminOperationsController
            AdminOperationsController adminOperationsController = context.getBean(AdminOperationsController.class);
            // mostrar la vista de administrador
            adminOperationsController.displayView(ViewType.ADMIN);
        } else {
            System.out.println("Running in web mode");
            // el modo web es manejado por Spring Boot
        }
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Main.class);

        // configurar el sistema para que no esté en modo headless (necesario para mostrar la interfaz de usuario)
        System.setProperty("java.awt.headless", "false");

        // ejecutar la aplicación
        app.run(args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
        };
    }
}