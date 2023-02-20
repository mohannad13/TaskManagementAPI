package com.example.TaskManagementAPI.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void testEquals_withSameObject_returnsTrue() {
        // Given
        Task task = new Task("Title", "Description", "Status", new User());

        // Then
        assertEquals(task, task);
    }

    @Test
    void testEquals_withEqualObjects_returnsTrue() {
        // Given
        Task task1 = new Task("Title", "Description", "Status", new User());
        Task task2 = new Task("Title", "Description", "Status", new User());

        // Then
        assertEquals(task1, task2);
    }

    @Test
    void testEquals_withDifferentTitle_returnsFalse() {
        // Given
        Task task1 = new Task("Title1", "Description", "Status", new User());
        Task task2 = new Task("Title2", "Description", "Status", new User());

        // Then
        assertNotEquals(task1, task2);
    }

    @Test
    void testEquals_withDifferentDescription_returnsFalse() {
        // Given
        Task task1 = new Task("Title", "Description1", "Status", new User());
        Task task2 = new Task("Title", "Description2", "Status", new User());

        // Then
        assertNotEquals(task1, task2);
    }

    @Test
    void testEquals_withDifferentStatus_returnsFalse() {
        // Given
        Task task1 = new Task("Title", "Descriptions", "Status1", new User());
        Task task2 = new Task("Title", "Description", "Status2", new User());

        // Then
        assertNotEquals(task1, task2);
    }

    @Test
    void testEquals_withDifferentAssignee_returnsFalse() {
        // Given
        User user1 = new User("Name1", "Email1", "Password", true);
        user1.setId(1L);
        User user2 = new User("Name2", "Email2", "Password", true);
        user2.setId(2L);
        Task task1 = new Task("Title", "Description", "Status", user1);
        Task task2 = new Task("Title", "Description", "Status", user2);

        // Then
        assertNotEquals(task1, task2);
    }
}
