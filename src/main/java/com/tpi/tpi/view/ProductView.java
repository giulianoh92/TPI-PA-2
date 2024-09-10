package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.model.ProductCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

public class ProductView extends AbstractView<Product, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final int ID_COLUMN = 0;
    private static final int CAT_ID_COLUMN = 5;
    private static final int CATEGORY_COLUMN = 6;
    private static final Logger LOGGER = Logger.getLogger(ProductView.class.getName());

    private List<ProductCategory> categories;
    private List<Product> products;

    public ProductView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
    }

    @Override
    protected boolean isFieldEditable() {
        return true;
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
            product.getProductId(),
            product.getName(),
            product.getDescription(),
            product.getUnitPrice(),
            product.getStock(),
            product.getCategory().getCategoryId(),
            product.getCategory().getCategory()
        };

        products = controller.getProductService().getAllProducts();
        categories = controller.getProductService().getAllCategories();

        super.showPanel(products, columnNames, rowMapper);

        JTable table = getTable();
        if (table != null) {
            hideCategoryIdColumn(table);
        }
    }

    private void hideCategoryIdColumn(JTable table) {
        table.getColumnModel().getColumn(CAT_ID_COLUMN).setMinWidth(0);
        table.getColumnModel().getColumn(CAT_ID_COLUMN).setMaxWidth(0);
        table.getColumnModel().getColumn(CAT_ID_COLUMN).setPreferredWidth(0);
    }

    @Override
    public void handleCommit(Object[][] data) {
        LOGGER.info("Committing product data:");
        for (Product product : products) {
            LOGGER.info(product.toString());
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

        Object[][] beforeEditData = getCurrentTableData();

        LOGGER.info("Before Edit Data:");
        logTableData(beforeEditData);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields, categoryComboBox);

            boolean hasChanges = checkForChanges(beforeEditData);

            LOGGER.info("Data After Edit:");
            logCurrentTableData();

            if (hasChanges) {
                resetButton.setEnabled(true);
                commitButton.setEnabled(true);
            }
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
                .filter(cat -> cat.getCategoryId() == currentCategoryId)
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
                        product.setCategory(selectedCategory);
                        getTable().setValueAt(selectedCategory.getCategoryId(), row, CAT_ID_COLUMN);
                        getTable().setValueAt(selectedCategory.getCategory(), row, CATEGORY_COLUMN);
                    }
                } else {
                    if (textFields[col] != null) {
                        updateProductField(product, col, textFields[col].getText());
                        getTable().setValueAt(textFields[col].getText(), row, col);
                    }
                }
            }
        }
    }

    private void updateProductField(Product product, int col, String value) {
        switch (col) {
            case 1:
                product.setName(value);
                break;
            case 2:
                product.setDescription(value);
                break;
            case 3:
                product.setUnitPrice(Float.parseFloat(value));
                break;
            case 4:
                product.setStock(Integer.parseInt(value));
                break;
        }
    }

    @Override
    protected void onDelete() {
        int row = getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected row?", "Delete Row", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            Product productToDelete = products.get(row);
            controller.deleteProduct(productToDelete);
            products.remove(row);

            DefaultTableModel model = (DefaultTableModel) getTable().getModel();
            model.removeRow(row);

            resetButton.setEnabled(true);
            commitButton.setEnabled(true);
        }
    }

    @Override
    protected void onAdd() {
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField unitPriceField = new JTextField();
        JTextField stockField = new JTextField();
        JComboBox<ProductCategory> categoryComboBox = new JComboBox<>();

        JPanel panel = createAddPanel(nameField, descriptionField, unitPriceField, stockField, categoryComboBox);

        populateComboBox(categoryComboBox, categories);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Product newProduct = createNewProduct(nameField, descriptionField, unitPriceField, stockField, categoryComboBox);
                if (newProduct != null) {
                    controller.addProduct(newProduct);
                    products.add(newProduct);

                    DefaultTableModel model = (DefaultTableModel) getTable().getModel();
                    model.addRow(new Object[]{
                        newProduct.getProductId(),
                        newProduct.getName(),
                        newProduct.getDescription(),
                        newProduct.getUnitPrice(),
                        newProduct.getStock(),
                        newProduct.getCategory().getCategoryId(),
                        newProduct.getCategory().getCategory()
                    });

                    resetButton.setEnabled(true);
                    commitButton.setEnabled(true);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for Unit Price and Stock.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createAddPanel(JTextField nameField, JTextField descriptionField, JTextField unitPriceField, JTextField stockField, JComboBox<ProductCategory> categoryComboBox) {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Unit Price:"));
        panel.add(unitPriceField);
        panel.add(new JLabel("Stock:"));
        panel.add(stockField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryComboBox);
        return panel;
    }

    private Product createNewProduct(JTextField nameField, JTextField descriptionField, JTextField unitPriceField, JTextField stockField, JComboBox<ProductCategory> categoryComboBox) {
        String name = nameField.getText();
        String description = descriptionField.getText();
        float unitPrice = Float.parseFloat(unitPriceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        ProductCategory selectedCategory = (ProductCategory) categoryComboBox.getSelectedItem();

        if (selectedCategory != null) {
            return new Product(
                0,
                name,
                description,
                unitPrice,
                stock,
                true,
                selectedCategory
            );
        }
        return null;
    }

    private void logTableData(Object[][] data) {
        for (Object[] rowArray : data) {
            LOGGER.info(Arrays.toString(rowArray));
        }
    }
}