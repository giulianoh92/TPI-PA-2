package com.tpi.tpi.service;

import com.tpi.tpi.model.Administrator;
import com.tpi.tpi.model.Product;
import com.tpi.tpi.repository.AdminRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public List<Administrator> getAllAdminList() {
        return adminRepository.findAll();
    }
}
