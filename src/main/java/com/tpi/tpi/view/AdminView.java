package com.tpi.tpi.view;

import com.tpi.tpi.controller.AdminOperationsController;
import com.tpi.tpi.controller.ViewType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.logging.Logger;

public class AdminView extends AbstractView<Object, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final int PADDING = 10;
    private static final int BORDER_PADDING = 20;
    private static final String PRODUCT_BUTTON_TEXT = "Products";
    private static final String USER_BUTTON_TEXT = "Users";
    private static final String ORDER_BUTTON_TEXT = "Orders";
    private static final String CUSTOMER_BUTTON_TEXT = "Customers";
    private static final Logger LOGGER = Logger.getLogger(AdminView.class.getName());

    private JButton productButton;
    private JButton userButton;
    private JButton orderButton;
    private JButton customerButton;

    public AdminView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();

        productButton = createButton(PRODUCT_BUTTON_TEXT, e -> controller.displayView(ViewType.PRODUCT));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(productButton, gbc);

        userButton = createButton(USER_BUTTON_TEXT, e -> controller.displayView(ViewType.USER));
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userButton, gbc);

        orderButton = createButton(ORDER_BUTTON_TEXT, e -> controller.displayView(ViewType.ORDER));
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(orderButton, gbc);

        customerButton = createButton(CUSTOMER_BUTTON_TEXT, e -> controller.displayView(ViewType.CUSTOMER));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(customerButton, gbc);

        setBorder(new EmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private JButton createButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        return button;
    }

    @Override
    protected boolean shouldShowDefaultButtons() {
        return false;
    }

    @Override
    protected String getFrameTitle() {
        return "Admin Operations";
    }

    @Override
    public void showPanel(AdminOperationsController controller) {
        setController(controller);
        JFrame frame = new JFrame(getFrameTitle());
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    public void handleCommit(Object[][] data) {
        LOGGER.info("Committing admin data:");
        for (Object[] row : data) {
            for (Object value : row) {
                LOGGER.info(value + "\t");
            }
            LOGGER.info("\n");
        }

        controller.commitAdminData(data);
    }
}