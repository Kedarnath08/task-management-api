package com.kedar.taskmanager.dto;

import com.kedar.taskmanager.model.Task;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskResponse {

    private Long id;
    private String title;
    private String description;
    private Task.Status status;
    private Task.Priority priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private String userEmail;
}