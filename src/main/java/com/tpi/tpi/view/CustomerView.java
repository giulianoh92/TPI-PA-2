package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Customer;

import java.util.List;
import java.util.function.Function;

public class CustomerView extends AbstractView<Customer, AdminOperationsController> implements PanelView<AdminOperationsController> {

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
            customer.getEmail(),
            customer.getPassword(),
            customer.getDireccion(),
            customer.getFechaRegistro()
        };
        List<Customer> customers = controller.getCustomerService().getAllCustomerList();
        super.showPanel(customers, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        // Specific commit logic for CustomerView
        System.out.println("Committing customer data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }

        // Example: Call a method on the controller to handle the commit
        controller.commitCustomerData(data);
    }
}
