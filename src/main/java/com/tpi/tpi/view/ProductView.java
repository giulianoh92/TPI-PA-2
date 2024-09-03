package com.tpi.tpi.view;

import com.tpi.tpi.model.Product;
import com.tpi.tpi.service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductView implements PanelView<ProductService> {

    // Method to show the details of the product list
    @Override
    public void showPanel(ProductService productService) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame("Product Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Add the table to the panel
        JScrollPane tableScrollPane = createTable(productService.getAllProducts());
        panel.add(tableScrollPane, BorderLayout.CENTER);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    // Method to create a table inside a frame, left side, with column names and data
    private JScrollPane createTable(List<Product> products) {
        String[] columnNames = {"ID", "Name", "Description", "Unit Price", "Stock"};
        Object[][] data = new Object[products.size()][5];
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = product.getIdProducto();
            data[i][1] = product.getNombre();
            data[i][2] = product.getDescripcion();
            data[i][3] = product.getPrecioUnitario();
            data[i][4] = product.getStock();
        }

        JTable table = new JTable(data, columnNames);
        return new JScrollPane(table);
    }
}