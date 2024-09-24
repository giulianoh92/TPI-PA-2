package com.tpi.tpi.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tpi.tpi.common.service.CustomerService;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/login")
    public String login(CsrfToken csrfToken, Model model) {
        model.addAttribute("_csrf", csrfToken);
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> authenticate(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Map<String, Object> response = new HashMap<>();
        if (customerService.authenticate(email, password)) {
            response.put("success", true);
        } else {
            response.put("success", false);
            response.put("message", "Invalid email or password");
        }

        return ResponseEntity.ok(response);
    }
}