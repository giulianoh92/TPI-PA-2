package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.User;
import com.tpi.tpi.view.AbstractView; // Corrected import statement

import java.awt.*;
import java.util.Date; // Added import for Date
import java.util.List;
import java.util.function.Function;

public class UserView extends AbstractView<User, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private List<User> users;

    public UserView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
    }

    @Override
    protected String getFrameTitle() {
        return "User Management";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller); // Set the controller

        String[] columnNames = {"ID", "Username", "Password", "Registered At"};
        Function<User, Object[]> rowMapper = user -> new Object[]{
            user.getUserId(),
            user.getUsername(),
            user.getPassword(),
            user.getRegisterDate()
        };
        users = controller.getUserService().getAllUserList();
        super.showPanel(users, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        System.out.println("Committing user data:");
        for (int i = 0; i < data.length; i++) {
            users.get(i).setUserId((Integer) data[i][0]);
            users.get(i).setUsername((String) data[i][1]);
            users.get(i).setPassword((String) data[i][2]);
            users.get(i).setRegisterDate(new java.sql.Date(((java.util.Date) data[i][3]).getTime())); // Convert java.util.Date to java.sql.Date
            System.out.println(users.get(i));
        }
    
        controller.commitUserData(users);
    }
}