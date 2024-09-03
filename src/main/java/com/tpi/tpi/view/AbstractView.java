package com.tpi.tpi.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractView<T, C> {
    protected C controller;
    private JTable table;
    private Object[][] initialTableData;

    // Method to set the controller
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

        // Add the table to the panel
        JScrollPane tableScrollPane = createTable(data, columnNames, rowMapper);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        JButton commitButton = new JButton("Commit");

        // Add action listeners to the buttons
        resetButton.addActionListener(e -> onReset());
        commitButton.addActionListener(e -> onCommit());

        // Add buttons to the button panel
        buttonPanel.add(resetButton);
        buttonPanel.add(commitButton);

        // Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private JScrollPane createTable(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        // Store a hard copy of the initial data
        initialTableData = storeInitialData(data, columnNames, rowMapper);

        // Use a copy of the initial data for the table
        Object[][] tableData = Arrays.copyOf(initialTableData, initialTableData.length);
        for (int i = 0; i < tableData.length; i++) {
            tableData[i] = Arrays.copyOf(initialTableData[i], initialTableData[i].length);
        }

        // Create the table with a copy of the initial data
        table = new JTable(tableData, columnNames);
        return new JScrollPane(table);
    }

    private Object[][] storeInitialData(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        Object[][] dataCopy = new Object[data.size()][columnNames.length];
        for (int i = 0; i < data.size(); i++) {
            dataCopy[i] = Arrays.copyOf(rowMapper.apply(data.get(i)), columnNames.length);
        }
        return dataCopy;
    }

    protected abstract String getFrameTitle();

    // Method to print the current content of the table
    protected void onCommit() {
        System.out.println("Current table values:");
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();

        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                System.out.print(table.getValueAt(row, col) + "\t");
            }
            System.out.println();
        }
    }

    // Method to refresh the table values to their initial state
    protected void onReset() {
        if (initialTableData == null) {
            System.out.println("No initial data available to reset.");
            return;
        }
        
        System.out.println("Resetting table values to initial state");
        
        // Check if the current table data size matches the initial data size
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
    
        if (rowCount != initialTableData.length || columnCount != initialTableData[0].length) {
            System.out.println("Mismatch in data size. Cannot reset.");
            return;
        }
    
        // Restore table data to initial state
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                table.setValueAt(initialTableData[row][col], row, col);
            }
        }
    }
}
