package com.example.TaskManagementAPI.controller;

import com.example.TaskManagementAPI.model.Comment;
import com.example.TaskManagementAPI.model.Task;
import com.example.TaskManagementAPI.model.User;
import com.example.TaskManagementAPI.repository.CommentRepository;
import com.example.TaskManagementAPI.repository.TaskRepository;
import com.example.TaskManagementAPI.repository.UserRepository;
import com.example.TaskManagementAPI.security.service.UserDetailsImpl;
import com.example.TaskManagementAPI.security.service.UserDetailsServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private CommentController commentController;

    @Test
    public void testGetAllComments_withAdminRole_returnsComments() {
        // Given
        User user = new User("Admin", "admin@example.com", "admin", true);
        UserDetails userDetails = new UserDetailsImpl(user);
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setComment("Comment 1");
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setComment("Comment 2");
        List<Comment> comments = Arrays.asList(comment1, comment2);
        when(commentRepository.findAll()).thenReturn(comments);

        // When
        List<Comment> returnedComments = commentController.getAllComments(userDetails);

        // Then
        assertEquals(comments, returnedComments);
    }

    @Test
    public void testGetAllComments_withNonAdminRole_throwsForbiddenException() {
        // Given
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);

        // Then
        assertThrows(ResponseStatusException.class, () -> commentController.getAllComments(userDetails));
    }

    @Test
    public void testGetCommentsByTaskId_withAdminRole_returnsComments() {
        // Given
        User user = new User("Admin", "admin@example.com", "admin", true);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setComment("Comment 1");
        comment1.setTask(task);
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setComment("Comment 2");
        comment2.setTask(task);
        List<Comment> comments = Arrays.asList(comment1, comment2);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.findByTask(task)).thenReturn(comments);

        // When
        List<Comment> returnedComments = commentController.getCommentsByTaskId(1L, userDetails);

        // Then
        assertEquals(comments, returnedComments);
    }
    @Test
    public void testGetCommentsByTaskId_withNonAdminRoleAndUnassignedTask_throwsForbiddenException() {
        User user = new User("User", "user@example.com", "user", false);
        user.setId(1L);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Task description");
        User assignee = new User("Assignee", "assignee@example.com", "assignee", false);
        assignee.setId(2L);
        task.setAssignee(assignee);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        assertThrows(ResponseStatusException.class, () -> commentController.getCommentsByTaskId(1L, userDetails));
    }


    @Test
    public void testCreateComment_withAdminRoleAndValidTaskAndUser_returnsComment() {
        // Given
        User user = new User("Admin", "admin@example.com", "admin", true);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        User assignee = new User("Assignee", "assignee@example.com", "assignee", false);
        assignee.setId(2L);
        task.setAssignee(assignee);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Comment 1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));
        when(commentRepository.save(comment)).thenReturn(comment);

        // When
        Comment returnedComment = commentController.createComment(comment, 1L, 2L, userDetails);

        // Then
        assertEquals(comment, returnedComment);
    }


    @Test
    public void testCreateComment_withNonAdminRoleAndValidTaskAndUser_throwsForbiddenException() {
        // Given
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        User assignee = new User("Assignee", "assignee@example.com", "assignee", false);
        assignee.setId(2L);
        task.setAssignee(assignee);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Comment 1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.of(assignee));

        // Then
        assertThrows(ResponseStatusException.class, () -> commentController.createComment(comment, 1L, 2L, userDetails));
    }

    @Test
    public void testCreateComment_withValidTaskAndUserButInvalidUserCredentials_throwsForbiddenException() {
        // Given
        User user = new User("User", "user@example.com", "user", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        User assignee = new User("Assignee", "assignee@example.com", "assignee", false);
        assignee.setId(2L);
        task.setAssignee(assignee);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Comment 1");

        // Then
        assertThrows(ResponseStatusException.class, () -> commentController.createComment(comment, 1L, 2L, userDetails));
    }

    @Test
    public void testCreateComment_withValidTaskAndInvalidUser_throwsNotFoundException() {
        // Given
        User user = new User("Admin", "admin@example.com", "admin", true);
        UserDetails userDetails = new UserDetailsImpl(user);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Comment 1");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // Then
        assertThrows(ResponseStatusException.class, () -> commentController.createComment(comment, 1L, 2L, userDetails));
    }

    @Test
    public void testCreateComment_withInvalidTask_throwsNotFoundException() {
        // Given
        User user = new User("User", "user@example.com", "user", false);
        user.setId(1L);
        UserDetails userDetails = new UserDetailsImpl(user);
        Comment comment = new Comment();
        comment.setComment("This is a comment");
        Long taskId = 1L;
        Long userId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ResponseStatusException.class, () -> commentController.createComment(comment, taskId, userId, userDetails));
    }


    @Test
    public void testUpdateComment_withNonAdminRoleAndMismatchedUser_throwsForbiddenException() {
        // Given
        User user = new User("Non-Admin", "nonadmin@example.com", "nonadmin", false);
        UserDetails userDetails = new UserDetailsImpl(user);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setComment("Original Comment");
        User commentUser = new User("Comment User", "commentuser@example.com", "commentuser", false);
        comment.setUser(commentUser);
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        comment.setTask(task);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setComment("Updated Comment");

        // Then
        assertThrows(ResponseStatusException.class, () -> commentController.updateComment(1L, updatedComment, userDetails));
    }


}