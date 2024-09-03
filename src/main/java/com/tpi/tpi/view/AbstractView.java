package com.tpi.tpi.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractView<T, C> {
    protected C controller;

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

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private JScrollPane createTable(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        Object[][] tableData = new Object[data.size()][columnNames.length];

        for (int i = 0; i < data.size(); i++) {
            tableData[i] = rowMapper.apply(data.get(i));
        }

        JTable table = new JTable(tableData, columnNames);
        return new JScrollPane(table);
    }

    protected abstract String getFrameTitle();
}