package com.tpi.tpi.service;

import com.tpi.tpi.model.User;
import com.tpi.tpi.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUserList() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error fetching all users", e);
        }
    }

    public void updateUser(User row) {
        try {
            userRepository.updateUser(row);
        } catch (Exception e) {
            // Log the exception and rethrow it or handle it accordingly
            throw new RuntimeException("Error updating user", e);
        }
    }
}