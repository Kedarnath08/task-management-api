package com.kedar.taskmanager.dto;

import com.kedar.taskmanager.model.Task;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private Task.Status status;

    private Task.Priority priority;

    private LocalDateTime dueDate;
}