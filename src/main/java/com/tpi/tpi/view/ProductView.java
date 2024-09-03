package com.tpi.tpi.view;

import com.tpi.tpi.model.Product;
import com.tpi.tpi.service.ProductService;

import java.util.List;
import java.util.function.Function;

public class ProductView extends AbstractView<Product, ProductService> implements PanelView<ProductService> {

    @Override
    protected String getFrameTitle() {
        return "Product Management";
    }

    @Override
    public void showPanel(ProductService productService) {
        String[] columnNames = {"ID", "Name", "Description", "Unit Price", "Stock"};
        Function<Product, Object[]> rowMapper = product -> new Object[]{
            product.getIdProducto(),
            product.getNombre(),
            product.getDescripcion(),
            product.getPrecioUnitario(),
            product.getStock()
        };
        super.showPanel(productService, ProductService::getAllProducts, columnNames, rowMapper);
    }
}