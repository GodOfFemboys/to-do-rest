package org.example.todorest.service;

import org.example.todorest.dto.TaskDto;
import org.example.todorest.entity.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {
    List<TaskDto> getAllTasks();

    TaskDto getTaskById(Long id) ;

    TaskDto saveTask(Task task);

    public TaskDto patchTask(Map<String, Object> patch, Long id);

    void deleteTask(Long id);

}
