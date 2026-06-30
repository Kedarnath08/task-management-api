package com.kedar.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmartTaskRequest {

    @NotBlank(message = "Text is required")
    private String text;
}