package com.kedar.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kedar.taskmanager.dto.TaskRequest;
import com.kedar.taskmanager.dto.TaskResponse;
import com.kedar.taskmanager.model.Task;
import com.kedar.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskResponse taskResponse;
    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        taskResponse = new TaskResponse();
        taskResponse.setId(1L);
        taskResponse.setTitle("Test Task");
        taskResponse.setDescription("Test Description");
        taskResponse.setStatus(Task.Status.PENDING);
        taskResponse.setPriority(Task.Priority.MEDIUM);
        taskResponse.setCreatedAt(LocalDateTime.now());
        taskResponse.setUserEmail("user@example.com");

        taskRequest = new TaskRequest();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("Test Description");
        taskRequest.setStatus(Task.Status.PENDING);
        taskRequest.setPriority(Task.Priority.MEDIUM);
    }

    @Test
    void createTask_ShouldReturn200_WhenRequestIsValid() throws Exception {
        when(taskService.createTask(any(TaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(taskService, times(1)).createTask(any(TaskRequest.class));
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks_WhenNoFiltersApplied() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(taskResponse));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getAllTasks_ShouldReturnFilteredTasks_WhenStatusFilterApplied() throws Exception {
        when(taskService.getTasksByStatus(Task.Status.PENDING)).thenReturn(List.of(taskResponse));

        mockMvc.perform(get("/api/tasks").param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(taskService, times(1)).getTasksByStatus(Task.Status.PENDING);
    }

    @Test
    void getAllTasks_ShouldReturnFilteredTasks_WhenPriorityFilterApplied() throws Exception {
        when(taskService.getTasksByPriority(Task.Priority.MEDIUM)).thenReturn(List.of(taskResponse));

        mockMvc.perform(get("/api/tasks").param("priority", "MEDIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("MEDIUM"));

        verify(taskService, times(1)).getTasksByPriority(Task.Priority.MEDIUM);
    }

    @Test
    void updateTask_ShouldReturn200_WhenTaskUpdatedSuccessfully() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskRequest.class))).thenReturn(taskResponse);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(taskService, times(1)).updateTask(eq(1L), any(TaskRequest.class));
    }

    @Test
    void deleteTask_ShouldReturn204_WhenTaskDeletedSuccessfully() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }
}
