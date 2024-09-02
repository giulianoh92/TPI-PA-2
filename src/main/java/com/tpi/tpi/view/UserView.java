package com.tpi.tpi.view;

import com.tpi.tpi.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserView implements TableView {

    // Method to show the details of the user list
    @Override
    public void showDetails(List<?> userList) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame("User List");
        frame.setSize(800, 600); // Set fixed size
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        String[] columnNames = {"ID", "Password", "Username", "Registration Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 780, 580);
        panel.add(scrollPane);

        // Populate the table with user data
        for (User user : (List<User>) userList) {
            Object[] rowData = {user.getIdUsuario(), user.getPassword(), user.getNombreUsuario(), user.getFechaRegistro()};
            tableModel.addRow(rowData);
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}