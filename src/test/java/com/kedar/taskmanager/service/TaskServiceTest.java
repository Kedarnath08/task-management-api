package com.kedar.taskmanager.service;

import com.kedar.taskmanager.dto.TaskRequest;
import com.kedar.taskmanager.dto.TaskResponse;
import com.kedar.taskmanager.exception.ResourceNotFoundException;
import com.kedar.taskmanager.model.Task;
import com.kedar.taskmanager.model.User;
import com.kedar.taskmanager.repository.TaskRepository;
import com.kedar.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Task testTask;
    private TaskRequest taskRequest;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@example.com");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("user@example.com");
        testUser.setName("User Name");

        testTask = new Task();
        testTask.setId(10L);
        testTask.setTitle("Test Task");
        testTask.setDescription("Description");
        testTask.setStatus(Task.Status.PENDING);
        testTask.setPriority(Task.Priority.MEDIUM);
        testTask.setUser(testUser);
        testTask.setCreatedAt(LocalDateTime.now());

        taskRequest = new TaskRequest();
        taskRequest.setTitle("New Task Title");
        taskRequest.setDescription("New Task Description");
        taskRequest.setStatus(Task.Status.IN_PROGRESS);
        taskRequest.setPriority(Task.Priority.HIGH);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createTask_ShouldSaveTaskAndReturnResponse() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task saved = invocation.getArgument(0);
            saved.setId(11L);
            return saved;
        });

        TaskResponse response = taskService.createTask(taskRequest);

        assertNotNull(response);
        assertEquals(11L, response.getId());
        assertEquals("New Task Title", response.getTitle());
        assertEquals(Task.Status.IN_PROGRESS, response.getStatus());
        assertEquals(Task.Priority.HIGH, response.getPriority());
        assertEquals("user@example.com", response.getUserEmail());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void getAllTasks_ShouldReturnTasksForCurrentUser() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByUser(testUser)).thenReturn(List.of(testTask));

        List<TaskResponse> responses = taskService.getAllTasks();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Test Task", responses.get(0).getTitle());
    }

    @Test
    void getTasksByStatus_ShouldReturnFilteredTasks() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByUserAndStatus(testUser, Task.Status.PENDING)).thenReturn(List.of(testTask));

        List<TaskResponse> responses = taskService.getTasksByStatus(Task.Status.PENDING);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(Task.Status.PENDING, responses.get(0).getStatus());
    }

    @Test
    void getTasksByPriority_ShouldReturnFilteredTasks() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByUserAndPriority(testUser, Task.Priority.MEDIUM)).thenReturn(List.of(testTask));

        List<TaskResponse> responses = taskService.getTasksByPriority(Task.Priority.MEDIUM);

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(Task.Priority.MEDIUM, responses.get(0).getPriority());
    }

    @Test
    void updateTask_ShouldModifyAndReturnTask_WhenUserOwnsTask() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByIdAndUser(10L, testUser)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskResponse response = taskService.updateTask(10L, taskRequest);

        assertNotNull(response);
        assertEquals("New Task Title", response.getTitle());
        assertEquals(Task.Status.IN_PROGRESS, response.getStatus());
        assertEquals(Task.Priority.HIGH, response.getPriority());

        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void updateTask_ShouldThrowResourceNotFoundException_WhenTaskNotOwnedByUserOrNotExists() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByIdAndUser(10L, testUser)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTask(10L, taskRequest);
        });

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldRemoveTask_WhenUserOwnsTask() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByIdAndUser(10L, testUser)).thenReturn(Optional.of(testTask));

        assertDoesNotThrow(() -> taskService.deleteTask(10L));

        verify(taskRepository, times(1)).delete(testTask);
    }

    @Test
    void deleteTask_ShouldThrowResourceNotFoundException_WhenTaskNotOwnedByUserOrNotExists() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(testUser));
        when(taskRepository.findByIdAndUser(10L, testUser)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTask(10L);
        });

        verify(taskRepository, never()).delete(any(Task.class));
    }
}
