package com.kedar.taskmanager.service;

import com.kedar.taskmanager.dto.TaskRequest;
import com.kedar.taskmanager.dto.TaskResponse;
import com.kedar.taskmanager.exception.ResourceNotFoundException;
import com.kedar.taskmanager.model.Task;
import com.kedar.taskmanager.model.User;
import com.kedar.taskmanager.repository.TaskRepository;
import com.kedar.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import com.kedar.taskmanager.dto.ExtractedTask;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final GeminiService geminiService;

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

    public TaskResponse smartCreateTask(String text) {
        String prompt = "Extract task details from this text: \"" + text + "\"\n\n" +
                "Respond with ONLY a JSON object in this exact format, no extra text, no markdown:\n" +
                "{\"title\": \"short task title\", \"priority\": \"LOW or MEDIUM or HIGH\"}\n\n" +
                "If no priority is mentioned, use MEDIUM.";

        String aiResponse = geminiService.generateContent(prompt);

        String cleanJson = aiResponse.trim()
                .replace("```json", "")
                .replace("```", "")
                .trim();

        ExtractedTask parsed;
        try {
            ObjectMapper mapper = new ObjectMapper();
            parsed = mapper.readValue(cleanJson, ExtractedTask.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not understand the task. Please try rephrasing.");
        }

        TaskRequest request = new TaskRequest();
        request.setTitle(parsed.getTitle());
        request.setPriority(Task.Priority.valueOf(
                parsed.getPriority() != null ? parsed.getPriority() : "MEDIUM"));
        request.setStatus(Task.Status.PENDING);

        return createTask(request);
    }

    public List<TaskResponse> getAllTasks() {
        return taskRepository.findByUser(getCurrentUser())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public String getTaskSummary() {
        List<Task> tasks = taskRepository.findByUser(getCurrentUser());

        if (tasks.isEmpty()) {
            return "You have no tasks yet.";
        }

        StringBuilder taskList = new StringBuilder();
        for (Task task : tasks) {
            taskList.append("- ").append(task.getTitle())
                    .append(" (Status: ").append(task.getStatus())
                    .append(", Priority: ").append(task.getPriority())
                    .append(task.getDueDate() != null ? ", Due: " + task.getDueDate() : "")
                    .append(")\n");
        }

        String prompt = "Here is a list of tasks:\n" + taskList +
                "\nSummarize this in 2-3 short sentences. Mention how many tasks are pending, " +
                "any high priority items, and anything urgent. Keep it conversational and brief.";

        return geminiService.generateContent(prompt);
    }

    public List<TaskResponse> getTasksByStatus(Task.Status status) {
        return taskRepository.findByUserAndStatus(getCurrentUser(), status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<TaskResponse> getTasksByPriority(Task.Priority priority) {
        return taskRepository.findByUserAndPriority(getCurrentUser(), priority)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
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
        Task task = taskRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found or access denied"));
        taskRepository.delete(task);
    }
}