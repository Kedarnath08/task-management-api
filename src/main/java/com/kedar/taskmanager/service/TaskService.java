package com.kedar.taskmanager.service;

import com.kedar.taskmanager.dto.TaskRequest;
import com.kedar.taskmanager.dto.TaskResponse;
import com.kedar.taskmanager.model.Task;
import com.kedar.taskmanager.model.User;
import com.kedar.taskmanager.repository.TaskRepository;
import com.kedar.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setPriority(task.getPriority());
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUserEmail(task.getUser().getEmail());
        return response;
    }

    public TaskResponse createTask(TaskRequest request) {
        User user = getCurrentUser();
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : Task.Status.PENDING);
        task.setPriority(request.getPriority() != null ? request.getPriority() : Task.Priority.MEDIUM);
        task.setDueDate(request.getDueDate());
        task.setUser(user);
        return toResponse(taskRepository.save(task));
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findByUser(getCurrentUser())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null)
            task.setStatus(request.getStatus());
        if (request.getPriority() != null)
            task.setPriority(request.getPriority());
        if (request.getDueDate() != null)
            task.setDueDate(request.getDueDate());
        return toResponse(taskRepository.save(task));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}