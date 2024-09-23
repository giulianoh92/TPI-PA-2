package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;

public class UserView extends AbstractView<User, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final Logger LOGGER = Logger.getLogger(UserView.class.getName());
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
        setController(controller);
        setupPanel(controller);
    }

    @Override
    public void showPanel(AdminOperationsController controller, JPanel panel) {
        setController(controller);
        setupPanel(controller, panel);
    }

    private void setupPanel(AdminOperationsController controller) {
        String[] columnNames = {"ID", "Username", "Password", "Registered At"};
        Function<User, Object[]> rowMapper = createRowMapper();

        users = controller.getUserService().getAllUserList();

        JPanel tablePanel = createTable(users, columnNames, rowMapper);
        add(tablePanel, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    private void setupPanel(AdminOperationsController controller, JPanel panel) {
        String[] columnNames = {"ID", "Username", "Password", "Registered At"};
        Function<User, Object[]> rowMapper = createRowMapper();

        users = controller.getUserService().getAllUserList();

        JPanel tablePanel = createTable(users, columnNames, rowMapper);
        panel.add(tablePanel, BorderLayout.CENTER);

        if (shouldShowDefaultButtons()) {
            JPanel buttonPanel = createButtonPanel();
            panel.add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    private Function<User, Object[]> createRowMapper() {
        return user -> new Object[]{
            user.getUserId(),
            user.getUsername(),
            "******",
            user.getRegisterDate()
        };
    }

    @Override
    public void handleCommit(Object[][] data) {
        LOGGER.info("Committing user data:");
        for (int i = 0; i < data.length; i++) {
            User user = users.get(i);
            user.setUserId((Integer) data[i][0]);
            user.setUsername((String) data[i][1]);
            user.setPassword((String) data[i][2]);
            user.setRegisterDate(new java.sql.Date(((java.util.Date) data[i][3]).getTime()));
            LOGGER.info(user.toString());
        }

        controller.commitUserData(users);
    }
}