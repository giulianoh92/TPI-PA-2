package com.tpi.tpi.service;

import com.tpi.tpi.model.User;
import com.tpi.tpi.repository.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository adminRepository;

    public List<User> getAllUserList() {
        return adminRepository.findAll();
    }
}
