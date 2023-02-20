package com.example.TaskManagementAPI.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginRequestTest {

    @Test
    public void testGetUsername_returnsUsername() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john");

        // When
        String username = loginRequest.getUsername();

        // Then
        assertEquals("john", username);
    }

    @Test
    public void testSetUsername_setsUsername() {
        // Given
        LoginRequest loginRequest = new LoginRequest();

        // When
        loginRequest.setUsername("john");

        // Then
        assertEquals("john", loginRequest.getUsername());
    }

    @Test
    public void testGetPassword_returnsPassword() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("password");

        // When
        String password = loginRequest.getPassword();

        // Then
        assertEquals("password", password);
    }

    @Test
    public void testSetPassword_setsPassword() {
        // Given
        LoginRequest loginRequest = new LoginRequest();

        // When
        loginRequest.setPassword("password");

        // Then
        assertEquals("password", loginRequest.getPassword());
    }

    @Test
    public void testGetEmail_returnsEmail() {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");

        // When
        String email = loginRequest.getEmail();

        // Then
        assertEquals("john@example.com", email);
    }

    @Test
    public void testSetEmail_setsEmail() {
        // Given
        LoginRequest loginRequest = new LoginRequest();

        // When
        loginRequest.setEmail("john@example.com");

        // Then
        assertEquals("john@example.com", loginRequest.getEmail());
    }
}
