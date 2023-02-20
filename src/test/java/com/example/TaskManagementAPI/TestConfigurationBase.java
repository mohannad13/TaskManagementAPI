package com.example.TaskManagementAPI;


import com.example.TaskManagementAPI.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.TaskManagementAPI.repository.TaskRepository;
import com.example.TaskManagementAPI.repository.UserRepository;
import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;

import org.mockito.Mockito;

@TestConfiguration
public class TestConfigurationBase {

    @Bean
    public UserRepository userRepositoryMock() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public TaskRepository taskRepositoryBeanMock() {
        return Mockito.mock(TaskRepository.class);
    }
    @Bean
    public CommentRepository commentRepositoryBeanMock() {
        return Mockito.mock(CommentRepository.class);
    }

//    @Bean
//    public UserDetailsServiceImpl userDetailsService(UserRepository userRepository) {
//        return new UserDetailsServiceImpl(userRepository);
//    }
}
