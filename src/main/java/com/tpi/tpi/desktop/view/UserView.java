package com.tpi.tpi.desktop.view;

import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.common.model.User;

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