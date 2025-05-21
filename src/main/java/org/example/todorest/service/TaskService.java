package org.example.todorest.service;

import org.example.todorest.dto.CreateTaskDto;
import org.example.todorest.dto.PatchTaskDto;
import org.example.todorest.dto.TaskDto;

import java.util.List;

public interface TaskService {
    List<TaskDto> getAllTasks();

    TaskDto getTaskById(Long id);

    TaskDto saveTask(CreateTaskDto createTaskDto);

    public TaskDto patchTask(PatchTaskDto patchTaskDto, Long id);

    void deleteTask(Long id);

}
