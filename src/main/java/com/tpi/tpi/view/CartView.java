package com.tpi.tpi.view;

import com.tpi.tpi.model.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CartView implements TableView {

    // Method to show the details of the product list
    @Override
    public void showDetails(List<?> items) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame("Items List");
        frame.setSize(800, 600); // Set fixed size
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        String[] columnNames = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 780, 580);
        panel.add(scrollPane);

        // Populate the table with product data
        for (Item item : (List<Item>) items) {
            Object[] rowData = {item.getProducto().getNombre(), item.getCantidad(), item.getProducto().getPrecioUnitario(), item.getCantidad() * item.getProducto().getPrecioUnitario()};
            tableModel.addRow(rowData);
        }

        frame.add(panel);
        frame.setVisible(true);
    }

    
    // Method to get the cart id
    public static int getCartId() {
        String id = JOptionPane.showInputDialog("Enter the cart ID:");
        return Integer.parseInt(id);
    }
}