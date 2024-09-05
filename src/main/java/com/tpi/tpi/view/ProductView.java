package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.ProductCategory;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class ProductView extends AbstractView<Product, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final int ID_COLUMN = 0;
    private static final int CAT_ID_COLUMN = 5;
    private static final int CATEGORY_COLUMN = 6;

    private List<ProductCategory> categories;
    private List<Product> products;

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
        setController(controller);

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

        products = controller.getProductService().getAllProducts();
        categories = controller.getProductService().getAllCategories();

        super.showPanel(products, columnNames, rowMapper);

        JTable table = getTable();
        if (table != null) {
            table.getColumnModel().getColumn(CAT_ID_COLUMN).setMinWidth(0);
            table.getColumnModel().getColumn(CAT_ID_COLUMN).setMaxWidth(0);
            table.getColumnModel().getColumn(CAT_ID_COLUMN).setPreferredWidth(0);
        }
    }

    @Override
    public void handleCommit(Object[][] data) {
        System.out.println("Committing product data:");
        for (Product product : products) {
            System.out.println(product);
        }

        controller.commitProductData(products);
    }

    @Override
    protected void onEditRow() {
        int row = getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int columnCount = getTable().getColumnCount();
        Object[] rowData = getRowData(row, columnCount);
        JTextField[] textFields = new JTextField[columnCount];
        JComboBox<ProductCategory> categoryComboBox = new JComboBox<>();
        JPanel panel = createEditPanel(columnCount, rowData, textFields, categoryComboBox);

        populateComboBox(categoryComboBox, categories);
        selectCurrentCategory(row, categoryComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields, categoryComboBox);
        }
    }

    private Object[] getRowData(int row, int columnCount) {
        Object[] rowData = new Object[columnCount];
        for (int col = 0; col < columnCount; col++) {
            rowData[col] = getTable().getValueAt(row, col);
        }
        return rowData;
    }

    private JPanel createEditPanel(int columnCount, Object[] rowData, JTextField[] textFields, JComboBox<ProductCategory> categoryComboBox) {
        JPanel panel = new JPanel(new GridLayout(columnCount, 2));
        for (int col = 0; col < columnCount; col++) {
            if (col == CAT_ID_COLUMN) {
                continue;
            }

            panel.add(new JLabel(getTable().getColumnName(col)));
            if (col == CATEGORY_COLUMN) {
                panel.add(categoryComboBox);
            } else {
                textFields[col] = new JTextField(rowData[col] != null ? rowData[col].toString() : "");
                if (col == ID_COLUMN) {
                    textFields[col].setEditable(false);
                }
                panel.add(textFields[col]);
            }
        }
        return panel;
    }

    private void selectCurrentCategory(int row, JComboBox<ProductCategory> categoryComboBox) {
        int currentCategoryId = (Integer) getTable().getValueAt(row, CAT_ID_COLUMN);
        ProductCategory currentCategory = categories.stream()
                .filter(cat -> cat.getIdCategoria() == currentCategoryId)
                .findFirst()
                .orElse(null);
        selectCurrentItem(categoryComboBox, currentCategory);
    }

    private void updateTableData(int row, int columnCount, JTextField[] textFields, JComboBox<ProductCategory> categoryComboBox) {
        Product product = products.get(row);
        for (int col = 0; col < columnCount; col++) {
            if (col != ID_COLUMN && col != CAT_ID_COLUMN) {
                if (col == CATEGORY_COLUMN) {
                    ProductCategory selectedCategory = (ProductCategory) categoryComboBox.getSelectedItem();
                    if (selectedCategory != null) {
                        product.setCategoria(selectedCategory);
                        getTable().setValueAt(selectedCategory.getIdCategoria(), row, CAT_ID_COLUMN);
                        getTable().setValueAt(selectedCategory.getNombre(), row, CATEGORY_COLUMN);
                    }
                } else {
                    if (textFields[col] != null) {
                        switch (col) {
                            case 1:
                                product.setNombre(textFields[col].getText());
                                break;
                            case 2:
                                product.setDescripcion(textFields[col].getText());
                                break;
                            case 3:
                                product.setPrecioUnitario(Float.parseFloat(textFields[col].getText()));
                                break;
                            case 4:
                                product.setStock(Integer.parseInt(textFields[col].getText()));
                                break;
                        }
                        getTable().setValueAt(textFields[col].getText(), row, col);
                    }
                }
            }
        }
    }
}