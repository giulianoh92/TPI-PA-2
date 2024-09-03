package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Product;

import java.util.List;
import java.util.function.Function;

public class ProductView extends AbstractView<Product, AdminOperationsController> implements PanelView<AdminOperationsController> {

    @Override
    protected String getFrameTitle() {
        return "Product Management";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller

        String[] columnNames = {"ID", "Name", "Description", "Unit Price", "Stock"};
        Function<Product, Object[]> rowMapper = product -> new Object[]{
            product.getIdProducto(),
            product.getNombre(),
            product.getDescripcion(),
            product.getPrecioUnitario(),
            product.getStock()
        };
        List<Product> products = controller.getProductService().getAllProducts();
        super.showPanel(products, columnNames, rowMapper);
    }
}