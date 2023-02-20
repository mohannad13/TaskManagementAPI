package com.example.TaskManagementAPI.repository;

import com.example.TaskManagementAPI.model.Comment;
import com.example.TaskManagementAPI.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task);
}

