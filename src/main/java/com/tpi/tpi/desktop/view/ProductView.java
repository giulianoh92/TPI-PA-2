package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.Product;
import com.tpi.tpi.common.model.ProductCategory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

public class ProductView extends AbstractView<Product, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final int ID_COLUMN = 0;
    private static final int CAT_ID_COLUMN = 5;
    private static final int CATEGORY_COLUMN = 6;
    private static final int IMAGE_PATH_COLUMN = 7;
    private static final int IS_ACTIVE_COLUMN = 8;

    private final static Logger LOGGER = Logger.getLogger(ProductView.class.getName());


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
            product.isActive()
        };

        products = controller.getProductService().getAllProducts();
        categories = controller.getProductService().getAllCategories();

        JPanel tablePanel = createTable(products, columnNames, rowMapper);
        panel.add(tablePanel, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        JTable table = getTable();
        if (table != null) {
            configureTableSorter(table);
            hideColumn(table, CAT_ID_COLUMN);
            hideColumn(table, IMAGE_PATH_COLUMN);
            hideColumn(table, IS_ACTIVE_COLUMN);
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
        updateProductsInMemory(data);
        commitToDatabase();
        commitButton.setEnabled(false);
        refreshTableData();
    }

    private void updateProductsInMemory(Object[][] data) {
        for (Object[] row : data) {
            int productId;
            if (row[ID_COLUMN] instanceof String) productId = Integer.parseInt((String) row[ID_COLUMN]);
            else productId = (int) row[ID_COLUMN];
            for (Product product : products) {
                if (product.getProductId() == productId) {
                    updateProductFromRow(product, row);
                    break;
                }
            }
        }
    }

    private void updateProductFromRow(Product product, Object[] row) {
        product.setName((String) row[1]);
        product.setDescription((String) row[2]);
        product.setUnitPrice(Float.parseFloat(row[3].toString()));
        product.setStock(Integer.parseInt(row[4].toString()));
        product.getCategory().setCategoryId(Integer.parseInt(row[5].toString()));
        product.getCategory().setCategory((String) row[6]);
        product.setImagePath((String) row[7]);
        product.setActive(row[8] != null && Boolean.parseBoolean(row[8].toString()));
    }

    private void commitToDatabase() {
        for (Product product : products) {
            if (product.getProductId() == 0) {
                controller.getProductService().addProduct(product);
            } else {
                controller.getProductService().updateProduct(product);
            }
        }
    }

    private void refreshTableData() {
        products = controller.getProductService().getAllProducts();
        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        model.setRowCount(0);

        for (Product product : products) {
            model.addRow(new Object[]{
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                product.getStock(),
                product.getCategory().getCategoryId(),
                product.getCategory().getCategory(),
                product.getImagePath(),
                product.isActive()
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
            markProductAsInactive(row);
            updateTableModel();
            commitButton.setEnabled(true);
        }
    }

    private void markProductAsInactive(int row) {
        int productId = (int) getTable().getValueAt(row, ID_COLUMN);
        for (Product product : products) {
            if (product.getProductId() == productId) {
                product.setActive(false);
                break;
            }
        }
    }

    private void updateTableModel() {
        DefaultTableModel model = (DefaultTableModel) getTable().getModel();
        model.setRowCount(0);

        for (Product product : products) {
            if (product.isActive()) {
                model.addRow(new Object[]{
                    product.getProductId(),
                    product.getName(),
                    product.getDescription(),
                    product.getUnitPrice(),
                    product.getStock(),
                    product.getCategory().getCategoryId(),
                    product.getCategory().getCategory(),
                    product.getImagePath(),
                    product.isActive()
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
        loadImage(row, imageLabel);

        Object[][] beforeEditData = getCurrentTableData();

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields, categoryComboBox);
            if (uploadedImagePath != null) {
                getTable().setValueAt(uploadedImagePath, row, IMAGE_PATH_COLUMN);
            }
            if (checkForChanges(beforeEditData)) {
                commitButton.setEnabled(true);
            }
        }
    }

    private void loadImage(int row, JLabel imageLabel) {
        Object imagePathObj = getTable().getValueAt(row, IMAGE_PATH_COLUMN);
        System.out.println("Image path: " + imagePathObj);
        
        if (imagePathObj instanceof String) {
            String imagePath = (String) imagePathObj;
            if (imagePath != null && !imagePath.isEmpty()) {
                java.net.URL imgURL = getClass().getClassLoader().getResource(imagePath);
                if (imgURL != null) {
                    imageLabel.setIcon(new ImageIcon(getScaledImage(new ImageIcon(imgURL).getImage(), 400, 400)));
                } else {
                    File imgFile = new File("src/main/resources/static/images/" + imagePath);
                    if (imgFile.exists()) {
                        imageLabel.setIcon(new ImageIcon(getScaledImage(new ImageIcon(imgFile.getAbsolutePath()).getImage(), 400, 400)));
                    } else {
                        imageLabel.setText("Image not found");
                    }
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
                addCategoryField(panel, gbc, categoryComboBox);
            } else if (col == IMAGE_PATH_COLUMN || col == IS_ACTIVE_COLUMN || col == CAT_ID_COLUMN) {
                // Skip adding these fields to the panel
                continue;
            } else {
                addTextField(panel, gbc, col, rowData, textFields);
            }
        }
    
        // Set the ID column text field to uneditable
        if (textFields[ID_COLUMN] != null) {
            textFields[ID_COLUMN].setEditable(false);
        }
    
        addImageUploadButton(panel, gbc, imageLabel, columnCount);
    
        return panel;
    }

    private void addCategoryField(JPanel panel, GridBagConstraints gbc, JComboBox<ProductCategory> categoryComboBox) {
        gbc.gridx = 0;
        gbc.gridy = CATEGORY_COLUMN;
        panel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryComboBox, gbc);
    }

    private void addTextField(JPanel panel, GridBagConstraints gbc, int col, Object[] rowData, JTextField[] textFields) {
        gbc.gridx = 0;
        gbc.gridy = col;
        panel.add(new JLabel(getTable().getColumnName(col)), gbc);
        textFields[col] = new JTextField(rowData[col].toString());
        gbc.gridx = 1;
        panel.add(textFields[col], gbc);
    }

    private void addImageUploadButton(JPanel panel, GridBagConstraints gbc, JLabel imageLabel, int columnCount) {
        gbc.gridx = 0;
        gbc.gridy = columnCount;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(new JLabel("Image:"), gbc);

        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
        imageScrollPane.setPreferredSize(new Dimension(400, 400));
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

        JButton uploadButton = new JButton("Upload Image");
        uploadButton.addActionListener(e -> uploadImage(imageLabel, getTable().getSelectedRow()));
        gbc.gridy = columnCount + 2;
        panel.add(uploadButton, gbc);
    }

    private void uploadImage(JLabel imageLabel, int row) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String projectDir = new File("").getAbsolutePath();
                File destDir = new File(projectDir, "src/main/resources/static/images/");
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                File destFile = new File(destDir, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                ImageIcon imageIcon = new ImageIcon(destFile.getPath());
                imageLabel.setIcon(new ImageIcon(getScaledImage(imageIcon.getImage(), 400, 400)));
                table.setValueAt(selectedFile.getName(), row, IMAGE_PATH_COLUMN);
            } catch (IOException ex) {
                ex.printStackTrace();
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
            } else if (col != IS_ACTIVE_COLUMN && textFields[col] != null) {
                getTable().setValueAt(textFields[col].getText(), row, col);
            }

            LOGGER.info("Updated  Product ID: " + getTable().getValueAt(row, ID_COLUMN) + " Column: " + col + " Value: " + getTable().getValueAt(row, col));
        }
    }

    protected Object[] getRowData(int row, int columnCount) {
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
        products.forEach(product -> product.setActive(true));
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
            products.add(newProduct);
            addProductToTable(newProduct);
            commitButton.setEnabled(true);
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
            product.isActive()
        });
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