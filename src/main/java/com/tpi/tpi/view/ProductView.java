package com.tpi.tpi.view;

import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.ProductCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductView {

    public void showProductDetails(List<Product> productList) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame("Product List");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        String[] columnNames = {"ID", "Nombre", "Descripcion", "Precio Unitario", "Stock", "Categoria"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 10, 780, 580);
        panel.add(scrollPane);

        for (Product product : productList) {
            Object[] rowData = {product.getIdProducto(), product.getNombre(), product.getDescripcion(), product.getPrecioUnitario(), product.getStock(), product.getCategoria().getNombre()};
            tableModel.addRow(rowData);
        }

        frame.add(panel);
        frame.setVisible(true);
    }
}