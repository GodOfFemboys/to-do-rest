package org.example.todorest.controller;

import org.example.todorest.dto.TaskDto;
import org.example.todorest.entity.Task;
import org.example.todorest.repository.TaskRepository;
import org.example.todorest.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> taskDto = taskService.getAllTasks();
        return ResponseEntity.ok(taskDto);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto taskDto = taskService.getTaskById(id);
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping("/task")
    public ResponseEntity<TaskDto> addTask(@RequestBody Task task) {
        TaskDto saved = taskService.saveTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @PatchMapping("/task/{id}")
    //Как будет на фронтенде? по сути при нажатии кнопки должен будет отправлять этот ендпоинт, но как сделать, так чтобы каждая кнопка "изменить" содержала в себе айди таски?
    public ResponseEntity<TaskDto> updateTask(@RequestBody Map<String, Object> updates, @PathVariable Long id) {
        TaskDto saved = taskService.patchTask(updates,id);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}