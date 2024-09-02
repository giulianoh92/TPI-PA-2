package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminView implements TableView {
    private AdminOperationsController controller;

    // Method to set the controller
    public void setController(AdminOperationsController controller) {
        this.controller = controller;
    }

    // Method to show the details of the admin list
    @Override
    public void showDetails(List<?> adminList) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Cannot create GUI in a headless environment.");
            return;
        }

        JFrame frame = new JFrame("Admin Operations");
        frame.setSize(800, 600); // Set fixed size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton productButton = new JButton("Show Products");
        productButton.setBounds(170, 10, 150, 30);
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayTable("product");
            }
        });

        JButton userButton = new JButton("Show Users");
        userButton.setBounds(330, 10, 150, 30);
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayTable("user");
            }
        });

        panel.add(productButton);
        panel.add(userButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}