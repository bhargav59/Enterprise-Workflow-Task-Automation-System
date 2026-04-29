package com.enterprise.workflow.service;

import com.enterprise.workflow.dto.TaskRequest;
import com.enterprise.workflow.entity.Project;
import com.enterprise.workflow.entity.Task;
import com.enterprise.workflow.entity.User;
import com.enterprise.workflow.repository.ProjectRepository;
import com.enterprise.workflow.repository.TaskRepository;
import com.enterprise.workflow.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final WorkflowEngineService workflowEngineService;
    private final NotificationService notificationService;
    
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
            UserRepository userRepository, WorkflowEngineService workflowEngineService,
            NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.workflowEngineService = workflowEngineService;
        this.notificationService = notificationService;
    }
    
    public Task create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus() != null ? request.getStatus() : Task.TaskStatus.TODO);
        
        if (request.getDeadline() != null) {
            task.setDeadline(LocalDate.parse(request.getDeadline()));
        }
        
        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            task.setProject(project);
        }
        
        if (request.getAssignedToId() != null) {
            User user = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(user);
        }
        
        Task savedTask = taskRepository.save(task);
        workflowEngineService.execute(savedTask);
        return savedTask;
    }
    
    public List<Task> getAll() {
        return taskRepository.findAll();
    }
    
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }
    
    public Task update(Long id, TaskRequest request) {
        Task task = getById(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        
        if (request.getDeadline() != null) {
            task.setDeadline(LocalDate.parse(request.getDeadline()));
        }
        
        if (request.getAssignedToId() != null) {
            User user = userRepository.findById(request.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            task.setAssignedTo(user);
        }
        
        Task updatedTask = taskRepository.save(task);
        workflowEngineService.execute(updatedTask);
        return updatedTask;
    }
    
    public Task updateStatus(Long id, Task.TaskStatus status) {
        Task task = getById(id);
        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        
        if (status == Task.TaskStatus.DONE && task.getAssignedTo() != null) {
            notificationService.create(task.getAssignedTo(), 
                    "Task '" + task.getTitle() + "' marked as DONE");
        }
        
        workflowEngineService.execute(updatedTask);
        return updatedTask;
    }
    
    public void delete(Long id) {
        taskRepository.delete(getById(id));
    }
    
    public List<Task> getByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }
    
    public List<Task> getByUser(Long userId) {
        return taskRepository.findByAssignedToId(userId);
    }
    
    public List<Task> getOverdueTasks() {
        List<Task> allTasks = taskRepository.findAll();
        LocalDate today = LocalDate.now();
        return allTasks.stream()
                .filter(t -> t.getDeadline() != null && t.getDeadline().isBefore(today)
                        && t.getStatus() != Task.TaskStatus.DONE)
                .toList();
    }
}