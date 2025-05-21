package org.example.todorest.service;

import org.example.todorest.dto.TaskDto;
import org.example.todorest.entity.Task;
import org.example.todorest.entity.User;
import org.example.todorest.mapper.TaskMapper;
import org.example.todorest.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, CustomUserDetailsService customUserDetailsService, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDto> getAllTasks() {
        User user = customUserDetailsService.getCurrentUser();
        return taskRepository.findAllByUser(user).stream().map((taskMapper::toDto)).toList();
    }

    @Override
    public TaskDto getTaskById(Long id) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        // сравниваем не id тасок, а id user-ов
        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can't get the task");
        }
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto saveTask(Task task) {

        User user = customUserDetailsService.getCurrentUser();
        if (task.getId() == null) { //необязательная строчка.
            task.setUser(user);
        }
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public TaskDto patchTask(Map<String, Object> patch, Long id) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")); // если айди есть, но по нему ничего не находиться
        // валидация по юзеру
        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can't update the task"); //попытка изменить не свою таску
        }
        // ВАЛИДАЦИЯ ДАННЫХ В JSON ЗАПРОСЕ
        if (patch.containsKey("completed")) {
            Object completed = patch.get("completed");
            if (completed == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "completed cannot be null");
            }
            if (!(completed instanceof Boolean)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "completed must be true or false");
            }
            task.setCompleted((boolean) completed);
        }
        if (patch.containsKey("description")) {
            Object description = patch.get("description");
            if (description == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description cannot be null");
            }
            if (!(description instanceof String desc)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description must be type String");
            }
            if (desc.length() > 255) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description too long");
            }
            task.setDescription(desc);
        }
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        User user = customUserDetailsService.getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        // сравниваем не id тасок, а id user-ов
        if (!task.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can't delete the task");
        }
        taskRepository.deleteById(id);
    }
}
