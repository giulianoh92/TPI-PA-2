package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Customer;
import com.tpi.tpi.view.AbstractView; // Corrected import

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
        return "Customer Management";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller

        String[] columnNames = {"ID", "Username", "Email", "Password", "Address", "Registered At"};
        Function<Customer, Object[]> rowMapper = customer -> new Object[]{
            customer.getIdUsuario(),
            customer.getNombreUsuario(),
            customer.getEmailUsuario(),
            customer.getPassword(),
            customer.getDireccion(),
            customer.getFechaRegistro().toString() // Convert Date to String for display
        };
        customers = controller.getCustomerService().getAllCustomerList();
        super.showPanel(customers, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        System.out.println("Committing customer data:");
        for (int i = 0; i < getTable().getRowCount(); i++) {
            Customer customer = customers.get(i);
            customer.setNombreUsuario((String) getTable().getValueAt(i, 1));
            customer.setEmailUsuario((String) getTable().getValueAt(i, 2));
            customer.setPassword((String) getTable().getValueAt(i, 3));
            customer.setDireccion((String) getTable().getValueAt(i, 4));
            
            // Handle Date conversion
            Object dateValue = getTable().getValueAt(i, 5);
            if (dateValue instanceof String) {
                customer.setFechaRegistro(java.sql.Date.valueOf((String) dateValue));
            } else if (dateValue instanceof java.sql.Date) {
                customer.setFechaRegistro((java.sql.Date) dateValue);
            }
            
            System.out.println(customer.getDireccion());
        }

        controller.commitCustomerData(customers);
    }
}