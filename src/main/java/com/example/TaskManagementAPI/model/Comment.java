package com.example.TaskManagementAPI.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_USER = "user_id";
    public static final String COLUMN_TASK = "task_id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = COLUMN_COMMENT)
    private String comment;

    @Column(nullable = false, name = COLUMN_TIMESTAMP)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = COLUMN_USER)
    private User user;

    @ManyToOne
    @JoinColumn(name = COLUMN_TASK)
    private Task task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
