package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.User;

import javax.swing.*;
import java.awt.*;
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
            user.getIdUsuario(),
            user.getNombreUsuario(),
            user.getPassword(),
            user.getFechaRegistro()
        };
        users = controller.getUserService().getAllUserList();
        super.showPanel(users, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        System.out.println("Committing user data:");
        for (User user : users) {
            System.out.println(user);
        }

        controller.commitUserData(users);
    }
}