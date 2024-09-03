package com.tpi.tpi.view;

import com.tpi.tpi.model.User;
import com.tpi.tpi.service.UserService;

import java.util.List;
import java.util.function.Function;

public class UserView extends AbstractView<User, UserService> implements PanelView<UserService> {

    @Override
    protected String getFrameTitle() {
        return "User Management";
    }

    @Override
    public void showPanel(UserService userService) {
        String[] columnNames = {"ID", "Username", "Password", "Registered At"};
        Function<User, Object[]> rowMapper = user -> new Object[]{
            user.getIdUsuario(),
            user.getNombreUsuario(),
            user.getPassword(),
            user.getFechaRegistro()
        };
        super.showPanel(userService, UserService::getAllUserList, columnNames, rowMapper);
    }
}