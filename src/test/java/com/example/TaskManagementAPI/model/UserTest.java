package com.example.TaskManagementAPI.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testConstructor_withValidArguments_createsUser() {
        // Given
        String name = "John Doe";
        String email = "johndoe@example.com";
        String password = "password";
        boolean active = true;

        // When
        User user = new User(name, email, password, active);
        user.setId(1L);
        // Then
        assertNotNull(user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
        assertTrue(user.getPasswordHash().startsWith("$2a$"));
        assertTrue(user.isActive());
    }

    @Test
    public void testSetPassword_withValidPassword_updatesPassword() {
        // Given
        String password = "password";
        User user = new User();
        user.setPassword(password);

        // When
        user.setPassword("new_password");

        // Then
        assertNotEquals(password, user.getPasswordHash());
        assertTrue(user.getPasswordHash().startsWith("$2a$"));
    }
}
