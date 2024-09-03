/*package com.tpi.tpi.view;

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

        String[] columnNames = {"ID", "Customer", "Status", "Total"};
        Function<Order, Object[]> rowMapper = order -> new Object[]{
            order.getId_pedido(),
            order.get().getNombre(),
            order.getEstado(),
            order.getTotal()
        };
        List<Item> items = controller.getOrderService();
        super.showPanel(orders, columnNames, rowMapper);
    }
}*/