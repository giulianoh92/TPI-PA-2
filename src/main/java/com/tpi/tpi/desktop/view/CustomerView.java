package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

public class CustomerView extends AbstractView<Customer, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final Logger LOGGER = Logger.getLogger(CustomerView.class.getName());
    private List<Customer> customers;

    public CustomerView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
    }

    @Override
    protected String getFrameTitle() {
        return "Customer Management";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        // This method can remain empty or call the other showPanel method with a default panel
    }

    @Override
    public void showPanel(AdminOperationsController controller, JPanel panel) {
        setController(controller);

        String[] columnNames = {"ID", "Username", "Email", "Password", "Address", "Registered At"};
        Function<Customer, Object[]> rowMapper = customer -> new Object[]{
            customer.getUserId(),
            customer.getUsername(),
            customer.getEmail(),
            customer.getPassword(),
            customer.getAddress(),
            customer.getRegisterDate().toString()
        };
        customers = controller.getCustomerService().getAllCustomerList();

        JScrollPane tableScrollPane = createTable(customers, columnNames, rowMapper);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Add the button panel
        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    @Override
    public void handleCommit(Object[][] data) {
        LOGGER.info("Committing customer data:");
        for (int i = 0; i < getTable().getRowCount(); i++) {
            Customer customer = customers.get(i);
            customer.setUsername((String) getTable().getValueAt(i, 1));
            customer.setEmail((String) getTable().getValueAt(i, 2));
            customer.setPassword((String) getTable().getValueAt(i, 3));
            customer.setAddress((String) getTable().getValueAt(i, 4));

            Object dateValue = getTable().getValueAt(i, 5);
            if (dateValue instanceof String string) {
                customer.setRegisterDate(java.sql.Date.valueOf(string));
            } else if (dateValue instanceof java.sql.Date date) {
                customer.setRegisterDate(date);
            }

            LOGGER.info(customer.getAddress());
        }

        controller.commitCustomerData(customers);
    }
}