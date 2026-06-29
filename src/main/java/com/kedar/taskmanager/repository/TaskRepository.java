package com.kedar.taskmanager.repository;

import com.kedar.taskmanager.model.Task;
import com.kedar.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserAndStatus(User user, Task.Status status);
    List<Task> findByUserAndPriority(User user, Task.Priority priority);
    Optional<Task> findByIdAndUser(Long id, User user);
}