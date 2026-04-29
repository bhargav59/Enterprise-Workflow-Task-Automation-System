package com.enterprise.workflow.service;

import com.enterprise.workflow.entity.Task;
import com.enterprise.workflow.entity.Workflow;
import com.enterprise.workflow.repository.WorkflowRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WorkflowEngineService {
    private final WorkflowRepository workflowRepository;
    private final NotificationService notificationService;
    
    public WorkflowEngineService(WorkflowRepository workflowRepository, NotificationService notificationService) {
        this.workflowRepository = workflowRepository;
        this.notificationService = notificationService;
    }
    
    public void execute(Task task) {
        List<Workflow> workflows = workflowRepository.findByEnabled(true);
        
        for (Workflow workflow : workflows) {
            try {
                boolean conditionMet = evaluateCondition(workflow.getCondition(), task);
                if (conditionMet) {
                    executeAction(workflow.getAction(), task);
                }
            } catch (Exception e) {
                // Log error and continue
            }
        }
    }
    
    private boolean evaluateCondition(String condition, Task task) {
        if (condition == null) return false;
        
        condition = condition.toUpperCase();
        
        if (condition.contains("STATUS=DONE")) {
            return task.getStatus() == Task.TaskStatus.DONE;
        }
        if (condition.contains("STATUS=IN_PROGRESS")) {
            return task.getStatus() == Task.TaskStatus.IN_PROGRESS;
        }
        if (condition.contains("OVERDUE")) {
            java.time.LocalDate today = java.time.LocalDate.now();
            return task.getDeadline() != null && task.getDeadline().isBefore(today) 
                    && task.getStatus() != Task.TaskStatus.DONE;
        }
        
        return false;
    }
    
    private void executeAction(String action, Task task) {
        if (action == null) return;
        
        action = action.toUpperCase();
        
        if (action.contains("NOTIFY")) {
            if (task.getAssignedTo() != null) {
                notificationService.create(task.getAssignedTo(), 
                        "Workflow triggered for task: " + task.getTitle());
            }
        }
        if (action.contains("OVERDUE")) {
            task.setStatus(Task.TaskStatus.OVERDUE);
        }
    }
}