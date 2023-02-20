package com.example.TaskManagementAPI.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.UserRepository;
import com.example.TaskManagementAPI.security.service.UserDetailsImpl;
import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UsernamePasswordAuthenticationToken authentication;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsers_withAdminRole_returnsUsers() {
        User user = new User("Admin", "admin@example.com", "admin", true);
        user.setId(1L);
        when(authentication.getAuthorities()).thenReturn((Collection<GrantedAuthority>) new UserDetailsImpl(user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user1 = new User("User 1", "user1@example.com", "user1", false);
        User user2 = new User("User 2", "user2@example.com", "user2", false);
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        List<User> returnedUsers = userController.getAllUsers(null);
        assertEquals(users, returnedUsers);
    }
    @Test
    @WithMockUser(roles = "USER")
    public void testGetAllUsers_withUserRole_throwsException() {
        User user = new User("User", "user@example.com", "user", false);
        user.setId(1L);
        when(authentication.getAuthorities()).thenReturn((Collection<GrantedAuthority>) new UserDetailsImpl(user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Exception exception = assertThrows(ResponseStatusException.class, () -> userController.getAllUsers(null));
        assertEquals("403 FORBIDDEN \"User does not have the required role\"", exception.getMessage());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void testCreateUser_withUserRole_throwsException() {
        User user = new User("User", "user@example.com", "user", false);
        user.setId(1L);
        when(authentication.getAuthorities()).thenReturn((Collection<GrantedAuthority>) new UserDetailsImpl(user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User newUser = new User("User 1", "user1@example.com", "user1", false);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.createUser(newUser, null);
        });
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("User does not have the required role", exception.getReason());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateUser_withAdminRole_createsUser() {
        User user = new User("Admin", "admin@example.com", "admin", true);
        user.setId(1L);
        when(authentication.getAuthorities()).thenReturn((Collection<GrantedAuthority>) new UserDetailsImpl(user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User newUser = new User("User 1", "user1@example.com", "user1", false);
        User returnedUser = userController.createUser(newUser, null);

        assertEquals(newUser, returnedUser);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateUser_withUserRole_throwsException() {
        User user = new User("User", "user@example.com", "user", false);
        when(authentication.getAuthorities()).thenReturn((Collection<GrantedAuthority>) new UserDetailsImpl(user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long id = 1L;
        User updatedUser = new User("Updated User", "updatedUser@example.com", "updatedUser", false);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userController.updateUser(id, updatedUser, null));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("User does not have the required role", exception.getReason());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testDeleteUser_withUserRole_throwsException() {
        User user = new User("User", "user@example.com", "user", false);
        user.setId(1L);
        when(authentication.getAuthorities()).thenReturn((Collection<GrantedAuthority>) new UserDetailsImpl(user).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.deleteUser(1L, null);
        });
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals("User does not have the required role", exception.getReason());
    }
}