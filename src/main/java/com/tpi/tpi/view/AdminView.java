package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.controller.ViewType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        productButton = new JButton("Products");
        productButton.addActionListener(e -> controller.displayView(ViewType.PRODUCT));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(productButton, gbc);

        userButton = new JButton("Users");
        userButton.addActionListener(e -> controller.displayView(ViewType.USER));
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userButton, gbc);

        orderButton = new JButton("Orders");
        orderButton.addActionListener(e -> controller.displayView(ViewType.ORDER));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(orderButton, gbc);

        customerButton = new JButton("Customers");
        customerButton.addActionListener(e -> controller.displayView(ViewType.CUSTOMER));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(customerButton, gbc);

        setBorder(new EmptyBorder(20, 20, 20, 20)); // Add padding around the panel
    }

    @Override
    protected boolean shouldShowDefaultButtons() {
        return false;
    }

    @Override
    protected String getFrameTitle() {
        return "Admin Operations";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller
        JFrame frame = new JFrame(getFrameTitle());
        frame.setSize(400, 200); // Set fixed size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(this);  // Add the panel directly
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