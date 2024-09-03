package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.model.User;

import java.util.List;
import java.util.function.Function;

public class UserView extends AbstractView<User, AdminOperationsController> implements PanelView<AdminOperationsController> {

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
        List<User> users = controller.getUserService().getAllUserList();
        super.showPanel(users, columnNames, rowMapper);
    }

    @Override
    public void handleCommit(Object[][] data) {
        // Specific commit logic for UserView
        System.out.println("Committing user data:");
        for (Object[] row : data) {
            for (Object value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }

        controller.commitUserData(data);
    }
}