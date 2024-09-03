package com.tpi.tpi.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.tpi.tpi.service.UserService;
import com.tpi.tpi.model.User; // Ensure this import is correct
import com.tpi.tpi.view.PanelView; // Ensure this import is correct

public class UserView implements PanelView<UserService> {

    // Method to show the details of the product list
    @Override
    public void showPanel(UserService userService) {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        JFrame frame = new JFrame("User Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        // Add the table to the panel
        JScrollPane tableScrollPane = createTable(userService.getAllUserList());
        panel.add(tableScrollPane, BorderLayout.CENTER);

        frame.add(panel);
        frame.pack(); 
        frame.setVisible(true);
    }

    private JScrollPane createTable(List<User> users) {
        String[] columnNames = {"ID", "Username", "Password", "Registered At"};
        Object[][] data = new Object[users.size()][4];

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            data[i][0] = user.getIdUsuario();
            data[i][1] = user.getNombreUsuario();
            data[i][2] = user.getPassword();
            data[i][3] = user.getFechaRegistro();
        }

        JTable table = new JTable(data, columnNames);
        return new JScrollPane(table);
    }
}