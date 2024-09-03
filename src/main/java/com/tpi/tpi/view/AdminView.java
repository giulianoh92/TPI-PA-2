package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.controller.ViewType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminView extends AbstractView<Object, AdminOperationsController> implements PanelView<AdminOperationsController> {

    @Override
    protected String getFrameTitle() {
        return "Admin Operations";
    }

    // Method to show the details of the admin list
    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller

        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Cannot create GUI in a headless environment.");
            return;
        }

        JFrame frame = new JFrame(getFrameTitle());
        frame.setSize(800, 600); // Set fixed size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton productButton = new JButton("Products CRUD");
        productButton.setBounds(30, 10, 150, 30);
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayView(ViewType.PRODUCT);
            }
        });

        JButton userButton = new JButton("Users CRUD");
        userButton.setBounds(170, 10, 150, 30);
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayView(ViewType.USER);
            }
        });

        JButton orderButton = new JButton("Orders CRUD");
        orderButton.setBounds(30, 50, 150, 30);
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayView(ViewType.ORDER);
            }
        });

        JButton customerButton = new JButton("Customers CRUD");
        customerButton.setBounds(170, 50, 150, 30);
        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayView(ViewType.CUSTOMER);
            }
        });

        panel.add(productButton);
        panel.add(userButton);
        panel.add(orderButton);
        panel.add(customerButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void handleCommit(Object[][] data) {
        // Specific commit logic for AdminView
        System.out.println("Committing admin data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }

        // Example: Call a method on the controller to handle the commit
        controller.commitAdminData(data);
    }
}