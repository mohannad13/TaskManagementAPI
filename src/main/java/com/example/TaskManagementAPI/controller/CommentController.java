package com.example.TaskManagementAPI.controller;

import com.example.TaskManagementAPI.model.Comment;
import com.example.TaskManagementAPI.model.Task;
import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.CommentRepository;
import com.example.TaskManagementAPI.repository.TaskRepository;
import com.example.TaskManagementAPI.repository.UserRepository;

import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    /**
     * This endpoint returns a list of all comments, but only to users with ADMIN role.
     * @param userDetails the authenticated user details
     * @return a list of comments
     */
    @GetMapping
    public List<Comment> getAllComments(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null && userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return commentRepository.findAll();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * This endpoint returns a list of all comments of a task with the specified taskId,
     * but only to users with ADMIN role or to the assignee of the task.
     * @param taskId the id of the task
     * @param userDetails the authenticated user details
     * @return a list of comments
     */
    @GetMapping("/task/{taskId}")
    public List<Comment> getCommentsByTaskId(@PathVariable Long taskId, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + taskId);
        }
        if (userDetails != null && (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || task.getAssignee().getEmail().equals(userDetails.getUsername()))) {
            return commentRepository.findByTask(task);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }


    /**
     * This endpoint creates a new comment, but only to users with ADMIN role or to the user who is adding the comment.
     * @param comment the comment object to be created
     * @param taskId the id of the task
     * @param userId the id of the user who is adding the comment
     * @param userDetails the authenticated user details
     * @return the created comment
     */
    @PostMapping
    public Comment createComment(@RequestBody Comment comment, @RequestParam Long taskId, @RequestParam Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found with id: " + taskId);
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
        }
        if (userDetails != null && (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || userDetails.getUsername().equals(user.getEmail()))) {
            comment.setTask(task);
            comment.setUser(user);
            comment.setTimestamp(LocalDateTime.now());
            return commentRepository.save(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * This endpoint updates a comment with the specified id, but only to users with ADMIN role or to the user who created the comment.
     * @param id the id of the comment
     * @param updatedComment the updated comment object
     * @param userDetails the authenticated user details
     * @return the updated comment
     */
    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment updatedComment, @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with id: " + id);
        }
        if (userDetails != null && (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || comment.getUser().getEmail().equals(userDetails.getUsername()))) {
            comment.setComment(updatedComment.getComment());
            return commentRepository.save(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

    /**
     * Deletes the comment with the given ID, but only if the requesting user is an admin or the author of the comment.
     *
     * @param id the ID of the comment to delete
     * @param userDetails the details of the authenticated user making the request
     * @throws ResponseStatusException if the comment is not found, or if the user does not have the required role to delete the comment
     */
    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found with id: " + id);
        }
        if (userDetails != null && (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || comment.getUser().getEmail().equals(userDetails.getUsername()))) {
            commentRepository.delete(comment);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have the required role");
        }
    }

}

