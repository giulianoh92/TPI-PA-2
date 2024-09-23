package com.tpi.tpi.desktop.view;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.tpi.tpi.desktop.controller.AdminOperationsController;
import com.tpi.tpi.desktop.controller.ViewType;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class AdminView extends AbstractView<Object, AdminOperationsController> implements PanelView<AdminOperationsController> {

    private static final Logger LOGGER = Logger.getLogger(AdminView.class.getName());
    private static final int FRAME_WIDTH = 1200;
    private static final int FRAME_HEIGHT = 800;

    private JTabbedPane tabbedPane;

    public AdminView() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        setBorder(null); // Remove any border to eliminate padding
        setBackground(UIManager.getColor("Panel.background")); // Set background color to match dark theme
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
        setupLookAndFeel();
        setController(controller);
        JFrame frame = createFrame();
        frame.add(this, BorderLayout.CENTER); // Add the main panel to the center of the content pane
        frame.setVisible(true);

        // Add tabs for different views
        addTabs();
    }

    @Override
    public void showPanel(AdminOperationsController controller, JPanel panel) {
        setController(controller);
        configurePanel(panel);
    }

    private void addTabs() {
        addTab(ViewType.PRODUCT, "Products");
        addTab(ViewType.USER, "Users");
        addTab(ViewType.ORDER, "Orders");
        addTab(ViewType.CUSTOMER, "Customers");
    }

    private void addTab(ViewType viewType, String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIManager.getColor("Panel.background")); // Set background color to match dark theme
        tabbedPane.addTab(title, panel);
        controller.displayViewInPanel(viewType, panel);
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

    private void setupLookAndFeel() {
        FlatDarkLaf.setup();
        FlatLaf.setUseNativeWindowDecorations(false);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame(getFrameTitle());
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT); // Increased size to fit tabs properly
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout()); // Ensure content pane uses BorderLayout
        frame.getContentPane().setBackground(UIManager.getColor("Panel.background")); // Set background color to match dark theme
        return frame;
    }

    private void configurePanel(JPanel panel) {
        panel.setLayout(new BorderLayout()); // Ensure panel uses BorderLayout
        panel.add(this, BorderLayout.CENTER); // Add the main panel to the center of the panel
        panel.setBackground(UIManager.getColor("Panel.background")); // Set background color to match dark theme
    }
}