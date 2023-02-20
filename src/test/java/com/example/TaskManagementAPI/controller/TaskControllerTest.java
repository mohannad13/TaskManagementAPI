package com.example.TaskManagementAPI.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.TaskManagementAPI.model.Task;
import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.TaskRepository;
import com.example.TaskManagementAPI.repository.UserRepository;
import com.example.TaskManagementAPI.security.service.UserDetailsImpl;
import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private TaskController taskController;

    @Test
    public void testGetAllTasks_withAdminRole_returnsTasks() {
        User user = new User("Admin", "admin@example.com", "admin", true);
        // Given
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<Task> returnedTasks = taskController.getAllTasks(userDetails);

        // Then
        assertEquals(tasks, returnedTasks);
    }

    @Test
    public void testGetAllTasks_withNonAdminRole_throwsForbiddenException() {
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        assertThrows(ResponseStatusException.class, () -> taskController.getAllTasks(userDetails));
    }

    @Test
    public void testCreateTask_withAdminRole_returnsCreatedTask() {
        User user = new User("Admin", "admin@example.com", "admin", true);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setTitle("Task 1");
        when(taskRepository.save(task)).thenReturn(task);
        Task returnedTask = taskController.createTask(task, userDetails);
        assertEquals(task, returnedTask);
    }

    @Test
    public void testCreateTask_withNonAdminRole_throwsForbiddenException() {
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setTitle("Task 1");
        assertThrows(ResponseStatusException.class, () -> taskController.createTask(task, userDetails));
    }

    @Test
    public void testGetAssignedTasks_withAdminRole_returnsAssignedTasks() {
        User user = new User("Admin", "admin@example.com", "admin", true);
        UserDetails userDetails = new UserDetailsImpl(user);
        User assignee = new User("Assignee", "assignee@example.com", "assignee", false);
        assignee.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(assignee));
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setAssignee(assignee);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setAssignee(assignee);
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findByAssigneeId(1L)).thenReturn(tasks);
        List<Task> returnedTasks = taskController.getAssignedTasks(1L, userDetails);
        assertEquals(tasks, returnedTasks);
    }

    @Test
    public void testGetAssignedTasks_withNonAdminRole_returnsAssignedTasksForCurrentUser() {
        User user = new User("User", "user@example.com", "user", true);
        user.setId(1L);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setAssignee(user);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setAssignee(user);
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findByAssigneeId(1L)).thenReturn(tasks);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        List<Task> returnedTasks = taskController.getAssignedTasks(1L, userDetails);
        assertEquals(tasks, returnedTasks);
    }


    @Test
    public void testGetAssignedTasks_withNonAdminRole_throwsForbiddenExceptionForOtherUsers() {
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        User assignee = new User("Assignee", "assignee@example.com", "assignee", false);
        assignee.setId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(assignee));
        assertThrows(ResponseStatusException.class, () -> taskController.getAssignedTasks(1L, userDetails));
    }

    @Test
    public void testUpdateTask_withAdminRole_returnsUpdatedTask() {
        User user = new User("Admin", "admin@example.com", "admin", true);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setTitle("Task 1");
        task.setId(1L);
        when(taskRepository.save(task)).thenReturn(task);
        Task returnedTask = taskController.updateTask(1L, task, userDetails);
        assertEquals(task, returnedTask);
    }

    @Test
    public void testUpdateTask_withNonAdminRole_throwsForbiddenException() {
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setTitle("Task 1");
        task.setId(1L);
        assertThrows(ResponseStatusException.class, () -> taskController.updateTask(1L, task, userDetails));
    }

    @Test
    public void testDeleteTask_withNonAdminRole_throwsForbiddenException() {
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        assertThrows(ResponseStatusException.class, () -> taskController.deleteTask(1L, userDetails));
    }

}
