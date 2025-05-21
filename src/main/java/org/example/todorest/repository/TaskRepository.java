package org.example.todorest.repository;

import org.example.todorest.entity.Task;
import org.example.todorest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);
}
