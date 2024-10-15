package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class CustomerView extends AbstractView<Customer, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private List<Customer> customers;

    public CustomerView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
    }

    @Override
    protected String getFrameTitle() {
        return "Gestión de Clientes";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller);
        setupPanel(controller);
    }

    @Override
    public void showPanel(AdminOperationsController controller, JPanel panel) {
        setController(controller);
        setupPanel(controller, panel);
    }

    private void setupPanel(AdminOperationsController controller) {
        String[] columnNames = {"ID", "Usuario", "Email", "Contraseña", "Dirección", "Fecha de Registro"};
        Function<Customer, Object[]> rowMapper = createRowMapper();

        customers = controller.getCustomerService().getAllCustomerList();

        JPanel tablePanel = createTable(customers, columnNames, rowMapper);
        add(tablePanel, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    private void setupPanel(AdminOperationsController controller, JPanel panel) {
        String[] columnNames = {"ID", "Usuario", "Email", "Contraseña", "Dirección", "Fecha de Registro"};
        Function<Customer, Object[]> rowMapper = createRowMapper();

        customers = controller.getCustomerService().getAllCustomerList();

        JPanel tablePanel = createTable(customers, columnNames, rowMapper);
        panel.add(tablePanel, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    private Function<Customer, Object[]> createRowMapper() {
        return customer -> new Object[]{
            customer.getUserId(),
            customer.getUsername(),
            customer.getEmail(),
            "******",
            customer.getAddress(),
            customer.getRegisterDate().toString()
        };
    }

    @Override
    public void handleCommit(Object[][] data) {
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
        }

        controller.commitCustomerData(customers);
    }
}