package com.tpi.tpi.desktop.view;

import javax.swing.JPanel;

public interface PanelView<T> {
    // Method to show the details of the data list
    void showPanel(T service);

    void showPanel(T service, JPanel panel); // Add this method

    void handleCommit(Object[][] data);
}