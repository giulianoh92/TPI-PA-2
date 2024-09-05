package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.ProductCategory;
import com.tpi.tpi.view.AbstractView; // Corrected import statement

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class ProductView extends AbstractView<Product, AdminOperationsController> implements PanelView<AdminOperationsController> {
    
    public ProductView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
    }
    
    @Override
    protected String getFrameTitle() {
        return "Product Management";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller

        String[] columnNames = {"ID", "Name", "Description", "Unit Price", "Stock", "CatId", "Category"};
   
        Function<Product, Object[]> rowMapper = product -> new Object[]{
            product.getIdProducto(),
            product.getNombre(),
            product.getDescripcion(),
            product.getPrecioUnitario(),
            product.getStock(),
            product.getCategoria().getIdCategoria(),
            product.getCategoria().getNombre()
        };
        
        List<Product> products = controller.getProductService().getAllProducts();
        super.showPanel(products, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        // Specific commit logic for ProductView
        System.out.println("Committing product data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }

        // Example: Call a method on the controller to handle the commit
        controller.commitProductData(data);
    }
}