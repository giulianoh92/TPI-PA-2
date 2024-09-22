package com.tpi.tpi.desktop.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractView<T, C> extends JPanel {
    private static final Color BUTTON_BACKGROUND_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 12);
    private static final Logger LOGGER = Logger.getLogger(AbstractView.class.getName());

    protected C controller;
    protected JTable table;
    private Object[][] initialTableData;
    protected JButton commitButton;
    protected JButton resetButton;
    protected JButton editRowButton;
    protected JButton addRowButton;
    protected JButton deleteRowButton;
    private JTextField searchField;
    private List<JComboBox<String>> filterComboBoxes = new ArrayList<>(); // Ensure this is declared here

    private boolean commitMade = false;

    public AbstractView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    protected JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        resetButton = createStyledButton("Reset");
        commitButton = createStyledButton("Commit");
        editRowButton = createStyledButton("Edit Row");
        addRowButton = createStyledButton("Add Row");
        deleteRowButton = createStyledButton("Delete Row");

        resetButton.setEnabled(false);
        commitButton.setEnabled(false);

        resetButton.addActionListener(e -> onReset());
        commitButton.addActionListener(e -> onCommit());
        editRowButton.addActionListener(e -> onEditRow());
        addRowButton.addActionListener(e -> onAdd());
        deleteRowButton.addActionListener(e -> onDelete());

        buttonPanel.add(resetButton);
        buttonPanel.add(commitButton);
        buttonPanel.add(editRowButton);

        if (isFieldEditable()) {
            buttonPanel.add(addRowButton);
            buttonPanel.add(deleteRowButton);
        }

        return buttonPanel;
    }

    protected boolean shouldShowDefaultButtons() {
        return true;
    }

    protected boolean isFieldEditable() {
        return false;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_BACKGROUND_COLOR);
        button.setForeground(BUTTON_FOREGROUND_COLOR);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        return button;
    }

    public void setController(C controller) {
        this.controller = controller;
    }

    protected void onAdd() {
        // To be implemented by subclasses
    }

    protected void onDelete() {
        // To be implemented by subclasses
    }

    public void showPanel(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame(getFrameTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JScrollPane tableScrollPane = createTable(data, columnNames, rowMapper);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    protected JScrollPane createTable(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        initialTableData = storeInitialData(data, columnNames, rowMapper);
    
        Object[][] tableData = Arrays.copyOf(initialTableData, initialTableData.length);
        for (int i = 0; i < tableData.length; i++) {
            tableData[i] = rowMapper.apply(data.get(i));
        }
    
        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    
        table = new JTable(tableModel);
        styleTable(table);
    
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
    
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    onEditRow();
                }
            }
        });
    
        adjustColumnSizes();
    
        JScrollPane scrollPane = new JScrollPane(table);
        Dimension tablePreferredSize = table.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(tablePreferredSize.width + 20, tablePreferredSize.height + 20));
    
        // Add search and filter components
        JPanel searchPanel = createSearchPanel(columnNames);
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(searchPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);
    
        return new JScrollPane(containerPanel);
    }

    private JPanel createSearchPanel(String[] columnNames) {
        searchField = new JTextField();
        searchField.setToolTipText("Search...");
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });
        
        JPanel filterPanel = new JPanel(new BorderLayout());
        JComboBox<String> comboBox = new JComboBox<>();
        filterComboBoxes.add(comboBox);
        filterComboBoxes.get(0).addItem("All");
        
        // Populate combo box with unique values from the "Category" column
        final int categoryColumnIndex = getCategoryColumnIndex(columnNames);
        
        if (categoryColumnIndex != -1) {
            List<String> uniqueValues = Arrays.stream(initialTableData)
                    .map(row -> row[categoryColumnIndex] != null ? row[categoryColumnIndex].toString() : "")
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            for (String value : uniqueValues) {
                filterComboBoxes.get(0).addItem(value);
            }
        
            filterComboBoxes.get(0).addActionListener(e -> filterTable());
            filterPanel.add(filterComboBoxes.get(0), BorderLayout.EAST);
        }
    
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);
    
        return searchPanel;
    }

    private int getCategoryColumnIndex(String[] columnNames) {
        for (int i = 0; i < columnNames.length; i++) {
            if ("Category".equals(columnNames[i])) {
                return i;
            }
        }
        return -1;
    }

    private void filterTable() {
        RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
        if (rowSorter instanceof TableRowSorter) {
            TableRowSorter<? extends TableModel> sorter = (TableRowSorter<? extends TableModel>) rowSorter;
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
    
            if (filterComboBoxes.get(0).getSelectedIndex() > 0) {
                filters.add(RowFilter.regexFilter(filterComboBoxes.get(0).getSelectedItem().toString(), table.getColumnModel().getColumnIndex("Category")));
            }
            if (!searchField.getText().trim().isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchField.getText().trim()));
            }
    
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    private void adjustColumnSizes() {
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn column = table.getColumnModel().getColumn(col);
            int width = 15; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            column.setPreferredWidth(width);
        }
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
        table.setSelectionBackground(BUTTON_BACKGROUND_COLOR);
        table.setSelectionForeground(BUTTON_FOREGROUND_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 12));
        header.setBackground(BUTTON_BACKGROUND_COLOR);
        header.setForeground(BUTTON_FOREGROUND_COLOR);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private Object[][] storeInitialData(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        Object[][] dataCopy = new Object[data.size()][columnNames.length];
        for (int i = 0; i < data.size(); i++) {
            dataCopy[i] = rowMapper.apply(data.get(i));
        }
        return dataCopy;
    }

    protected abstract String getFrameTitle();

    protected void onCommit() {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();

        Object[][] currentData = new Object[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                currentData[row][col] = table.getValueAt(row, col);
            }
        }

        handleCommit(currentData);
        commitMade = true;
    }

    protected abstract void handleCommit(Object[][] data);

    protected void onReset() {
        if (initialTableData == null) {
            return;
        }

        LOGGER.info("Resetting table values to initial state");

        int currentRowCount = table.getRowCount();
        int initialRowCount = initialTableData.length;
        int columnCount = table.getColumnCount();

        adjustTableRowCount(currentRowCount, initialRowCount, columnCount);

        updateTableValues(initialRowCount, columnCount);

        if (checkForChanges(initialTableData) || commitMade) {
            resetButton.setEnabled(false);
            commitButton.setEnabled(false);
        } else {
            resetButton.setEnabled(true);
            commitButton.setEnabled(true);
        }
    }

    private void adjustTableRowCount(int currentRowCount, int initialRowCount, int columnCount) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (currentRowCount < initialRowCount) {
            for (int i = currentRowCount; i < initialRowCount; i++) {
                model.addRow(new Object[columnCount]);
            }
        } else if (currentRowCount > initialRowCount) {
            for (int i = currentRowCount - 1; i >= initialRowCount; i--) {
                model.removeRow(i);
            }
        }
    }

    private void updateTableValues(int initialRowCount, int columnCount) {
        for (int row = 0; row < initialRowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                table.setValueAt(initialTableData[row][col], row, col);
            }
        }
    }

    protected void onEditRow() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int columnCount = table.getColumnCount();
        Object[] rowData = new Object[columnCount];
        for (int col = 0; col < columnCount; col++) {
            rowData[col] = table.getValueAt(row, col);
        }

        JTextField[] textFields = createTextFields(columnCount, rowData);

        Object[][] beforeEditData = getCurrentTableData();

        int result = JOptionPane.showConfirmDialog(this, createEditPanel(columnCount, textFields), "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields);

            boolean hasChanges = checkForChanges(beforeEditData);

            if (hasChanges) {
                resetButton.setEnabled(true);
                commitButton.setEnabled(true);
            }
        }
    }

    private JTextField[] createTextFields(int columnCount, Object[] rowData) {
        JTextField[] textFields = new JTextField[columnCount];
        for (int col = 0; col < columnCount; col++) {
            textFields[col] = new JTextField(rowData[col] != null ? rowData[col].toString() : "");
            if (isNonEditableColumn(col)) {
                textFields[col].setEditable(false);
            }
        }
        return textFields;
    }

    private JPanel createEditPanel(int columnCount, JTextField[] textFields) {
        JPanel panel = new JPanel(new GridLayout(columnCount, 2));
        for (int col = 0; col < columnCount; col++) {
            panel.add(new JLabel(table.getColumnName(col)));
            panel.add(textFields[col]);
        }
        return panel;
    }

    private boolean isNonEditableColumn(int col) {
        String columnName = table.getColumnName(col);
        return col == 0 || "Registered At".equals(columnName) || "Date".equals(columnName) || "Total".equals(columnName) || "Payment Method".equals(columnName) || "Username".equals(columnName) || "Password".equals(columnName) || "Email".equals(columnName) || "Address".equals(columnName);
    }

    protected Object[][] getCurrentTableData() {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                data[i][j] = table.getValueAt(i, j);
            }
        }
        return data;
    }

    private void updateTableData(int row, int columnCount, JTextField[] textFields) {
        for (int col = 0; col < columnCount; col++) {
            table.setValueAt(textFields[col].getText(), row, col);
        }
    }

    public boolean checkForChanges(Object[][] beforeEditData) {
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if (!beforeEditData[i][j].toString().equals(table.getValueAt(i, j).toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    protected <E> void populateComboBox(JComboBox<E> comboBox, List<E> items) {
        for (E item : items) {
            comboBox.addItem(item);
        }
    }

    protected <E> void selectCurrentItem(JComboBox<E> comboBox, E currentItem) {
        comboBox.setSelectedItem(currentItem);
    }
}