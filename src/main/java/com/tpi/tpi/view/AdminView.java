package com.tpi.tpi.view;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.tpi.tpi.model.Administrator;
import com.tpi.tpi.controller.AdminOperationsController;

public class AdminView {
    private AdminOperationsController controller;

    public void setController(AdminOperationsController controller) {
        this.controller = controller;
    }

    public void showAdminDetails(List<Administrator> adminList) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Cannot create GUI in a headless environment.");
            return;
        }

        JFrame frame = new JFrame("Admin Operations");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton adminButton = new JButton("Show Admins");
        adminButton.setBounds(10, 10, 150, 30);
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAdminTable(adminList);
            }
        });

        JButton productButton = new JButton("Show Products");
        productButton.setBounds(170, 10, 150, 30);
        productButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.displayProductTable();
            }
        });

        panel.add(adminButton);
        panel.add(productButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void showAdminTable(List<Administrator> adminList) {
        JFrame frame = new JFrame("Admin List");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        String[] columnNames = {"ID", "Password", "Username", "Registration Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 580, 380);
        panel.add(scrollPane);

        for (Administrator admin : adminList) {
            Object[] rowData = {admin.getIdUsuario(), admin.getPassword(), admin.getNombreUsuario(), admin.getFechaRegistro()};
            tableModel.addRow(rowData);
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}