package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.ProductCategory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

    private String uploadedImagePath;
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
    
        String[] columnNames = {"ID", "Name", "Description", "Unit Price", "Stock", "CatId", "Category", "Image Path", "Is Active"};
    
        Function<Product, Object[]> rowMapper = product -> new Object[]{
            product.getProductId(),
            product.getName(),
            product.getDescription(),
            product.getUnitPrice(),
            product.getStock(),
            product.getCategory().getCategoryId(),
            product.getCategory().getCategory(),
            product.getImagePath(),
            product.isActive() // Include isActive value
        };
    
        products = controller.getProductService().getAllProducts();
        categories = controller.getProductService().getAllCategories();
    
        JPanel tablePanel = createTable(products, columnNames, rowMapper);
        panel.add(tablePanel, BorderLayout.CENTER);
    
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
            hideColumn(table, 8); // Hide the isActive column
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
        for (Product product : products) {
            System.out.println("Committing product: " + product.getProductId() + " isActive: " + product.isActive());
            if (product.getProductId() == 0) {
                controller.getProductService().addProduct(product); // Add new product
            } else {
                controller.getProductService().updateProduct(product); // Update product (including is_active status)
            }
        }
    
        // Disable the commit button
        commitButton.setEnabled(false);
    
        // Refresh the table data
        refreshTableData();
    }
    
    private void updateProductsInMemory(Object[][] data) {
        for (int i = 0; i < data.length; i++) {
            int productId = (int) data[i][ID_COLUMN];
            for (Product product : products) {
                if (product.getProductId() == productId) {
                    product.setName((String) data[i][1]);
                    product.setDescription((String) data[i][2]);
                    product.setUnitPrice(Float.parseFloat(data[i][3].toString()));
                    product.setStock(Integer.parseInt(data[i][4].toString()));
                    product.getCategory().setCategoryId(Integer.parseInt(data[i][5].toString()));
                    product.getCategory().setCategory((String) data[i][6]);
                    product.setImagePath((String) data[i][7]);
                    product.setActive(data[i][8] != null && Boolean.parseBoolean(data[i][8].toString())); // Update isActive value
                    System.out.println("Updated product in memory: " + product.getProductId() + " isActive: " + product.isActive());
                    break;
                }
            }
        }
    }
    
    private void refreshTableData() {
        products = controller.getProductService().getAllProducts();
        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        model.setRowCount(0); // Clear existing rows
    
        for (Product product : products) {
            System.out.println("Refreshing table data for product: " + product.getProductId() + " isActive: " + product.isActive());
            model.addRow(new Object[]{
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getStock(),
                product.getCategory().getCategoryId(),
                product.getCategory().getCategory(),
                product.getImagePath(),
                product.isActive() // Include isActive value
            });
        }
    }
    
    @Override
    protected void onDelete() {
        int row = getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the selected row?", "Delete Row", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            int productId = (int) getTable().getValueAt(row, ID_COLUMN);
            for (Product product : products) {
                if (product.getProductId() == productId) {
                    product.setActive(false); // Mark as inactive in memory
                    System.out.println("Marked product as inactive: " + product.getProductId() + " isActive: " + product.isActive());
                    break;
                }
            }
            updateTableModel(); // Update the table model directly
            commitButton.setEnabled(true); // Enable the commit button
        }
    }
    
    private void updateTableModel() {
        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        model.setRowCount(0); // Clear existing rows
    
        for (Product product : products) {
            if (product.isActive()) {
                System.out.println("Updating table model for product: " + product.getProductId() + " isActive: " + product.isActive());
                model.addRow(new Object[]{
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getUnitPrice(),
                    product.getStock(),
                    product.getCategory().getCategoryId(),
                    product.getCategory().getCategory(),
                    product.getImagePath(),
                    product.isActive() // Include isActive value
                });
            }
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
            updateTableData(row, columnCount, textFields, categoryComboBox);
            if (uploadedImagePath != null) {
                getTable().setValueAt(uploadedImagePath, row, IMAGE_PATH_COLUMN);
            }
            if (checkForChanges(beforeEditData)) {
                commitButton.setEnabled(true); // Enable the commit button
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
            } else {
                imageLabel.setText("No image path provided");
            }
        } else {
            imageLabel.setText("Invalid image path");
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
            if (col == CATEGORY_COLUMN) {
                gbc.gridx = 0;
                gbc.gridy = col;
                panel.add(new JLabel("Category:"), gbc);
                gbc.gridx = 1;
                panel.add(categoryComboBox, gbc);
            } else if (col == IMAGE_PATH_COLUMN) {
                gbc.gridx = 0;
                gbc.gridy = col;
                panel.add(new JLabel("Image Path:"), gbc);
                gbc.gridx = 1;
                panel.add(imageLabel, gbc);
            } else {
                textFields[col] = new JTextField(rowData[col].toString());
                gbc.gridx = 0;
                gbc.gridy = col;
                panel.add(new JLabel(getTable().getColumnName(col) + ":"), gbc);
                gbc.gridx = 1;
                panel.add(textFields[col], gbc);
            }
        }
    
        gbc.gridx = 0;
        gbc.gridy = columnCount;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(new JLabel("Image:"), gbc);
    
        // Create a JScrollPane to contain the imageLabel
        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
        imageScrollPane.setPreferredSize(new Dimension(400, 400)); // Initial size
    
        // Add a ComponentListener to adjust the image size when the window is resized
        imageScrollPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = imageScrollPane.getSize();
                ImageIcon icon = (ImageIcon) imageLabel.getIcon();
                if (icon != null) {
                    imageLabel.setIcon(new ImageIcon(getScaledImage(icon.getImage(), size.width, size.height)));
                }
            }
        });
    
        gbc.gridy = columnCount + 1;
        panel.add(imageScrollPane, gbc);
    
        // Add upload button
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(e -> uploadImage(imageLabel));
        gbc.gridy = columnCount + 2;
        panel.add(uploadButton, gbc);
    
        return panel;
    }

    private void uploadImage(JLabel imageLabel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Get the absolute path of the project directory
                String projectDir = new File("").getAbsolutePath();
                File destDir = new File(projectDir, "src/main/resources/images");
                if (!destDir.exists()) {
                    destDir.mkdirs(); // Create the directory if it does not exist
                }
                File destFile = new File(destDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                // Store the relative path
                uploadedImagePath = "images/" + selectedFile.getName();
                ImageIcon imageIcon = new ImageIcon(destFile.getPath());
                imageLabel.setIcon(new ImageIcon(getScaledImage(imageIcon.getImage(), 400, 400)));
            } catch (IOException ex) {
                ex.printStackTrace(); // Print stack trace for debugging
                JOptionPane.showMessageDialog(this, "Error uploading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void updateTableData(int row, int columnCount, JTextField[] textFields, JComboBox<ProductCategory> categoryComboBox) {
        for (int col = 0; col < columnCount; col++) {
            if (col == CATEGORY_COLUMN) {
                ProductCategory selectedCategory = (ProductCategory) categoryComboBox.getSelectedItem();
                getTable().setValueAt(selectedCategory.getCategory(), row, col);
                getTable().setValueAt(selectedCategory.getCategoryId(), row, CAT_ID_COLUMN);
            } else if (col != IMAGE_PATH_COLUMN) {
                getTable().setValueAt(textFields[col].getText(), row, col);
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
            products.add(newProduct); // Add to the in-memory list
            addProductToTable(newProduct); // Add to the table
            System.out.println("Added new product to memory: " + newProduct);
            commitButton.setEnabled(true); // Enable the commit button
        }
    }
    
    private void addProductToTable(Product product) {
        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        model.addRow(new Object[]{
            product.getProductId(),
            product.getName(),
            product.getDescription(),
            product.getUnitPrice(),
            product.getStock(),
            product.getCategory().getCategoryId(),
            product.getCategory().getCategory(),
            product.getImagePath(),
            product.isActive() // Include isActive value
        });
        products.add(product); // Add to the in-memory list
    }

    private Product createNewProduct(JTextField nameField, JTextField descriptionField, JTextField unitPriceField, JTextField stockField, JComboBox<ProductCategory> categoryComboBox) {
        String name = nameField.getText();
        String description = descriptionField.getText();
        float unitPrice = Float.parseFloat(unitPriceField.getText());
        int stock = Integer.parseInt(stockField.getText());
        ProductCategory selectedCategory = (ProductCategory) categoryComboBox.getSelectedItem();
    
        return new Product(0, name, description, unitPrice, stock, true, selectedCategory); // Default isActive to true for new products
    }
}