package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Order;

import java.util.List;
import java.util.function.Function;

public class OrderView extends AbstractView<Order, AdminOperationsController> implements PanelView<AdminOperationsController> {

    @Override
    protected String getFrameTitle() {
        return "Order Management";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller

        String[] columnNames = {"ID", "Date", "Status", "Payment Method", "Total"};
        Function<Order, Object[]> rowMapper = order -> new Object[]{
            order.getIdPedido(),
            order.getPago().getFechaDePago(),
            order.getEstado().getEstado(),
            order.getPago().getMetodoDePago(),
            order.getPago().getMonto()
        };

        List<Order> orders = controller.getOrderService().getAllOrders();
        super.showPanel(orders, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        // Specific commit logic for OrderView
        System.out.println("Committing order data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }

        // Example: Call a method on the controller to handle the commit
        controller.commitOrderData(data);
    }
}
