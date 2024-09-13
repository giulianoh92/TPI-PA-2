package com.tpi.tpi.common.service;

import com.tpi.tpi.common.model.Administrator;
import com.tpi.tpi.common.repository.AdminRepository;

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
