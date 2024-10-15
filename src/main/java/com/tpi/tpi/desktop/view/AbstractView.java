package com.tpi.tpi.desktop.view;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
public abstract class AbstractView<T, C> extends JPanel {
    protected static final Color BUTTON_BACKGROUND_COLOR = new Color(70, 130, 180);
    protected static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;
    protected static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 12);

    protected C controller;
    protected JTable table;
    private Object[][] initialTableData;
    protected JButton commitButton;
    protected JButton resetButton;
    protected JButton editRowButton;
    protected JButton addRowButton;
    protected JButton deleteRowButton;
    private JTextField searchField;

    public AbstractView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        if (shouldShowDefaultButtons()) {
            add(createButtonPanel(), BorderLayout.SOUTH);
        }
    }

    protected JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        resetButton = createStyledButton("Reestablecer");
        commitButton = createStyledButton("Guardar");
        editRowButton = createStyledButton("Editar Fila");
        addRowButton = createStyledButton("Agregar Fila");
        deleteRowButton = createStyledButton("Eliminar Fila");

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

    protected JButton createStyledButton(String text) {
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
        panel.add(createTable(data, columnNames, rowMapper), BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            panel.add(createButtonPanel(), BorderLayout.SOUTH);
        }

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        // Add a component listener to adjust column sizes when the frame is resized
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustColumnSizes();
            }
        });
    }

    protected JPanel createTable(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        initialTableData = storeInitialData(data, columnNames, rowMapper);
        Object[][] tableData = Arrays.copyOf(initialTableData, initialTableData.length);

        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells uneditable
            }
        };

        tableModel.addTableModelListener(e -> {
            if (checkForChanges(initialTableData)) {
                resetButton.setEnabled(true);
            } else {
                resetButton.setEnabled(false);
            }
        });

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

        JScrollPane scrollPane = new JScrollPane(table);
        Dimension tablePreferredSize = table.getPreferredSize();
        scrollPane.setPreferredSize(new Dimension(tablePreferredSize.width + 20, tablePreferredSize.height + 20));

        JPanel searchPanel = createSearchPanel(columnNames);
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.add(searchPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        return containerPanel;
    }

    private JPanel createSearchPanel(String[] columnNames) {
        searchField = new JTextField();
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(new JLabel("Buscar: "), BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        return searchPanel;
    }

    private void filterTable() {
        RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
        if (rowSorter instanceof TableRowSorter) {
            @SuppressWarnings("unchecked")
            TableRowSorter<TableModel> sorter = (TableRowSorter<TableModel>) rowSorter;
            String text = searchField.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        }
    }

    private void adjustColumnSizes() {
        TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = columnModel.getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private Object[][] storeInitialData(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        return data.stream().map(rowMapper).toArray(Object[][]::new);
    }

    protected abstract String getFrameTitle();

    protected void onCommit() {
        handleCommit(getCurrentTableData());
        resetButton.setEnabled(false);
    }

    protected abstract void handleCommit(Object[][] data);

    protected void onReset() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Object[] row : initialTableData) {
            model.addRow(row);
        }
        resetButton.setEnabled(false);
        commitButton.setEnabled(false);
    }

    protected void onEditRow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "No row selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int columnCount = table.getColumnCount();
        Object[] rowData = getRowData(row, columnCount);
        JTextField[] textFields = createTextFields(columnCount, rowData);
        JPanel panel = createEditPanel(columnCount, textFields);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            updateTableData(row, columnCount, textFields);
        }
    }

    private JTextField[] createTextFields(int columnCount, Object[] rowData) {
        JTextField[] textFields = new JTextField[columnCount];
        for (int i = 0; i < columnCount; i++) {
            textFields[i] = new JTextField(rowData[i].toString());
        }
        return textFields;
    }

    private JPanel createEditPanel(int columnCount, JTextField[] textFields) {
        JPanel panel = new JPanel(new GridLayout(columnCount, 2));
        for (int i = 0; i < columnCount; i++) {
            panel.add(new JLabel(table.getColumnName(i)));
            textFields[i].setEditable(false);;
            panel.add(textFields[i]);
        }
        return panel;
    }

    protected Object[] getRowData(int row, int columnCount) {
        Object[] rowData = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            rowData[i] = table.getValueAt(row, i);
        }
        return rowData;
    }

    private void updateTableData(int row, int columnCount, JTextField[] textFields) {
        for (int i = 0; i < columnCount; i++) {
            table.setValueAt(textFields[i].getText(), row, i);
        }
    }

    protected Object[][] getCurrentTableData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();
        Object[][] data = new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                data[i][j] = model.getValueAt(i, j);
            }
        }

        return data;
    }

    public boolean checkForChanges(Object[][] beforeEditData) {
        Object[][] currentData = getCurrentTableData();
        return !Arrays.deepEquals(beforeEditData, currentData);
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    protected <E> void populateComboBox(JComboBox<E> comboBox, List<E> items) {
        comboBox.removeAllItems();
        for (E item : items) {
            comboBox.addItem(item);
        }
    }

    protected <E> void selectCurrentItem(JComboBox<E> comboBox, E currentItem) {
        comboBox.setSelectedItem(currentItem);
    }
}