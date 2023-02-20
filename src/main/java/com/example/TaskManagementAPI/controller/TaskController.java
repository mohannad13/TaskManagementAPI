package com.example.TaskManagementAPI.controller;

import java.util.List;
import java.util.Optional;

import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.UserRepository;
import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;


import com.example.TaskManagementAPI.model.Task;
import com.example.TaskManagementAPI.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    /**
     * Returns a list of all tasks in the system.
     *
     * @param userDetails details of the authenticated user.
     * @return a list of all tasks.
     * @throws ResponseStatusException if the user is not an admin.
     */
    @GetMapping
    public List<Task> getAllTasks(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return taskRepository.findAll();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }


    /**
     * Creates a new task.
     *
     * @param task         the task to be created.
     * @param userDetails  details of the authenticated user.
     * @return the newly created task.
     * @throws ResponseStatusException if the user is not an admin.
     */
    @PostMapping
    public Task createTask(@RequestBody Task task, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return taskRepository.save(task);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Returns a list of tasks assigned to the user with the specified ID.
     *
     * @param userId       the ID of the user.
     * @param userDetails  details of the authenticated user.
     * @return a list of tasks assigned to the specified user.
     * @throws ResponseStatusException if the user is not an admin or if the user is not the same as the specified user.
     */
    @GetMapping("/assigned")
    public List<Task> getAssignedTasks(@RequestParam("userId") Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> current = userRepository.findById(userId);
        if (current.isPresent() && userDetails != null && (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || userDetails.getUsername().equals(current.get().getEmail()))) {
            return taskRepository.findByAssigneeId(userId);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Updates the task with the specified ID.
     *
     * @param id           the ID of the task to be updated.
     * @param task         the updated task.
     * @param userDetails  details of the authenticated user.
     * @return the updated task.
     * @throws ResponseStatusException if the user is not an admin.
     */
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task task, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            task.setId(id);
            return taskRepository.save(task);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Deletes the task with the specified ID.
     *
     * @param id           the ID of the task to be deleted.
     * @param userDetails  details of the authenticated user.
     * @throws ResponseStatusException if the user is not an admin.
     */
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            taskRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }
}
