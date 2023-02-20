package com.example.TaskManagementAPI.security.service;

import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User admin = new User("Admin","admin@example.com","admin",true);
        userRepository.save(admin);
    }
}
