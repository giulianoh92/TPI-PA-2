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
        productButton.setBounds(170, 10, 150, 30);
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayView(ViewType.PRODUCT);
            }
        });

        JButton userButton = new JButton("Users CRUD");
        userButton.setBounds(330, 10, 150, 30);
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayView(ViewType.USER);
            }
        });

        JButton orderButton = new JButton("Orders CRUD");
        userButton.setBounds(330, 10, 150, 30);
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayView(ViewType.ORDER);
            }
        });

        panel.add(orderButton);
        panel.add(productButton);
        panel.add(userButton);

        frame.add(panel);
        frame.setVisible(true);
    }
}