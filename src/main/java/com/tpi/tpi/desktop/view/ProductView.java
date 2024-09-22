package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.ProductCategory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ProductView extends AbstractView<Product, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final int ID_COLUMN = 0;
    private static final int CAT_ID_COLUMN = 5;
    private static final int CATEGORY_COLUMN = 6;
    private static final int IMAGE_PATH_COLUMN = 7;

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
        // This method can remain empty or call the other showPanel method with a default panel
    }

    @Override
    public void showPanel(AdminOperationsController controller, JPanel panel) {
        setController(controller);

        String[] columnNames = {"ID", "Name", "Description", "Unit Price", "Stock", "CatId", "Category", "Image Path"};

        Function<Product, Object[]> rowMapper = product -> new Object[]{
            product.getProductId(),
            product.getName(),
            product.getDescription(),
            product.getUnitPrice(),
            product.getStock(),
            product.getCategory().getCategoryId(),
            product.getCategory().getCategory(),
            product.getImagePath() // Assuming getImagePath() method exists in Product class
        };

        products = controller.getProductService().getAllProducts();
        categories = controller.getProductService().getAllCategories();

        JScrollPane tableScrollPane = createTable(products, columnNames, rowMapper);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Add the button panel
        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        JTable table = getTable();
        if (table != null) {
            configureTableSorter(table);
            hideColumn(table, CAT_ID_COLUMN);
            hideColumn(table, IMAGE_PATH_COLUMN);
        }
    }

    private void configureTableSorter(JTable table) {
        RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
        if (rowSorter instanceof TableRowSorter) {
            TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) rowSorter;
            sorter.setComparator(ID_COLUMN, Comparator.comparingInt(o -> Integer.parseInt(o.toString())));
        }
    }

    private void hideColumn(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }

    @Override
    public void handleCommit(Object[][] data) {
        // Log the data values to the terminal
        System.out.println("Logging data values:");
        for (Object[] row : data) {
            for (Object cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

        // Update the product list in memory
        updateProductsInMemory(data);

        // Commit to the database
        controller.commitProductData(products);

        // Disable the commit button
        commitButton.setEnabled(false);

        // Refresh the table data
        refreshTableData();
    }

    private void updateProductsInMemory(Object[][] data) {
        for (Product product : products) {
            int index = products.indexOf(product);
            if (index < data.length) {
                try {
                    product.setProductId(Integer.parseInt(data[index][ID_COLUMN].toString()));
                    product.setName(data[index][1].toString());
                    product.setDescription(data[index][2].toString());
                    product.setUnitPrice(Float.parseFloat(data[index][3].toString()));
                    product.setStock(Integer.parseInt(data[index][4].toString()));

                    // Update the ProductCategory object
                    int categoryId = Integer.parseInt(data[index][CAT_ID_COLUMN].toString());
                    String categoryName = data[index][CATEGORY_COLUMN].toString();
                    ProductCategory category = product.getCategory();
                    category.setCategoryId(categoryId);
                    category.setCategory(categoryName);

                    // Handle the imagePath if it is editable
                    // product.setImagePath(data[index][IMAGE_PATH_COLUMN].toString());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Error in data format: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    return; // Exit the method in case of error
                }
            }
        }
    }

    private void refreshTableData() {
        products = controller.getProductService().getAllProducts();
        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        model.setRowCount(0); // Clear existing rows

        for (Product product : products) {
            model.addRow(new Object[]{
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getStock(),
                product.getCategory().getCategoryId(),
                product.getCategory().getCategory()
            });
        }
    }

    @Override
    protected void onEditRow() {
        int row = getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int columnCount = getTable().getColumnCount();
        Object[] rowData = getRowData(row, columnCount);
        JTextField[] textFields = new JTextField[columnCount];
        JComboBox<ProductCategory> categoryComboBox = new JComboBox<>();
        JLabel imageLabel = new JLabel();
        JPanel panel = createEditPanel(columnCount, rowData, textFields, categoryComboBox, imageLabel);

        populateComboBox(categoryComboBox, categories);
        selectCurrentCategory(row, categoryComboBox);

        // Load and display the image
        loadImage(row, imageLabel);

        Object[][] beforeEditData = getCurrentTableData();

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            // Update the product in memory
            updateTableData(row, columnCount, textFields, categoryComboBox);
            // Only reflect changes in the table if any data was modified
            boolean hasChanges = checkForChanges(beforeEditData);

            if (hasChanges) {
                resetButton.setEnabled(true);
                commitButton.setEnabled(true);
            }
        }
    }

    private void loadImage(int row, JLabel imageLabel) {
        Object imagePathObj = getTable().getValueAt(row, IMAGE_PATH_COLUMN);
        if (imagePathObj instanceof String) {
            String imagePath = (String) imagePathObj;
            if (imagePath != null && !imagePath.isEmpty()) {
                java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);
                if (imgURL != null) {
                    ImageIcon imageIcon = new ImageIcon(imgURL);
                    int width = imageIcon.getIconWidth();
                    int height = imageIcon.getIconHeight();
                    if (width > 0 && height > 0) {
                        Image scaledImage = getScaledImage(imageIcon.getImage(), width, height);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                    } else {
                        imageLabel.setText("Invalid image dimensions");
                    }
                } else {
                    imageLabel.setText("Image not found");
                }
            }
        }
    }
    
    private Image getScaledImage(Image srcImg, int w, int h) {
        if (w <= 0 || h <= 0) {
            throw new IllegalArgumentException("Width (" + w + ") and height (" + h + ") cannot be <= 0");
        }
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();
    
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
    
        return resizedImg;
    }

    private JPanel createEditPanel(int columnCount, Object[] rowData, JTextField[] textFields, JComboBox<ProductCategory> categoryComboBox, JLabel imageLabel) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int col = 0; col < columnCount; col++) {
            if (col == CAT_ID_COLUMN) {
                continue; // Skip the CatId column
            }

            gbc.gridx = 0;
            gbc.gridy = col;
            gbc.weightx = 0.1;
            JLabel label = new JLabel(getTable().getColumnName(col) + ":");
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.9;
            if (col == CATEGORY_COLUMN) {
                panel.add(categoryComboBox, gbc);
            } else {
                JTextField textField = new JTextField(rowData[col] != null ? rowData[col].toString() : "");
                textFields[col] = textField;
                panel.add(textField, gbc);
                if (col == IMAGE_PATH_COLUMN) {
                    label.setVisible(false);
                    textField.setVisible(false);
                    textField.setEditable(false);
                }
            }
        }

        gbc.gridx = 0;
        gbc.gridy = columnCount;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(new JLabel("Image:"), gbc);

        // Set a fixed size for the image label
        imageLabel.setPreferredSize(new Dimension(200, 200)); // Adjust the size as needed
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);

        gbc.gridy = columnCount + 1;
        panel.add(imageLabel, gbc);

        return panel;
    }

    private void updateTableData(int row, int columnCount, JTextField[] textFields, JComboBox<ProductCategory> categoryComboBox) {
        Product product = products.get(row);
        for (int col = 0; col < columnCount; col++) {
            if (col != ID_COLUMN) {
                if (col == CATEGORY_COLUMN) {
                    ProductCategory selectedCategory = (ProductCategory) categoryComboBox.getSelectedItem();
                    if (selectedCategory != null) {
                        product.setCategory(selectedCategory);
                        getTable().setValueAt(selectedCategory.getCategory(), row, CATEGORY_COLUMN);
                        getTable().setValueAt(selectedCategory.getCategoryId(), row, CAT_ID_COLUMN); // Update CatId column
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
            case IMAGE_PATH_COLUMN:
                product.setImagePath(value);
                break;
        }
    }

    private Object[] getRowData(int row, int columnCount) {
        Object[] rowData = new Object[columnCount];
        for (int col = 0; col < columnCount; col++) {
            rowData[col] = getTable().getValueAt(row, col);
        }
        return rowData;
    }

    private void selectCurrentCategory(int row, JComboBox<ProductCategory> categoryComboBox) {
        int currentCategoryId = (Integer) getTable().getValueAt(row, CAT_ID_COLUMN);
        ProductCategory currentCategory = categories.stream()
                .filter(cat -> cat.getCategoryId() == currentCategoryId)
                .findFirst()
                .orElse(null);
        selectCurrentItem(categoryComboBox, currentCategory);
    }

    @Override
    protected void onReset() {
        // Set active to true for all products
        products.forEach(product -> product.setActive(true));

        // Call the superclass method to reset the table data
        super.onReset();
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
            Product product = products.get(row);
            product.setActive(false);
            controller.getProductService().updateProduct(product);
            refreshTableData();
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
            Product newProduct = createNewProduct(nameField, descriptionField, unitPriceField, stockField, categoryComboBox);
            controller.getProductService().addProduct(newProduct);
            refreshTableData();
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

        return new Product(0, name, description, unitPrice, stock, true, selectedCategory);
    }
}