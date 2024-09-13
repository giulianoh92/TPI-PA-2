package com.tpi.tpi.desktop.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

public abstract class AbstractView<T, C> extends JPanel {
    private static final Color BUTTON_BACKGROUND_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 12);
    private static final Logger LOGGER = Logger.getLogger(AbstractView.class.getName());

    protected C controller;
    protected JTable table;
    private Object[][] initialTableData;
    protected JButton commitButton;
    protected JButton resetButton;
    protected JButton editRowButton;
    protected JButton addRowButton;
    protected JButton deleteRowButton;

    private boolean commitMade = false;

    public AbstractView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    protected JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        resetButton = createStyledButton("Reset");
        commitButton = createStyledButton("Commit");
        editRowButton = createStyledButton("Edit Row");
        addRowButton = createStyledButton("Add Row");
        deleteRowButton = createStyledButton("Delete Row");

        resetButton.setEnabled(false);
        commitButton.setEnabled(false);

        resetButton.addActionListener(e -> onReset());
        commitButton.addActionListener(e -> onCommit());
        editRowButton.addActionListener(e -> onEditRow());
        addRowButton.addActionListener(e -> onAdd());
        deleteRowButton.addActionListener(e -> onDelete());

        buttonPanel.add(resetButton);
        buttonPanel.add(commitButton);
        buttonPanel.add(editRowButton);

        if (isFieldEditable()) {
            buttonPanel.add(addRowButton);
            buttonPanel.add(deleteRowButton);
        }

        return buttonPanel;
    }

    protected boolean shouldShowDefaultButtons() {
        return true;
    }

    protected boolean isFieldEditable() {
        return false;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_BACKGROUND_COLOR);
        button.setForeground(BUTTON_FOREGROUND_COLOR);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        return button;
    }

    public void setController(C controller) {
        this.controller = controller;
    }

    protected void onAdd() {}

    protected void onDelete() {}

    public void showPanel(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
    
        JFrame frame = new JFrame(getFrameTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel panel = new JPanel(new BorderLayout());
    
        JScrollPane tableScrollPane = createTable(data, columnNames, rowMapper);
        panel.add(tableScrollPane, BorderLayout.CENTER);
    
        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
    
        frame.add(panel);
        frame.pack(); // Adjust the frame size to fit the preferred size of its components
        frame.setVisible(true);
    }

    protected JScrollPane createTable(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        initialTableData = storeInitialData(data, columnNames, rowMapper);
    
        Object[][] tableData = Arrays.copyOf(initialTableData, initialTableData.length);
        for (int i = 0; i < tableData.length; i++) {
            tableData[i] = Arrays.copyOf(initialTableData[i], initialTableData[i].length);
        }
    
        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        table = new JTable(tableModel);
        styleTable(table);
    
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    onEditRow();
                }
            }
        });
    
        adjustColumnSizes(); // Adjust column sizes after creating the table
    
        JScrollPane scrollPane = new JScrollPane(table);
    
        // Adjust the panel or window size to fit the table's preferred size
        Dimension tablePreferredSize = table.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(tablePreferredSize.width + 20, tablePreferredSize.height + 20)); // Add some padding
    
        return scrollPane;
    }

    private void adjustColumnSizes() {
        for (int col = 0; col < table.getColumnCount(); col++) {
            int maxWidth = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
            }
            TableColumn column = table.getColumnModel().getColumn(col);
            column.setPreferredWidth(maxWidth + 10); // Add some padding
        }
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setSelectionBackground(BUTTON_BACKGROUND_COLOR);
        table.setSelectionForeground(BUTTON_FOREGROUND_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 12));
        header.setBackground(BUTTON_BACKGROUND_COLOR);
        header.setForeground(BUTTON_FOREGROUND_COLOR);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private Object[][] storeInitialData(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        Object[][] dataCopy = new Object[data.size()][columnNames.length];
        for (int i = 0; i < data.size(); i++) {
            dataCopy[i] = Arrays.copyOf(rowMapper.apply(data.get(i)), columnNames.length);
        }

        //LOGGER.info("Initial Table Data:");
        //for (Object[] row : dataCopy) {
        //    LOGGER.info(Arrays.toString(row));
        //}

        return dataCopy;
    }

    protected abstract String getFrameTitle();

    protected void onCommit() {
        //LOGGER.info("Current table values:");
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
    
        Object[][] currentData = new Object[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                currentData[row][col] = table.getValueAt(row, col);
                //LOGGER.info(currentData[row][col] + "\t");
            }
            //LOGGER.info("\n");
        }
    
        handleCommit(currentData);
        commitMade = true; // Set the flag to true after commit
    }

    protected abstract void handleCommit(Object[][] data);

    protected void onReset() {
        if (initialTableData == null) {
            LOGGER.warning("No initial data available to reset.");
            return;
        }
    
        LOGGER.info("Resetting table values to initial state");
    
        int currentRowCount = table.getRowCount();
        int initialRowCount = initialTableData.length;
        int columnCount = table.getColumnCount();
    
        // Adjust the table's row count to match the initial data length
        if (currentRowCount < initialRowCount) {
            // Add rows
            for (int i = currentRowCount; i < initialRowCount; i++) {
                ((DefaultTableModel) table.getModel()).addRow(new Object[columnCount]);
            }
        } else if (currentRowCount > initialRowCount) {
            // Remove rows
            for (int i = currentRowCount - 1; i >= initialRowCount; i--) {
                ((DefaultTableModel) table.getModel()).removeRow(i);
            }
        }
    
        // Update the table values to match the initial data
        for (int row = 0; row < initialRowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                table.setValueAt(initialTableData[row][col], row, col);
            }
        }

        // Check if the data has changed and enable or disable the commit button accordingly
        if (checkForChanges(initialTableData) || commitMade) {
            commitButton.setEnabled(true);
        } else {
            commitButton.setEnabled(false);
        }
    }

    protected void onEditRow() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int columnCount = table.getColumnCount();
        Object[] rowData = new Object[columnCount];
        for (int col = 0; col < columnCount; col++) {
            rowData[col] = table.getValueAt(row, col);
        }

        JTextField[] textFields = new JTextField[columnCount];
        JPanel panel = new JPanel(new GridLayout(columnCount, 2));
        for (int col = 0; col < columnCount; col++) {
            panel.add(new JLabel(table.getColumnName(col)));
            textFields[col] = new JTextField(rowData[col].toString());
            if (isNonEditableColumn(col)) {
                textFields[col].setEditable(false);
            }
            panel.add(textFields[col]);
        }

        Object[][] beforeEditData = getCurrentTableData();

        //LOGGER.info("Before Edit Data:");
        //for (Object[] rowArray : beforeEditData) {
        //    LOGGER.info(Arrays.toString(rowArray));
        //}

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields);

            boolean hasChanges = checkForChanges(beforeEditData);

            //LOGGER.info("Data After Edit:");
            //logCurrentTableData();

            if (hasChanges) {
                resetButton.setEnabled(true);
                commitButton.setEnabled(true);
            }
        }
    }

    private boolean isNonEditableColumn(int col) {
        String columnName = table.getColumnName(col);
        return col == 0 || "Registered At".equals(columnName) || "Date".equals(columnName) || "Total".equals(columnName) || "Payment Method".equals(columnName) || "Username".equals(columnName) || "Password".equals(columnName) || "Email".equals(columnName) || "Address".equals(columnName);
    }

    protected Object[][] getCurrentTableData() {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                data[i][j] = table.getValueAt(i, j);
            }
        }
        return data;
    }

    private void updateTableData(int row, int columnCount, JTextField[] textFields) {
        for (int col = 0; col < columnCount; col++) {
            if (!isNonEditableColumn(col)) {
                table.setValueAt(textFields[col].getText(), row, col);
            }
        }
    }

    public boolean checkForChanges(Object[][] beforeEditData) {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (!beforeEditData[i][j].toString().equals(table.getValueAt(i, j).toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void logCurrentTableData() {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                LOGGER.info(table.getValueAt(i, j) + "\t");
            }
            LOGGER.info("\n");
        }
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    protected <E> void populateComboBox(JComboBox<E> comboBox, List<E> items) {
        for (E item : items) {
            comboBox.addItem(item);
        }
    }

    protected <E> void selectCurrentItem(JComboBox<E> comboBox, E currentItem) {
        comboBox.setSelectedItem(currentItem);
    }
}