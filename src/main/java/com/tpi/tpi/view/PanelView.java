package com.tpi.tpi.view;

public interface PanelView<T> {
    // Method to show the details of the data list
    void showPanel(T service);

    void handleCommit(Object[][] data);
}