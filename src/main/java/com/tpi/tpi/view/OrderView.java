package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Order;
import com.tpi.tpi.model.Status;

import java.sql.Date; // Add this import
import java.util.List;
import java.util.function.Function;
import javax.swing.*;
import java.awt.*;

public class OrderView extends AbstractView<Order, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final int ID_COLUMN = 0;
    private static final int STATUS_COLUMN = 2;

    private List<Status> statuses;
    private List<Order> orders;

    public OrderView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
    }

    @Override
    protected String getFrameTitle() {
        return "Order Management";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller
    
        String[] columnNames = {"ID", "Date", "Status", "Payment Method", "Total"};
        Function<Order, Object[]> rowMapper = order -> new Object[]{
            order.getOrderId(),
            order.getPayment().getPaymentDate(),
            order.getStatus().getStatus(),
            order.getPayment().getPaymentMethod(),
            order.getPayment().getAmount()
        };
    
        orders = controller.getOrderService().getAllOrders();
        statuses = controller.getOrderService().getAllStatuses();
    
        // Debug: Print orders list
        System.out.println("Orders List:");
        for (Order order : orders) {
            System.out.println(order.getOrderId() + " " + order.getPayment().getPaymentDate() + " " + order.getStatus().getStatus() + " " + order.getPayment().getPaymentMethod() + " " + order.getPayment().getAmount());
        }
    
        super.showPanel(orders, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        System.out.println("Committing order data:");
        for (Order order : orders) {
            order.printAttributes();
        }

        controller.commitOrderData(orders);
    }

    @Override
    protected void onEditRow() {
        int row = getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int columnCount = getTable().getColumnCount();
        Object[] rowData = getRowData(row, columnCount);
        JTextField[] textFields = new JTextField[columnCount];
        JComboBox<Status> statusComboBox = new JComboBox<>();
        JPanel panel = createEditPanel(columnCount, rowData, textFields, statusComboBox);
    
        populateComboBox(statusComboBox, statuses);
        selectCurrentStatus(row, statusComboBox);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields, statusComboBox);
            resetButton.setEnabled(true);
            commitButton.setEnabled(true);
        }
    }

    private Object[] getRowData(int row, int columnCount) {
        Object[] rowData = new Object[columnCount];
        for (int col = 0; col < columnCount; col++) {
            rowData[col] = getTable().getValueAt(row, col);
        }
        return rowData;
    }

    private JPanel createEditPanel(int columnCount, Object[] rowData, JTextField[] textFields, JComboBox<Status> statusComboBox) {
        JPanel panel = new JPanel(new GridLayout(columnCount, 2));
        for (int col = 0; col < columnCount; col++) {
            panel.add(new JLabel(getTable().getColumnName(col)));
            if (col == STATUS_COLUMN) {
                panel.add(statusComboBox);
            } else {
                textFields[col] = new JTextField(rowData[col] != null ? rowData[col].toString() : "");
                if (col == ID_COLUMN || col == 1 || col == 3 || col == 4) { // Make "ID", "Date", "Payment Method", and "Total" columns uneditable
                    textFields[col].setEditable(false);
                }
                panel.add(textFields[col]);
            }
        }
        return panel;
    }

    private void selectCurrentStatus(int row, JComboBox<Status> statusComboBox) {
        String currentStatusValue = (String) getTable().getValueAt(row, STATUS_COLUMN);
        Status currentStatus = statuses.stream()
                .filter(status -> status.getStatus().equals(currentStatusValue))
                .findFirst()
                .orElse(null);
        selectCurrentItem(statusComboBox, currentStatus);
    }

    private void updateTableData(int row, int columnCount, JTextField[] textFields, JComboBox<Status> statusComboBox) {
        Order order = orders.get(row);
        for (int col = 0; col < columnCount; col++) {
            if (col != ID_COLUMN) {
                if (col == STATUS_COLUMN) {
                    Status selectedStatus = (Status) statusComboBox.getSelectedItem();
                    if (selectedStatus != null) {
                        order.setStatus(selectedStatus);
                        getTable().setValueAt(selectedStatus.getStatus(), row, STATUS_COLUMN);
                    }
                } else {
                    if (textFields[col] != null) {
                        switch (col) {
                            case 1:
                                order.getPayment().setPaymentDate(Date.valueOf(textFields[col].getText()));
                                break;
                            case 3:
                                order.getPayment().setPaymentMethod(textFields[col].getText());
                                break;
                            case 4:
                                order.getPayment().setAmount(Float.parseFloat(textFields[col].getText()));
                                break;
                        }
                        getTable().setValueAt(textFields[col].getText(), row, col);
                    }
                }
            }
        }
    }
}