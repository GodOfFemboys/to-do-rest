package org.example.todorest.controller;

import jakarta.validation.Valid;
import org.example.todorest.dto.CreateTaskDto;
import org.example.todorest.dto.PatchTaskDto;
import org.example.todorest.dto.TaskDto;
import org.example.todorest.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
    public ResponseEntity<TaskDto> addTask(@Valid @RequestBody CreateTaskDto createTaskDto) {
        TaskDto saved = taskService.saveTask(createTaskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);

    }

    @PatchMapping("/task/{id}")
    public ResponseEntity<TaskDto> updateTask(@Valid @RequestBody PatchTaskDto patchTaskDto, @PathVariable Long id) {
        TaskDto saved = taskService.patchTask(patchTaskDto, id);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}