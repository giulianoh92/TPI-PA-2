package com.tpi.tpi.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractView<T, C> extends javax.swing.JPanel {
    protected C controller;
    private JTable table;
    private Object[][] initialTableData;

    // Constructor sin parámetros para compatibilidad con NetBeans
    public AbstractView() {
        initComponents();  // Llamada para inicializar los componentes gráficos
    }

    // Método para inicializar componentes gráficos - necesario para NetBeans
    private void initComponents() {
        // Inicialización básica del panel
        setLayout(new BorderLayout());

        // Panel de botones (botones vacíos por ahora, se llenarán en la subclase)
        JPanel buttonPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        JButton commitButton = new JButton("Commit");

        buttonPanel.add(resetButton);
        buttonPanel.add(commitButton);

        // Agrega el panel de botones al panel principal
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Método para configurar el controlador
    public void setController(C controller) {
        this.controller = controller;
    }

    public void showPanel(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame(getFrameTitle());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Añade la tabla al panel
        JScrollPane tableScrollPane = createTable(data, columnNames, rowMapper);
        panel.add(tableScrollPane, BorderLayout.CENTER);

        // Crea un panel para los botones
        JPanel buttonPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        JButton commitButton = new JButton("Commit");

        // Añadir listeners de acción a los botones
        resetButton.addActionListener(e -> onReset());
        commitButton.addActionListener(e -> onCommit());

        // Añade los botones al panel de botones
        buttonPanel.add(resetButton);
        buttonPanel.add(commitButton);

        // Añade el panel de botones al panel principal
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private JScrollPane createTable(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        // Almacena una copia de los datos iniciales
        initialTableData = storeInitialData(data, columnNames, rowMapper);

        // Usa una copia de los datos iniciales para la tabla
        Object[][] tableData = Arrays.copyOf(initialTableData, initialTableData.length);
        for (int i = 0; i < tableData.length; i++) {
            tableData[i] = Arrays.copyOf(initialTableData[i], initialTableData[i].length);
        }

        // Crea la tabla con una copia de los datos iniciales
        table = new JTable(tableData, columnNames);
        return new JScrollPane(table);
    }

    private Object[][] storeInitialData(List<T> data, String[] columnNames, Function<T, Object[]> rowMapper) {
        Object[][] dataCopy = new Object[data.size()][columnNames.length];
        for (int i = 0; i < data.size(); i++) {
            dataCopy[i] = Arrays.copyOf(rowMapper.apply(data.get(i)), columnNames.length);
        }
        return dataCopy;
    }

    protected abstract String getFrameTitle();

    // Método para imprimir el contenido actual de la tabla
    protected void onCommit() {
        System.out.println("Current table values:");
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();

        Object[][] currentData = new Object[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                currentData[row][col] = table.getValueAt(row, col);
                System.out.print(currentData[row][col] + "\t");
            }
            System.out.println();
        }

        // Llama al método abstracto de commit
        handleCommit(currentData);
    }

    // Método abstracto para que las subclases implementen la lógica específica de commit
    protected abstract void handleCommit(Object[][] data);

    // Método para refrescar los valores de la tabla a su estado inicial
    protected void onReset() {
        if (initialTableData == null) {
            System.out.println("No initial data available to reset.");
            return;
        }
        
        System.out.println("Resetting table values to initial state");
        
        // Verifica si el tamaño de los datos actuales coincide con el tamaño de los datos iniciales
        int rowCount = table.getRowCount();
        int columnCount = table.getColumnCount();
    
        if (rowCount != initialTableData.length || columnCount != initialTableData[0].length) {
            System.out.println("Mismatch in data size. Cannot reset.");
            return;
        }
    
        // Restaura los datos de la tabla a su estado inicial
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                table.setValueAt(initialTableData[row][col], row, col);
            }
        }
    }
}
