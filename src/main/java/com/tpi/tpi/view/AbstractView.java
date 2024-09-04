package com.tpi.tpi.view;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractView<T, C> extends JPanel {
    protected C controller;
    private JTable table;
    private Object[][] initialTableData;

    public AbstractView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = new JPanel();
            JButton resetButton = createStyledButton("Reset");
            JButton commitButton = createStyledButton("Commit");
            JButton editRowButton = createStyledButton("Edit Row"); // New button

            resetButton.addActionListener(e -> onReset());
            commitButton.addActionListener(e -> onCommit());
            editRowButton.addActionListener(e -> onEditRow()); // Add action listener

            buttonPanel.add(resetButton);
            buttonPanel.add(commitButton);
            buttonPanel.add(editRowButton); // Add button to panel

            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    protected boolean shouldShowDefaultButtons() {
        return true;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        return button;
    }

    public void setController(C controller) {
        this.controller = controller;
    }

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
            JPanel buttonPanel = new JPanel();
            JButton resetButton = createStyledButton("Reset");
            JButton commitButton = createStyledButton("Commit");
            JButton editRowButton = createStyledButton("Edit Row"); // New button

            resetButton.addActionListener(e -> onReset());
            commitButton.addActionListener(e -> onCommit());
            editRowButton.addActionListener(e -> onEditRow()); // Add action listener

            buttonPanel.add(resetButton);
            buttonPanel.add(commitButton);
            buttonPanel.add(editRowButton); // Add button to panel

            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private JScrollPane createTable(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        initialTableData = storeInitialData(data, columnNames, rowMapper);

        Object[][] tableData = Arrays.copyOf(initialTableData, initialTableData.length);
        for (int i = 0; i < tableData.length; i++) {
            tableData[i] = Arrays.copyOf(initialTableData[i], initialTableData[i].length);
        }

        // Create a custom table model
        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };

        table = new JTable(tableModel);
        styleTable(table);

        // Add mouse listener for double-click events
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    onEditRow();
                }
            }
        });

        return new JScrollPane(table);
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(70, 130, 180));
        table.setSelectionForeground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 12));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private Object[][] storeInitialData(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        Object[][] dataCopy = new Object[data.size()][columnNames.length];
        for (int i = 0; i < data.size(); i++) {
            dataCopy[i] = Arrays.copyOf(rowMapper.apply(data.get(i)), columnNames.length);
        }
        return dataCopy;
    }

    protected abstract String getFrameTitle();

    protected void onCommit() {
        System.out.println("Current table values:");
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();

        Object[][] currentData = new Object[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                currentData[row][col] = table.getValueAt(row, col);
                System.out.print(currentData[row][col] + "\t");
            }
            System.out.println();
        }

        handleCommit(currentData);
    }

    protected abstract void handleCommit(Object[][] data);

    protected void onReset() {
        if (initialTableData == null) {
            System.out.println("No initial data available to reset.");
            return;
        }

        System.out.println("Resetting table values to initial state");

        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();

        if (rowCount != initialTableData.length || columnCount != initialTableData[0].length) {
            System.out.println("Mismatch in data size. Cannot reset.");
            return;
        }

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                table.setValueAt(initialTableData[row][col], row, col);
            }
        }
    }

    private void onEditRow() {
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
            if (col == 0 || table.getColumnName(col).equalsIgnoreCase("Registered At") || table.getColumnName(col).equalsIgnoreCase("CatId") || table.getColumnName(col).equalsIgnoreCase("Category")) { // Assuming the "ID" column is the first column and "Registered At" is the column name for the registration date
                textFields[col].setEditable(false);
            }
            panel.add(textFields[col]);
        }
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            for (int col = 0; col < columnCount; col++) {
                if (col != 0 && !table.getColumnName(col).equalsIgnoreCase("Registered At") && !table.getColumnName(col).equalsIgnoreCase("CatId") && !table.getColumnName(col).equalsIgnoreCase("Category")) { // Skip the "ID" column and "Registered At" column
                    table.setValueAt(textFields[col].getText(), row, col);
                }
            }
        }
    }
}