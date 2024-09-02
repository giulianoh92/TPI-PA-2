package com.tpi.tpi.view;

import com.tpi.tpi.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductView implements TableView {

    // Method to show the details of the product list
    @Override
    public void showDetails(List<?> productList) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame("Product List");
        frame.setSize(800, 600); // Set fixed size
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        String[] columnNames = {"ID", "Nombre", "Descripcion", "Precio Unitario", "Stock", "Categoria"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 780, 580);
        panel.add(scrollPane);

        // Populate the table with product data
        for (Product product : (List<Product>) productList) {
            Object[] rowData = {product.getIdProducto(), product.getNombre(), product.getDescripcion(), product.getPrecioUnitario(), product.getStock(), product.getCategoria().getNombre()};
            tableModel.addRow(rowData);
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}