package com.tpi.tpi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String showLoginPage() {
        logger.info("Showing login page");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username, 
                        @RequestParam("password") String password, 
                        RedirectAttributes redirectAttributes) {
        // Log the login attempt
        logger.info("Login attempt with username: {}", username);

        // Basic validation
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            logger.warn("Login failed due to missing username or password");
            redirectAttributes.addFlashAttribute("error", "Username and password are required.");
            return "redirect:/login";
        }

        // Print the login data to the terminal (for debugging purposes)
        logger.debug("Username: {}", username);
        logger.debug("Password: {}", password);

        // Simulate authentication logic (to be implemented later)
        boolean isAuthenticated = false; // Replace with actual authentication logic
        if (!isAuthenticated) {
            logger.warn("Login failed for username: {}", username);
            redirectAttributes.addFlashAttribute("error", "Invalid username or password.");
            return "redirect:/login";
        }

        // Redirect to the home page or any other page
        return "redirect:/home";
    }
}