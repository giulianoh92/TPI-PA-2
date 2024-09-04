package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.controller.ViewType;

import javax.swing.*;
import java.awt.*;

public class AdminView extends AbstractView<Object, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private JButton productButton;
    private JButton userButton;
    private JButton orderButton;
    private JButton customerButton;
    
    public AdminView() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(null);

        productButton = new JButton("Products CRUD");
        productButton.setBounds(30, 10, 150, 30);
        productButton.addActionListener(e -> controller.displayView(ViewType.PRODUCT));

        userButton = new JButton("Users CRUD");
        userButton.setBounds(170, 10, 150, 30);
        userButton.addActionListener(e -> controller.displayView(ViewType.USER));

        orderButton = new JButton("Orders CRUD");
        orderButton.setBounds(30, 50, 150, 30);
        orderButton.addActionListener(e -> controller.displayView(ViewType.ORDER));

        customerButton = new JButton("Customers CRUD");
        customerButton.setBounds(170, 50, 150, 30);
        customerButton.addActionListener(e -> controller.displayView(ViewType.CUSTOMER));

        add(productButton);
        add(userButton);
        add(orderButton);
        add(customerButton);
    }

    @Override
    protected String getFrameTitle() {
        return "Admin Operations";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller
        JFrame frame = new JFrame(getFrameTitle());
        frame.setSize(800, 600); // Set fixed size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(this);  // Añadir el panel directamente
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

        controller.commitAdminData(data);
    }
}
