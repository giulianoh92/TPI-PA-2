package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.Item;
import com.tpi.tpi.common.model.Order;
import com.tpi.tpi.common.model.Status;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Date;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

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
        setupPanel(controller);
    }

    @Override
    public void showPanel(AdminOperationsController controller, JPanel panel) {
        setController(controller);
        setupPanel(controller, panel);
    }

    private void setupPanel(AdminOperationsController controller) {
        String[] columnNames = {"ID", "Customer", "Status", "Date", "Payment Method", "Total"};
        Function<Order, Object[]> rowMapper = createRowMapper(controller);

        orders = controller.getOrderService().getAllOrders();
        statuses = controller.getOrderService().getAllStatuses();

        JPanel ordersScrollPane = createTable(orders, columnNames, rowMapper);
        itemsTable = new JTable();
        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ordersScrollPane, itemsScrollPane);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.5);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(splitPane, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        JFrame frame = new JFrame(getFrameTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        configureTableSelectionListener();
    }

    private void setupPanel(AdminOperationsController controller, JPanel panel) {
        String[] columnNames = {"ID", "Customer", "Status", "Date", "Payment Method", "Total"};
        Function<Order, Object[]> rowMapper = createRowMapper(controller);

        orders = controller.getOrderService().getAllOrders();
        statuses = controller.getOrderService().getAllStatuses();

        JPanel ordersScrollPane = createTable(orders, columnNames, rowMapper);
        itemsTable = new JTable();
        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ordersScrollPane, itemsScrollPane);
        splitPane.setDividerSize(0);
        splitPane.setResizeWeight(0.5);

        panel.add(splitPane, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        configureTableSelectionListener();
    }

    private Function<Order, Object[]> createRowMapper(AdminOperationsController controller) {
        return order -> new Object[]{
            order.getOrderId(),
            controller.getCustomerService().getCustomerByOrderId(order.getOrderId()).getUsername(),
            order.getStatus().getStatus(),
            order.getPayment().getPaymentDate(),
            order.getPayment().getPaymentMethod(),
            order.getPayment().getAmount()
        };
    }

    private void configureTableSelectionListener() {
        JTable table = getTable();
        if (table != null) {
            configureTableSorter(table);
            table.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    Order selectedOrder = orders.get(table.convertRowIndexToModel(table.getSelectedRow()));
                    updateItemsTable(selectedOrder);
                }
            });
        }

        if (!orders.isEmpty()) {
            updateItemsTable(orders.get(0));
        }
    }

    private void configureTableSorter(JTable table) {
        RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
        if (rowSorter instanceof TableRowSorter) {
            TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) rowSorter;
            sorter.setComparator(ID_COLUMN, (o1, o2) -> {
                try {
                    return Integer.compare(Integer.parseInt(o1.toString()), Integer.parseInt(o2.toString()));
                } catch (NumberFormatException e) {
                    return o1.toString().compareTo(o2.toString());
                }
            });
        }
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
        int viewRow = getTable().getSelectedRow();
        if (viewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        int modelRow = getTable().convertRowIndexToModel(viewRow);
        int columnCount = getTable().getColumnCount();
        Object[] rowData = getRowData(modelRow, columnCount);
        JTextField[] textFields = new JTextField[columnCount];
        JComboBox<Status> statusComboBox = new JComboBox<>();
        JPanel panel = createEditPanel(columnCount, rowData, textFields, statusComboBox);
    
        populateComboBox(statusComboBox, statuses);
        selectCurrentStatus(modelRow, statusComboBox);
    
        Object[][] beforeEditData = getCurrentTableData();
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(modelRow, columnCount, textFields, statusComboBox);
    
            boolean hasChanges = checkForChanges(beforeEditData);
    
            if (hasChanges) {
                commitButton.setEnabled(true);
            }
        }
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

    private void selectCurrentStatus(int modelRow, JComboBox<Status> statusComboBox) {
        String currentStatusValue = (String) getTable().getValueAt(modelRow, STATUS_COLUMN);
        Status currentStatus = statuses.stream()
                .filter(status -> status.getStatus().equals(currentStatusValue))
                .findFirst()
                .orElse(null);
        selectCurrentItem(statusComboBox, currentStatus);
    }

    private void updateTableData(int modelRow, int columnCount, JTextField[] textFields, JComboBox<Status> statusComboBox) {
        Order order = orders.get(modelRow);
        for (int col = 0; col < columnCount; col++) {
            if (col == STATUS_COLUMN) {
                Status selectedStatus = (Status) statusComboBox.getSelectedItem();
                order.setStatus(selectedStatus);
                getTable().setValueAt(selectedStatus.getStatus(), modelRow, col);
            } else if (col != ID_COLUMN) {
                String value = textFields[col].getText();
                getTable().setValueAt(value, modelRow, col);
                updateOrderField(order, col, value);
            }
            LOGGER.info("Updated column " + getTable().getColumnName(col) + " for Order ID " + order.getOrderId() + " to " + getTable().getValueAt(modelRow, col));
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