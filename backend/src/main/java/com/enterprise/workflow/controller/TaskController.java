package com.enterprise.workflow.controller;

import com.enterprise.workflow.dto.TaskRequest;
import com.enterprise.workflow.entity.Task;
import com.enterprise.workflow.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @PostMapping
    public ResponseEntity<Task> create(@RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.create(request));
    }
    
    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.update(id, request));
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Task.TaskStatus status = Task.TaskStatus.valueOf(body.get("status"));
        return ResponseEntity.ok(taskService.updateStatus(id, status));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Task deleted"));
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getByProject(projectId));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getByUser(userId));
    }
    
    @GetMapping("/overdue")
    public ResponseEntity<List<Task>> getOverdue() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }
}