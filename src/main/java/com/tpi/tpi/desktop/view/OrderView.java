package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Order;
import com.tpi.tpi.common.model.Status;

import java.sql.Date;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OrderView extends AbstractView<Order, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final int ID_COLUMN = 0;
    private static final int CUSTOMER_COLUMN = 1;
    private static final int STATUS_COLUMN = 2;
    private static final int DATE_COLUMN = 3;
    private static final int PAYMENT_METHOD_COLUMN = 4;
    private static final int TOTAL_COLUMN = 5;
    private static final Logger LOGGER = Logger.getLogger(OrderView.class.getName());

    private List<Status> statuses;
    private List<Order> orders;
    private JTable itemsTable;

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
        setController(controller);

        String[] columnNames = {"ID", "Customer", "Status", "Date", "Payment Method", "Total"};
        Function<Order, Object[]> rowMapper = order -> new Object[]{
            order.getOrderId(),
            controller.getCustomerService().getCustomerByOrderId(order.getOrderId()).getUsername(),
            order.getStatus().getStatus(),
            order.getPayment().getPaymentDate(),
            order.getPayment().getPaymentMethod(),
            order.getPayment().getAmount()
        };

        orders = controller.getOrderService().getAllOrders();
        statuses = controller.getOrderService().getAllStatuses();

        JScrollPane ordersScrollPane = createTable(orders, columnNames, rowMapper);
        itemsTable = new JTable();
        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ordersScrollPane, itemsScrollPane);
        splitPane.setDividerSize(0); // Remove the divider gap
        splitPane.setResizeWeight(0.5); // Distribute space equally

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(splitPane, BorderLayout.CENTER);

        // Add the button panel
        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        JFrame frame = new JFrame(getFrameTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                Order selectedOrder = orders.get(table.getSelectedRow());
                updateItemsTable(selectedOrder);
            }
        });
    }

    @Override
    public void showPanel(AdminOperationsController controller, JPanel panel) {
        setController(controller);

        String[] columnNames = {"ID", "Customer", "Status", "Date", "Payment Method", "Total"};
        Function<Order, Object[]> rowMapper = order -> new Object[]{
            order.getOrderId(),
            controller.getCustomerService().getCustomerByOrderId(order.getOrderId()).getUsername(),
            order.getStatus().getStatus(),
            order.getPayment().getPaymentDate(),
            order.getPayment().getPaymentMethod(),
            order.getPayment().getAmount()
        };

        orders = controller.getOrderService().getAllOrders();
        statuses = controller.getOrderService().getAllStatuses();

        JScrollPane ordersScrollPane = createTable(orders, columnNames, rowMapper);
        itemsTable = new JTable();
        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ordersScrollPane, itemsScrollPane);
        splitPane.setDividerSize(0); // Remove the divider gap
        splitPane.setResizeWeight(0.5); // Distribute space equally

        panel.add(splitPane, BorderLayout.CENTER);

        // Add the button panel
        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                Order selectedOrder = orders.get(table.getSelectedRow());
                updateItemsTable(selectedOrder);
            }
        });
    }

    private void updateItemsTable(Order order) {
        String[] columnNames = {"Product", "Unit Price", "Amount", "Total Price"};
        Object[][] data = new Object[order.getItems().size()][4];

        for (int i = 0; i < order.getItems().size(); i++) {
            Item item = order.getItems().get(i);
            data[i][0] = item.getProduct().getName();
            data[i][1] = item.getProduct().getUnitPrice();
            data[i][2] = item.getAmount();
            data[i][3] = item.getProduct().getUnitPrice() * item.getAmount();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemsTable.setModel(tableModel);
    }

    @Override
    public void handleCommit(Object[][] data) {
        LOGGER.info("Committing order data:");
        for (Order order : orders) {
            order.printAttributes();
        }

        controller.commitOrderData(orders);

        commitButton.setEnabled(false);
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

        Object[][] beforeEditData = getCurrentTableData();

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields, statusComboBox);

            boolean hasChanges = checkForChanges(beforeEditData);

            if (hasChanges) {
                resetButton.setEnabled(true);
                commitButton.setEnabled(true);
            }
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
                if (col == ID_COLUMN || col == DATE_COLUMN || col == PAYMENT_METHOD_COLUMN || col == TOTAL_COLUMN || col == CUSTOMER_COLUMN) {
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
                        updateOrderField(order, col, textFields[col].getText());
                        getTable().setValueAt(textFields[col].getText(), row, col);
                    }
                }
            }
        }
    }

    private void updateOrderField(Order order, int col, String value) {
        switch (col) {
            case DATE_COLUMN:
                order.getPayment().setPaymentDate(Date.valueOf(value));
                break;
            case PAYMENT_METHOD_COLUMN:
                order.getPayment().setPaymentMethod(value);
                break;
            case TOTAL_COLUMN:
                order.getPayment().setAmount(Float.parseFloat(value));
                break;
        }
    }

    protected Object[][] getCurrentTableData() {
        int rowCount = getTable().getRowCount();
        int columnCount = getTable().getColumnCount();
        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                data[i][j] = getTable().getValueAt(i, j);
            }
        }
        return data;
    }

    public boolean checkForChanges(Object[][] beforeEditData) {
        int rowCount = getTable().getRowCount();
        int columnCount = getTable().getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (!beforeEditData[i][j].toString().equals(getTable().getValueAt(i, j).toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void logCurrentTableData() {
        int rowCount = getTable().getRowCount();
        int columnCount = getTable().getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                LOGGER.info(getTable().getValueAt(i, j) + "\t");
            }
            LOGGER.info("\n");
        }
    }
}